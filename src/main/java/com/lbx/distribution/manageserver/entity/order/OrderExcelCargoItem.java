package com.lbx.distribution.manageserver.entity.order;

import java.math.BigDecimal;

/**
 * 订单货物详情
 */
public class OrderExcelCargoItem {

    private String orderId;

    /**
     * 物品名称
     */
    private String itemName;

    /**
     * 物品数量
     */
    private Integer itemQuantity;

    /**
     * 物品总价
     */
    private BigDecimal itemActualPrice;

    /**
     * 物品单价
     */
    private BigDecimal itemSinglePrice;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public BigDecimal getItemActualPrice() {
        return itemActualPrice;
    }

    public void setItemActualPrice(BigDecimal itemActualPrice) {
        this.itemActualPrice = itemActualPrice;
    }

    public BigDecimal getItemSinglePrice() {
        return itemSinglePrice;
    }

    public void setItemSinglePrice(BigDecimal itemSinglePrice) {
        this.itemSinglePrice = itemSinglePrice;
    }
}