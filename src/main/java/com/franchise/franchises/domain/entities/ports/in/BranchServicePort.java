package com.franchise.franchises.domain.entities.ports.in;

import com.franchise.franchises.domain.entities.models.Branch;

public interface BranchServicePort {
    void save(Branch branch);
    void renameById(String id, String newName);
}
