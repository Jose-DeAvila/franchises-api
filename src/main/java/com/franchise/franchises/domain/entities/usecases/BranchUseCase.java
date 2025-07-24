package com.franchise.franchises.domain.entities.usecases;

import com.franchise.franchises.domain.entities.models.Branch;
import com.franchise.franchises.domain.entities.ports.in.BranchServicePort;
import com.franchise.franchises.domain.entities.ports.out.BranchPersistencePort;
import reactor.core.publisher.Mono;

public class BranchUseCase implements BranchServicePort {
    private final BranchPersistencePort branchPersistencePort;

    public BranchUseCase(BranchPersistencePort branchPersistencePort) {
        this.branchPersistencePort = branchPersistencePort;
    }

    @Override
    public Mono<Branch> save(Branch branch) {
        return branchPersistencePort.save(branch);
    }

    @Override
    public Mono<Branch> renameById(String id, String newName) {
        return branchPersistencePort.renameById(id, newName);
    }
}
