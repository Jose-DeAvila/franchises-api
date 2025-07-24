package com.franchise.franchises.infrastructure.adapters;

import com.franchise.franchises.domain.entities.exceptions.EntityNotFound;
import com.franchise.franchises.domain.entities.models.Franchise;
import com.franchise.franchises.domain.entities.ports.out.FranchisePersistencePort;
import com.franchise.franchises.infrastructure.mappers.FranchiseMapper;
import com.franchise.franchises.infrastructure.models.FranchiseEntity;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class FranchiseAdapter implements FranchisePersistencePort {
    private final DynamoDbAsyncTable<FranchiseEntity> franchiseTable;

    public FranchiseAdapter(DynamoDbEnhancedAsyncClient enhancedAsyncClient) {
        this.franchiseTable = enhancedAsyncClient.table("Franchise", TableSchema.fromBean(FranchiseEntity.class));
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        FranchiseEntity entity = FranchiseMapper.toDynamoEntity(franchise);

        return Mono.fromFuture(() -> franchiseTable.putItem(entity))
                .thenReturn(franchise)
                .doOnError(error -> System.out.println("Error saving the Franchise: " + error.getMessage()));
    }

    @Override
    public Mono<Franchise> renameById(String id, String newName) {
        Key key = Key.builder().partitionValue(id).build();
        return Mono.fromFuture(() -> franchiseTable.getItem(key))
                .switchIfEmpty(Mono.error(new EntityNotFound("Franchise", id)))
                .flatMap(foundedFranchise -> {
                   foundedFranchise.setName(newName);
                   Franchise franchise = new Franchise(foundedFranchise.getId(), foundedFranchise.getName());

                   return Mono.fromFuture(() -> franchiseTable.updateItem(foundedFranchise)).thenReturn(franchise);
                });
    }
}
