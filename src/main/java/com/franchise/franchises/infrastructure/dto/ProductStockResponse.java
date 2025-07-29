package com.franchise.franchises.infrastructure.dto;

public class ProductStockResponse {
    private String branchId;
    private String productId;
    private String productName;
    private Integer stock;

    public ProductStockResponse(
            String branchId,
            String productId,
            String productName,
            Integer stock) {
        this.branchId = branchId;
        this.productId = productId;
        this.productName = productName;
        this.stock = stock;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
