package com.example.demo.response;

/***
 * xml转集合--接收实体
 */
public class XmlReturn {

    private String warehouseCode;
    private String itemCode;
    private String itemId;
    private String inventoryType;
    private String imperfectGrade;
    private String quantity;
    private String lockQuantity;

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }

    public String getImperfectGrade() {
        return imperfectGrade;
    }

    public void setImperfectGrade(String imperfectGrade) {
        this.imperfectGrade = imperfectGrade;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLockQuantity() {
        return lockQuantity;
    }

    public void setLockQuantity(String lockQuantity) {
        this.lockQuantity = lockQuantity;
    }
}
