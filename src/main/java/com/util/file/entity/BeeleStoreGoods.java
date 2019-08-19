package com.util.file.entity;

public class BeeleStoreGoods {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private Long skuId;

    /**
     * 
     */
    private Long upc;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Integer storeId;

    /**
     * 
     */
    private Short status;

    /**
     * 
     */
    private Short upcType;

    /**
     * 
     */
    private Integer goodsId;

    public BeeleStoreGoods() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getUpc() {
        return upc;
    }

    public void setUpc(Long upc) {
        this.upc = upc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Short getUpcType() {
        return upcType;
    }

    public void setUpcType(Short upcType) {
        this.upcType = upcType;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }
}