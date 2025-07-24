package com.franchise.franchises.domain.entities.usecases;

import com.franchise.franchises.domain.entities.models.Franchise;
import com.franchise.franchises.domain.entities.ports.in.FranchiseServicePort;
import com.franchise.franchises.domain.entities.ports.out.FranchisePersistencePort;
import reactor.core.publisher.Mono;

public class FranchiseUseCase implements FranchiseServicePort {
    private final FranchisePersistencePort franchisePersistencePort;

    public FranchiseUseCase(FranchisePersistencePort franchisePersistencePort) {
        this.franchisePersistencePort = franchisePersistencePort;
    }

    @Override
    public Mono<Franchise> save(Franchise franchise) {
        return franchisePersistencePort.save(franchise);
    }

    @Override
    public Mono<Franchise> renameById(String id, String newName) {
        return franchisePersistencePort.renameById(id, newName);
    }
}
