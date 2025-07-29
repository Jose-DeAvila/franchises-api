package com.franchise.franchises.infrastructure.dto;

public class UpdateStockRequest {
    Integer deltaStock;

    public UpdateStockRequest(Integer deltaStock) {
        this.deltaStock = deltaStock;
    }

    public Integer getDeltaStock() {
        return deltaStock;
    }

    public void setDeltaStock(Integer deltaStock) {
        this.deltaStock = deltaStock;
    }
}
