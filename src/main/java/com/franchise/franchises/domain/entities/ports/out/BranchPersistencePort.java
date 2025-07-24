package com.franchise.franchises.domain.entities.ports.out;

import com.franchise.franchises.domain.entities.models.Branch;
import reactor.core.publisher.Mono;

public interface BranchPersistencePort {
    Mono<Branch> save(Branch branch);
    Mono<Branch> renameById(String id, String newName);
}
