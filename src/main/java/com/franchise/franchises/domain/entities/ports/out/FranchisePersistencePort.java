package com.franchise.franchises.domain.entities.ports.out;

import com.franchise.franchises.domain.entities.models.Franchise;
import reactor.core.publisher.Mono;

public interface FranchisePersistencePort {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> renameById(String id, String newName);
}
