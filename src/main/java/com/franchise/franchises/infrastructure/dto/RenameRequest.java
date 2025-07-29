package com.franchise.franchises.infrastructure.dto;

public class RenameRequest {
    private String newName;

    public RenameRequest() {
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
