package com.franchise.franchises.domain.entities.ports.out;

import com.franchise.franchises.domain.entities.models.BranchProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BranchProductPersistencePort {
    Mono<BranchProduct> save(BranchProduct branchProduct);
    Mono<Void> deleteById(String branchId, String productId);
    Mono<BranchProduct> renameById(String branchId, String productId, String newName);
    Mono<BranchProduct> changeStockById(String branchId, String productId, Integer quantity);
    Flux<BranchProduct> getTopProductByStockPerBranch(String franchiseId);
}
