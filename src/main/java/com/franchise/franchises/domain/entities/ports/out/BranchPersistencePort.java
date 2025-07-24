package com.franchise.franchises.domain.entities.ports.out;

import com.franchise.franchises.domain.entities.models.Branch;

public interface BranchPersistencePort {
    void save(Branch branch);
    void renameById(String id, String newName);
}
