package com.lbx.distribution.manageserver.entity.order;

/**
 * 订单统计实体
 */
public class OrderStatEntity {
    /**
     * 商户id
     */
    private Integer merchantId;

    /**
     * 管理公司ID
     */
    private Integer companyId;

    /**
     * 门店id
     */
    private Integer shopId;

    public OrderStatEntity(Integer merchantId, Integer companyId, Integer shopId) {
        this.merchantId = merchantId;
        this.companyId = companyId;
        this.shopId = shopId;
    }

    public OrderStatEntity() {
        super();
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}