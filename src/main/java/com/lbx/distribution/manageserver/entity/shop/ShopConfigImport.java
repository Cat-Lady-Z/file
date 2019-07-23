package com.lbx.distribution.manageserver.entity.shop;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 门店导入实体
 */
public class ShopConfigImport implements Serializable {

    /**
     * 门店id
     */
    private Integer shopId;

    /**
     * 渠道id
     */
    private Integer channelId;

    /**
     * 渠道门店id
     */
    private String channelShopId;

    /**
     * 相同门店标识
     */
    private String sign;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelShopId() {
        return channelShopId;
    }

    public void setChannelShopId(String channelShopId) {
        this.channelShopId = channelShopId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
