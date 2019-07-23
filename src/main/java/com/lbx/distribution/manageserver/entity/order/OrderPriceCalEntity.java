package com.lbx.distribution.manageserver.entity.order;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 预估订单配送价格实体
 */
public class OrderPriceCalEntity implements Serializable {

    private final static long serialVersionUID = 1L;

    /**
     * 城市国标
     */
    @JSONField(name="city_code")
    private String cityCode;

    /**
     * 渠道id
     */
    @JSONField(name="channel_id")
    private Integer channelId;

    /**
     * 商户id
     */
    @JSONField(name="merchant_id")
    private Integer merchantId;

    /**
     * 货品重量
     */
    @JSONField(name="cargo_weight")
    private BigDecimal cargoWeight;

    /**
     * 起始经纬度
     */
    @JSONField(name="origin")
    private String origin;

    /**
     * 目标经纬度
     */
    @JSONField(name="destination")
    private String destination;

    /**
     * 预估价格
     */
    private BigDecimal deliveryPrice;

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public BigDecimal getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(BigDecimal cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public BigDecimal getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
}
