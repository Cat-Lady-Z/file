package com.lbx.distribution.manageserver.entity.order;

import java.math.BigDecimal;

/**
 * 订单货物详情
 */
public class OrderCargoItem {
    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 物品编号
     */
    private String itemId;

    /**
     * 物品名称
     */
    private String itemName;

    /**
     * 物品数量
     */
    private Integer itemQuantity;

    /**
     * 物品价格
     */
    private BigDecimal itemPrice;

    /**
     * 物品实际价格
     */
    private BigDecimal itemActualPrice;

    /**
     * 物品单价
     */
    private BigDecimal itemSinglePrice;

    /**
     * 物品尺寸
     */
    private Integer itemSize;

    /**
     * 备注
     */
    private String itemRemark;

    /**
     * 是否需要包装
     */
    private Byte isNeedPackage;

    /**
     * 是否代理购买
     */
    private Byte isAgentPurchase;

    /**
     * 代理购买价格
     */
    private BigDecimal agentPurchasePrice;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getItemActualPrice() {
        return itemActualPrice;
    }

    public void setItemActualPrice(BigDecimal itemActualPrice) {
        this.itemActualPrice = itemActualPrice;
    }

    public Integer getItemSize() {
        return itemSize;
    }

    public void setItemSize(Integer itemSize) {
        this.itemSize = itemSize;
    }

    public String getItemRemark() {
        return itemRemark;
    }

    public void setItemRemark(String itemRemark) {
        this.itemRemark = itemRemark;
    }

    public Byte getIsNeedPackage() {
        return isNeedPackage;
    }

    public void setIsNeedPackage(Byte isNeedPackage) {
        this.isNeedPackage = isNeedPackage;
    }

    public Byte getIsAgentPurchase() {
        return isAgentPurchase;
    }

    public void setIsAgentPurchase(Byte isAgentPurchase) {
        this.isAgentPurchase = isAgentPurchase;
    }

    public BigDecimal getAgentPurchasePrice() {
        return agentPurchasePrice;
    }

    public void setAgentPurchasePrice(BigDecimal agentPurchasePrice) {
        this.agentPurchasePrice = agentPurchasePrice;
    }

    public BigDecimal getItemSinglePrice() {
        return itemSinglePrice;
    }

    public void setItemSinglePrice(BigDecimal itemSinglePrice) {
        this.itemSinglePrice = itemSinglePrice;
    }
}