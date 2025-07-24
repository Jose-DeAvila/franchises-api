package com.franchise.franchises.domain.entities.ports.in;

import com.franchise.franchises.domain.entities.models.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseServicePort {
    Mono<Franchise> save(Franchise franchise);
    Mono<Franchise> renameById(String id, String newName);
}
