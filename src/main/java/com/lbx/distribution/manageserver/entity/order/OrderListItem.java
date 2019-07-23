package com.lbx.distribution.manageserver.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: 订单列表子项
 * @Description: //
 */
public class OrderListItem {

    /**
     * 订单类型， 1: 即时单(尽快送达，限当日订单) 2: 预约单
     */
    private Integer orderType;

    /**
     * 订单类型名称
     */
    private String orderTypeName;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 第三方订单ID(前端显示- 订单号)
     */
    private String originId;

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 订单所在城市名称
     */
    private String cityName;

    /**
     * 订单所在城市的code
     */
    private String cityCode;

    /**
     * 配送渠道ID
     */
    private Integer channelId;

    /**
     * 配送渠道名称(需要二次查询，第一次查id，第二次查询填充)
     */
    private String channelName;

    /**
     * 渠道配送平台运单ID
     */
    private String channelPeisongId;

    /**
     * 配送员姓名（订单已被骑手接单后会返回骑手信息）
     */
    private String courierName;

    /**
     * 配送员电话（订单已被骑手接单后会返回骑手信息）
     */
    private String courierPhone;

    /**
     * 第三方订单来源标识(如o2o商城,美团)
     */
    private Integer originSource;

    /**
     * 订单来源名称
     */
    private String originSourceName;

    /**
     * 配送费,单位为元
     */
    private BigDecimal predictPrice;

    /**
     * 分发状态(0:未分发;1:分发渠道成功;2:订单已完成;3:订单已取消,-1:失败订单)
     */
    private Integer distributeStatus;

    /**
     * 状态（0-默认，待接单,1-配货中;2-已接单, 3-配送
     中，4-已完成;5-已取消）
     */
    private Integer statusCode;

    /**
     * 状态描述
     */
    private String statusMsg;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelPeisongId() {
        return channelPeisongId;
    }

    public void setChannelPeisongId(String channelPeisongId) {
        this.channelPeisongId = channelPeisongId;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getCourierPhone() {
        return courierPhone;
    }

    public void setCourierPhone(String courierPhone) {
        this.courierPhone = courierPhone;
    }

    public Integer getOriginSource() {
        return originSource;
    }

    public void setOriginSource(Integer originSource) {
        this.originSource = originSource;
    }

    public String getOriginSourceName() {
        return originSourceName;
    }

    public void setOriginSourceName(String originSourceName) {
        this.originSourceName = originSourceName;
    }

    public BigDecimal getPredictPrice() {
        return predictPrice;
    }

    public void setPredictPrice(BigDecimal predictPrice) {
        this.predictPrice = predictPrice;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public Integer getDistributeStatus() {
        return distributeStatus;
    }

    public void setDistributeStatus(Integer distributeStatus) {
        this.distributeStatus = distributeStatus;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }
}
