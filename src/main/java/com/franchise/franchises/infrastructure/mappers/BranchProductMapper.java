package com.franchise.franchises.infrastructure.mappers;

import com.franchise.franchises.domain.entities.models.BranchProduct;
import com.franchise.franchises.infrastructure.models.BranchProductEntity;

public class BranchProductMapper {
    public static BranchProductEntity toDynamoEntity(BranchProduct branchProduct) {
        BranchProductEntity entity = new BranchProductEntity();

        entity.setBranchId(branchProduct.branchId());
        entity.setProductId(branchProduct.productId());
        entity.setName(branchProduct.name());
        entity.setStock(branchProduct.stock());

        return entity;
    }

    public static BranchProduct toModel(BranchProductEntity branchProductEntity) {
        return new BranchProduct(
                branchProductEntity.getBranchId(),
                branchProductEntity.getProductId(),
                branchProductEntity.getName(),
                branchProductEntity.getStock()
        );
    }
}
