package com.franchise.franchises.infrastructure.adapters;

import com.franchise.franchises.domain.entities.exceptions.EntityNotFound;
import com.franchise.franchises.domain.entities.models.BranchProduct;
import com.franchise.franchises.domain.entities.ports.out.BranchProductPersistencePort;
import com.franchise.franchises.infrastructure.mappers.BranchProductMapper;
import com.franchise.franchises.infrastructure.models.BranchEntity;
import com.franchise.franchises.infrastructure.models.BranchProductEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.Comparator;

public class BranchProductAdapter implements BranchProductPersistencePort {
    private final DynamoDbAsyncTable<BranchEntity> branchTable;
    private final DynamoDbAsyncTable<BranchProductEntity> branchProductTable;

    public BranchProductAdapter(DynamoDbEnhancedAsyncClient enhancedAsyncClient) {
        this.branchTable = enhancedAsyncClient.table("Branch", TableSchema.fromBean(BranchEntity.class));
        this.branchProductTable = enhancedAsyncClient.table("BranchProduct", TableSchema.fromBean(BranchProductEntity.class));
    }

    @Override
    public Mono<BranchProduct> save(BranchProduct branchProduct) {
        Key branchKey = Key.builder().partitionValue(branchProduct.branchId()).build();
        BranchProductEntity entity = BranchProductMapper.toDynamoEntity(branchProduct);

        return Mono.fromFuture(() -> branchTable.getItem(branchKey))
                .switchIfEmpty(Mono.error(new EntityNotFound("Branch", branchProduct.branchId())))
                .flatMap(branch -> Mono.fromFuture(() -> branchProductTable.putItem(entity)))
                .thenReturn(branchProduct)
                .doOnError(e -> System.out.println("Error adding product to the branch"));
    }

    @Override
    public Mono<BranchProduct> renameById(String branchId, String productId, String newName) {
        Key productKey = Key.builder().partitionValue(branchId).sortValue(productId).build();
        Key branchKey = Key.builder().partitionValue(branchId).build();

        return Mono.fromFuture(() -> branchTable.getItem(branchKey))
                .flatMap(branch -> {
                    if (branch == null) {
                        return Mono.error(new EntityNotFound("Branch", branchId));
                    }

                    return Mono.fromFuture(() -> branchProductTable.getItem(productKey))
                            .flatMap(foundedProduct -> {
                                if (foundedProduct == null) {
                                    return Mono.error(new EntityNotFound("Product", productId));
                                }

                                foundedProduct.setName(newName);

                                return Mono.fromFuture(() -> branchProductTable.updateItem(foundedProduct))
                                        .thenReturn(BranchProductMapper.toModel(foundedProduct));
                            })
                            .switchIfEmpty(Mono.error(new EntityNotFound("Product", productId)));
                })
                .switchIfEmpty(Mono.error(new EntityNotFound("Branch", branchId)));
    }

    @Override
    public Mono<Void> deleteById(String branchId, String productId) {
        Key productKey = Key.builder().partitionValue(branchId).sortValue(productId).build();
        Key branchKey = Key.builder().partitionValue(branchId).build();

        return Mono.fromFuture(() -> branchTable.getItem(branchKey))
                .switchIfEmpty(Mono.error(new EntityNotFound("Branch", branchId)))
                .flatMap(branch -> Mono.fromFuture(() -> branchProductTable.getItem(productKey))
                            .switchIfEmpty(Mono.error(new EntityNotFound("Product", productId)))
                            .flatMap(product -> Mono.fromFuture(() -> branchProductTable.deleteItem(productKey)))
                            .then()
                );
    }

    @Override
    public Mono<BranchProduct> changeStockById(String branchId, String productId, Integer quantity) {
        Key branchKey = Key.builder().partitionValue(branchId).build();
        Key productKey = Key.builder().partitionValue(branchId).sortValue(productId).build();

        return Mono.fromFuture(() -> branchTable.getItem(branchKey))
                .flatMap(branch -> {
                    if (branch == null) {
                        return Mono.error(new EntityNotFound("Branch", branchId));
                    }

                    return Mono.fromFuture(() -> branchProductTable.getItem(productKey))
                            .flatMap(foundedProduct -> {
                                if (foundedProduct == null) {
                                    return Mono.error(new EntityNotFound("Product", productId));
                                }

                                foundedProduct.setStock(foundedProduct.getStock() + quantity);

                                return Mono.fromFuture(() -> branchProductTable.updateItem(foundedProduct))
                                        .thenReturn(BranchProductMapper.toModel(foundedProduct));
                            })
                            .switchIfEmpty(Mono.error(new EntityNotFound("Product", productId)));
                })
                .switchIfEmpty(Mono.error(new EntityNotFound("Branch", branchId)));
    }

    @Override
    public Flux<BranchProduct> getTopProductByStockPerBranch(String franchiseId) {
        return Flux.from(branchTable.scan())
                .flatMap(scanPage -> Flux.fromIterable(scanPage.items()))
                .filter(branch -> franchiseId.equals(branch.getFranchiseId()))
                .flatMap(branch -> {
                    String branchId = branch.getId();

                    return Flux.from(branchProductTable.query(r ->
                                    r.queryConditional(QueryConditional.keyEqualTo(
                                            Key.builder().partitionValue(branchId).build()
                                    ))
                            ))
                            .flatMap(queryPage -> Flux.fromIterable(queryPage.items()))
                            .sort(Comparator.comparingInt(BranchProductEntity::getStock).reversed())
                            .next() // Take the first (i.e., highest stock)
                            .map(BranchProductMapper::toModel);
                });
    }

}
