package com.franchise.franchises.infrastructure.mappers;

import com.franchise.franchises.domain.entities.models.Branch;
import com.franchise.franchises.infrastructure.models.BranchEntity;

public class BranchMapper {
    public static BranchEntity toDynamoEntity(Branch branch) {
        BranchEntity branchEntity = new BranchEntity();

        branchEntity.setId(branch.id());
        branchEntity.setName(branch.name());
        branchEntity.setFranchiseId(branch.franchiseId());

        return branchEntity;
    }

    public static Branch toModel(BranchEntity branchEntity) {
        return new Branch(branchEntity.getId(), branchEntity.getName(), branchEntity.getFranchiseId());
    }
}
