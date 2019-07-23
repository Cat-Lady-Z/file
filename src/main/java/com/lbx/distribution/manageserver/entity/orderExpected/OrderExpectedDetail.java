package com.lbx.distribution.manageserver.entity.orderExpected;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lbx.distribution.manageserver.entity.order.OrderCargoItem;
import com.lbx.distribution.manageserver.entity.order.OrderDetail;
import com.lbx.distribution.manageserver.entity.order.OrderDistributionStatusVo;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: 订单详情实体
 * @Description: //
 */
public class OrderExpectedDetail extends OrderDetail {

    /**
     * 预约单发送标识，1:未发送，2：已发送， 5：已取消
     */
    //@JsonProperty(value="send_id")
    private Integer sendId;

    /**
     * 订单类型， 1: 即时单(尽快送达，限当日订单) 2: 预约单
     */
    //@JsonProperty(value="order_type")
    private Integer orderType;

    /**
     * 订单类型名称
     */
    //@JsonProperty(value="order_type_name")
    private String orderTypeName;

    /**
     * 预约送达时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@JsonProperty(value="expected_delivery_time")
    private Date expectedDeliveryTime;

    /**
     * 当前配送状态
     */
    //@JsonProperty(value="active_status")
    private Integer activeStatus;

    /**
     * 当前配送状态标记id
     */
   // @JsonProperty(value="active_status_id")
    private Integer activeStatusId;

    /**
     * 订单id
     */
    //@JsonProperty(value="order_id")
    private String orderId;

    /**
     * 第三方订单ID(前端显示- 订单号)
     */
   // @JsonProperty(value="origin_id")
    private String originId;

    //预计送达时间（预留）

    /**
     * 配送距离,单位为米
     */
   // @JsonProperty(value="distance")
    private BigDecimal distance;

    /**
     * 预估配送价格
     */
    //@JsonProperty(value="predict_price")
    private BigDecimal predictPrice;

    //发货信息
    /**
     * 门店名称
     */
   // @JsonProperty(value="shop_name")
    private String shopName;

    //门店地址
   // @JsonProperty(value="address")
    private String address;

    //联系人电话
   // @JsonProperty(value="contact_phone")
    private String contactPhone;

    //顾客信息
    /**
     * 收货人姓名
     */
    //@JsonProperty(value="receiver_name")
    private String receiverName;

    /**
     * 收货人地址
     */
    //@JsonProperty(value="receiver_address")
    private String receiverAddress;

    /**
     * 收货人手机号（手机号和座机号必填一项）
     */
   // @JsonProperty(value="receiver_phone")
    private String receiverPhone;

    /**
     * 收货人手机号（手机号和座机号必填一项）
     */
   // @JsonProperty(value="receiver_tel")
    private String receiverTel;

    //订单明细 - 货物列表
    //@JsonProperty(value="order_cargo_items")
    List<OrderCargoItem> orderCargoItems;

    //配送信息
    /**
     * 配送渠道名称(需要二次查询，第一次查id，第二次查询填充)
     */
    //@JsonProperty(value="channel_name")
    private String channelName;

    /**
     * 渠道配送平台运单ID
     */
    //@JsonProperty(value="channel_peisong_id")
    private String channelPeisongId;

    /**
     * 配送员姓名（订单已被骑手接单后会返回骑手信息）
     */
   // @JsonProperty(value="courier_name")
    private String courierName;

    /**
     * 配送员电话（订单已被骑手接单后会返回骑手信息）
     */
    //@JsonProperty(value="courier_phone")
    private String courierPhone;

    //订单配送状态记录表
    //@JsonProperty(value="order_distStatus_list")
    List<OrderDistributionStatusVo> orderDistStatusList;

    /**
     * 订单所在城市名称
     */
   // @JsonProperty(value="city_name")
    private String cityName;

    /**
     * 确定分发的配送渠道ID
     */
    //@JsonProperty(value="channel_id")
    private Integer channelId;

    /**
     * 第三方订单来源标识(如o2o商城,美团)
     */
   // @JsonProperty(value="origin_source")
    private Integer originSource;

    /**
     * 商户id
     */
   // @JsonProperty(value="merchant_id")
    private Integer merchantId;

    /**
     * 门店id
     */
    //@JsonProperty(value="shop_id")
    private Integer shopId;

    /**
     * 订单所在城市的code
     */
    //@JsonProperty(value="city_code")
    private String cityCode;

    /**
     * 分发状态
     */
  //  @JsonProperty(value="distribute_status")
    private Integer distributeStatus;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   // @JsonProperty(value="create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //@JsonProperty(value="update_time")
    private Date updateTime;

    /**
     * 订单备注
     */
   // @JsonProperty(value="info")
    private String info;

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

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

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public BigDecimal getPredictPrice() {
        return predictPrice;
    }

    public void setPredictPrice(BigDecimal predictPrice) {
        this.predictPrice = predictPrice;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getReceiverTel() {
        return receiverTel;
    }

    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public List<OrderCargoItem> getOrderCargoItems() {
        return orderCargoItems;
    }

    public void setOrderCargoItems(List<OrderCargoItem> orderCargoItems) {
        this.orderCargoItems = orderCargoItems;
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

    public List<OrderDistributionStatusVo> getOrderDistStatusList() {
        return orderDistStatusList;
    }

    public void setOrderDistStatusList(List<OrderDistributionStatusVo> orderDistStatusList) {
        this.orderDistStatusList = orderDistStatusList;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getOriginSource() {
        return originSource;
    }

    public void setOriginSource(Integer originSource) {
        this.originSource = originSource;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public Integer getDistributeStatus() {
        return distributeStatus;
    }

    public void setDistributeStatus(Integer distributeStatus) {
        this.distributeStatus = distributeStatus;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getActiveStatusId() {
        return activeStatusId;
    }

    public void setActiveStatusId(Integer activeStatusId) {
        this.activeStatusId = activeStatusId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(Date expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
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

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }
}