package com.lbx.distribution.manageserver.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: 订单导出表格单体
 * @Description: //
 */
public class OrderExcelListItem {

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
     * 渠道配送平台运单ID
     */
    private String channelPeisongId;

    /**
     * 商户id
     */
    private Integer merchantId;

    //企业全称（商户）
    private String enterpriseName;

    /**
     * 公司id
     */
    private Integer companyId;

    //公司名称
    private String companyName;

    /**
     * 门店id
     */
    private Integer shopId;

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

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 配送渠道ID
     */
    private Integer channelId;

    /**
     * 配送渠道名称(需要二次查询，第一次查id，第二次查询填充)
     */
    private String channelName;

    /**
     * 状态（0-默认，待接单,1-配货中;2-已接单, 3-配送
     中，4-已完成;5-已取消）
     */
    private Integer statusCode;

    /**
     * 状态描述
     */
    private String statusMsg;

    /**
     * 预估配送价格
     */
    private BigDecimal predictPrice;

    /**
     * 基础价
     */
    private BigDecimal basePrice;

    /**
     * 距离加价
     */
    private BigDecimal addDistancePrice;

    /**
     * 重量加价
     */
    private BigDecimal addWeightPrice;

    /**
     * 时间加价
     */
    private BigDecimal addTimePrice;

    /**
     * 配送距离,单位为米
     */
    private BigDecimal distance;

    /**
     * 第三方订单来源标识(如o2o商城,美团)
     */
    private Integer originSource;

    /**
     * 订单来源名称
     */
    private String originSourceName;

    //地址信息
    /**
     * 省编码
     */
    private String provinceid;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 订单所在城市的code
     */
    private String cityCode;

    /**
     * 订单所在城市名称
     */
    private String cityName;

    /**
     * 区县编码
     */
    private String areaid;

    /**
     * 区域名称(如,浦东新区)
     */
    private String areaName;

    //门店地址
    private String address;

    //联系人电话
    private String contactPhone;

    /**
     * 配送员姓名（订单已被骑手接单后会返回骑手信息）
     */
    private String courierName;

    /**
     * 配送员电话（订单已被骑手接单后会返回骑手信息）
     */
    private String courierPhone;

    //顾客信息
    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人地址
     */
    private String receiverAddress;

    /**
     * 收货人手机号（手机号和座机号必填一项）
     */
    private String receiverPhone;

    /**
     * 收货人手机号（手机号和座机号必填一项）
     */
    private String receiverTel;

    /**
     * 备注
     */
    private String info;

    //订单明细 - 货物列表
    List<OrderExcelCargoItem> orderCargoItems;

    Map<Integer, OrderDistributionStatusVo> orderDistributionStatusVoMap;

    /**
     * 分发状态(0:未分发;1:分发渠道成功;2:订单已完成;3:订单已取消,-1:失败订单)
     */
    private Integer distributeStatus;

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


    public String getChannelPeisongId() {
        return channelPeisongId;
    }

    public void setChannelPeisongId(String channelPeisongId) {
        this.channelPeisongId = channelPeisongId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
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

    public BigDecimal getPredictPrice() {
        return predictPrice;
    }

    public void setPredictPrice(BigDecimal predictPrice) {
        this.predictPrice = predictPrice;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
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

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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

    public String getReceiverTel() {
        return receiverTel;
    }

    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel;
    }

    public List<OrderExcelCargoItem> getOrderCargoItems() {
        return orderCargoItems;
    }

    public void setOrderCargoItems(List<OrderExcelCargoItem> orderCargoItems) {
        this.orderCargoItems = orderCargoItems;
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

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getAddDistancePrice() {
        return addDistancePrice;
    }

    public void setAddDistancePrice(BigDecimal addDistancePrice) {
        this.addDistancePrice = addDistancePrice;
    }

    public BigDecimal getAddWeightPrice() {
        return addWeightPrice;
    }

    public void setAddWeightPrice(BigDecimal addWeightPrice) {
        this.addWeightPrice = addWeightPrice;
    }

    public BigDecimal getAddTimePrice() {
        return addTimePrice;
    }

    public void setAddTimePrice(BigDecimal addTimePrice) {
        this.addTimePrice = addTimePrice;
    }

    public Map<Integer, OrderDistributionStatusVo> getOrderDistributionStatusVoMap() {
        return orderDistributionStatusVoMap;
    }

    public void setOrderDistributionStatusVoMap(Map<Integer, OrderDistributionStatusVo> orderDistributionStatusVoMap) {
        this.orderDistributionStatusVoMap = orderDistributionStatusVoMap;
    }
}
