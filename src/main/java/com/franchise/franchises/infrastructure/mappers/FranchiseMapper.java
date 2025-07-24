package com.franchise.franchises.infrastructure.mappers;

import com.franchise.franchises.domain.entities.models.Franchise;
import com.franchise.franchises.infrastructure.models.FranchiseEntity;

public class FranchiseMapper {
    public static FranchiseEntity toDynamoEntity(Franchise franchise) {
        FranchiseEntity franchiseEntity = new FranchiseEntity();

        franchiseEntity.setId(franchise.id());
        franchiseEntity.setName(franchise.name());

        return franchiseEntity;
    }

    public static Franchise toModel(FranchiseEntity franchiseEntity) {
        return new Franchise(franchiseEntity.getId(), franchiseEntity.getName());
    }
}
