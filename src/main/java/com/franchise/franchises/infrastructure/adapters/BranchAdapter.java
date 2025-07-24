package com.franchise.franchises.infrastructure.adapters;

import com.franchise.franchises.domain.entities.exceptions.EntityNotFound;
import com.franchise.franchises.domain.entities.models.Branch;
import com.franchise.franchises.domain.entities.ports.out.BranchPersistencePort;
import com.franchise.franchises.infrastructure.mappers.BranchMapper;
import com.franchise.franchises.infrastructure.models.BranchEntity;
import com.franchise.franchises.infrastructure.models.FranchiseEntity;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class BranchAdapter implements BranchPersistencePort {
    private final DynamoDbAsyncTable<FranchiseEntity> franchiseTable;
    private final DynamoDbAsyncTable<BranchEntity> branchTable;

    public BranchAdapter(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient) {
        this.franchiseTable = dynamoDbEnhancedAsyncClient.table("Franchise", TableSchema.fromBean(FranchiseEntity.class));
        this.branchTable = dynamoDbEnhancedAsyncClient.table("Branch", TableSchema.fromBean(BranchEntity.class));
    }


    @Override
    public Mono<Branch> save(Branch branch) {
        Key franchiseId = Key.builder().partitionValue(branch.franchiseId()).build();
        BranchEntity entity = BranchMapper.toDynamoEntity(branch);

        return Mono.fromFuture(() -> franchiseTable.getItem(franchiseId))
                .switchIfEmpty(Mono.error(new EntityNotFound("Franchise", branch.franchiseId())))
                .flatMap(franchise -> Mono.fromFuture(() -> branchTable.putItem(entity)))
                .thenReturn(branch)
                .doOnError(e -> System.out.println("Error adding branch to franchise: " + e.getMessage()));
    }

    @Override
    public Mono<Branch> renameById(String id, String newName) {
        Key key = Key.builder().partitionValue(id).build();

        return Mono.fromFuture(() -> branchTable.getItem(key))
                .switchIfEmpty(Mono.error(new EntityNotFound("Branch", id)))
                .flatMap(foundedBranch -> {
                    foundedBranch.setName(newName);
                    Branch branch = new Branch(foundedBranch.getId(), foundedBranch.getName(),  foundedBranch.getFranchiseId());
                    return Mono.fromFuture(() -> branchTable.putItem(foundedBranch)).thenReturn(branch);
                });
    }
}
