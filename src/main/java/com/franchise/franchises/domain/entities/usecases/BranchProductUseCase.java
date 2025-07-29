package com.franchise.franchises.domain.entities.usecases;

import com.franchise.franchises.domain.entities.models.BranchProduct;
import com.franchise.franchises.domain.entities.ports.in.BranchProductServicePort;
import com.franchise.franchises.domain.entities.ports.out.BranchProductPersistencePort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class BranchProductUseCase implements BranchProductServicePort {
    private final BranchProductPersistencePort branchProductPersistencePort;

    public BranchProductUseCase(BranchProductPersistencePort branchProductPersistencePort) {
        this.branchProductPersistencePort = branchProductPersistencePort;
    }

    @Override
    public Mono<BranchProduct> save(BranchProduct branchProduct) {
        return branchProductPersistencePort.save(branchProduct);
    }

    @Override
    public Mono<Void> deleteById(String branchId, String productId) {
        return branchProductPersistencePort.deleteById(branchId, productId);
    }

    @Override
    public Mono<BranchProduct> renameById(String branchId, String productId, String newName) {
        return branchProductPersistencePort.renameById(branchId, productId, newName);
    }

    @Override
    public Mono<BranchProduct> changeStockById(String branchId, String productId, Integer quantity) {
        return branchProductPersistencePort.changeStockById(branchId, productId, quantity);
    }

    @Override
    public Flux<BranchProduct> getTopProductByStockPerBranch(String franchiseId) {
        return branchProductPersistencePort.getTopProductByStockPerBranch(franchiseId);
    }
}
