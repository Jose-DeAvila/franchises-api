package com.franchise.franchises.domain.entities.exceptions;

public class EntityNotFound extends RuntimeException {
    public EntityNotFound(String entityName, String entityId) {
        super(entityName + " not found for id " + entityId);
    }
}
