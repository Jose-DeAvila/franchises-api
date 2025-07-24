package com.franchise.franchises.domain.entities.ports.in;

import com.franchise.franchises.domain.entities.models.BranchProduct;

public interface BranchProductServicePort {
    void save(BranchProduct branchProduct);
    void renameById(String branchId, String productId, String newName);
}
