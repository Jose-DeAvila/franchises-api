package com.franchise.franchises.domain.entities.ports.out;

import com.franchise.franchises.domain.entities.models.BranchProduct;

public interface BranchProductServicePort {
    void save(BranchProduct branchProduct);
    void renameById(String branchId, String productId, String newName);
}
