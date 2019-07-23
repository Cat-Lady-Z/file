package com.lbx.distribution.manageserver.entity.orderExpected;

import java.math.BigDecimal;
import java.util.Date;

public class OrderExpected {
    /**
     * 订单id
     */
    private String orderId;

    /**
     * 商户id
     */
    private Integer merchantId;

    /**
     * 门店id
     */
    private Integer shopId;

    /**
     * 门店的上一级管理公司ID
     */
    private Integer companyId;

    /**
     * 第三方订单ID
     */
    private String originId;

    /**
     * 订单来源
     */
    private Integer originSource;

    /**
     * 订单所在城市的code
     */
    private String cityCode;

    /**
     * 订单所在城市的名字
     */
    private String cityName;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 是否需要垫付 1:是 0:否 (垫付订单金额，非运费)
     */
    private Integer isPrepay;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人地址
     */
    private String receiverAddress;

    /**
     * 收货人地址维度（高德坐标系）
     */
    private BigDecimal receiverLat;

    /**
     * 收货人地址经度（高德坐标系）
     */
    private BigDecimal receiverLng;

    /**
     * 回调URL
     */
    private String callbackUrl;

    /**
     * 收货人手机号（手机号和座机号必填一项）
     */
    private String receiverPhone;

    /**
     * 收货人座机号（手机号和座机号必填一项）
     */
    private String receiverTel;

    /**
     * 订单商品类型：食品小吃-1,饮料-2,鲜花-3,文印票务-8,便利店-9,水果生鲜-13,同城电商-19, 医药-20,蛋糕-21,酒品-24,小商品市场-25,服装-26,汽修零配-27,数码-28,小龙虾-29, 其他-5
     */
    private Integer cargoType;

    /**
     * 订单重量（单位：Kg）
     */
    private BigDecimal cargoWeight;

    /**
     * 订单商品数量
     */
    private Integer cargoNum;

    /**
     * 货物价格，单位为元，精确到小数点后两位（如果小数点后位数多于两位，则四舍五入保留两位小数），范围为0-5000
     */
    private BigDecimal cargoValue;

    /**
     * 货物高度，单位为cm，精确到小数点后两位
     */
    private BigDecimal cargoHeight;

    /**
     * 货物宽度，单位为cm，精确到小数点后两位
     */
    private BigDecimal cargoWidth;

    /**
     * 货物长度，单位为cm，精确到小数点后两位
     */
    private BigDecimal cargoLength;

    /**
     * 货物数量
     */
    private Integer cargoCount;

    /**
     * 货品名称
     */
    private String cargoName;

    /**
     * 货品价格
     */
    private BigDecimal cargoPrice;

    /**
     * 货品单位
     */
    private String cargoUnit;

    /**
     * 货物取货信息，用于骑手到店取货，最长不超过100个字符
     */
    private String cargoPickupInfo;

    /**
     * 货物交付信息，最长不超过100个字符
     */
    private String cargoDeliveryInfo;

    /**
     * 订单备注
     */
    private String info;

    /**
     * 发票抬头
     */
    private String invoiceTitle;

    /**
     * 订单来源标示
     */
    private String originMark;

    /**
     * 订单来源编号
     */
    private String originMarkNo;

    /**
     * 是否使用保价费（0：不使用保价，1：使用保价； 同时，请确保填写了订单金额（cargo_price））
     */
    private Integer isUseInsurance;

    /**
     * 收货码（0：不需要；1：需要。收货码的作用是：骑手必须输入收货码才能完成订单妥投）
     */
    private Integer isFinishCodeNeeded;

    /**
     * 预约发单时间（预约时间unix时间戳(10位),精确到分;整10分钟为间隔，并且需要至少提前20分钟预约。）
     */
    private Integer delayPublishTime;

    /**
     * 是否选择直拿直送（0：不需要；1：需要。选择直拿直送后，同一时间骑士只能配送此订单至完成，同时，也会相应的增加配送费用）
     */
    private Integer isDirectDelivery;

    /**
     * 保留字段
     */
    private Integer status;

    /**
     * 结算情况:0-已付款，1-已退款。
     */
    private Integer statement;

    /**
     * 配送费,单位为元
     */
    private BigDecimal deliveryFee;

    /**
     * 小费,单位为元
     */
    private BigDecimal tips;

    /**
     * 优惠券费用,单位为元
     */
    private BigDecimal couponFee;

    /**
     * 保价费,单位为元
     */
    private BigDecimal insuranceFee;

    /**
     * 实际支付费用,单位为元
     */
    private BigDecimal actualFee;

    /**
     * 配送距离,单位为米
     */
    private BigDecimal distance;

    /**
     * 违约金
     */
    private BigDecimal deductFee;

    /**
     * 坐标类型，0：火星坐标（高德，腾讯地图均采用火星坐标） 1：百度坐标 （默认值为0）
     */
    private Integer coordinateType;

    /**
     * 配送服务代码，详情见合同
     */
    private Integer deliveryServiceCode;

    /**
     * 是否需要发票, 0:不需要, 1:需要
     */
    private Integer isInvoiced;

    /**
     * 订单支付状态 0:未支付 1:已支付
     */
    private Integer orderPaymentStatus;

    /**
     * 订单支付方式 1:在线支付
     */
    private Integer orderPaymentMethod;

    /**
     * 是否需要ele代收 0:否
     */
    private Integer isAgentPayment;

    /**
     * 预约送达时间
     */
    private Date expectedDeliveryTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 预约单发送标识，1:未发送，2：已发送， 5：已取消
     */
    private Integer sendId;

    public OrderExpected() {
        super();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
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

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId == null ? null : originId.trim();
    }

    public Integer getOriginSource() {
        return originSource;
    }

    public void setOriginSource(Integer originSource) {
        this.originSource = originSource;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getIsPrepay() {
        return isPrepay;
    }

    public void setIsPrepay(Integer isPrepay) {
        this.isPrepay = isPrepay;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName == null ? null : receiverName.trim();
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress == null ? null : receiverAddress.trim();
    }

    public BigDecimal getReceiverLat() {
        return receiverLat;
    }

    public void setReceiverLat(BigDecimal receiverLat) {
        this.receiverLat = receiverLat;
    }

    public BigDecimal getReceiverLng() {
        return receiverLng;
    }

    public void setReceiverLng(BigDecimal receiverLng) {
        this.receiverLng = receiverLng;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl == null ? null : callbackUrl.trim();
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone == null ? null : receiverPhone.trim();
    }

    public String getReceiverTel() {
        return receiverTel;
    }

    public void setReceiverTel(String receiverTel) {
        this.receiverTel = receiverTel == null ? null : receiverTel.trim();
    }

    public Integer getCargoType() {
        return cargoType;
    }

    public void setCargoType(Integer cargoType) {
        this.cargoType = cargoType;
    }

    public BigDecimal getCargoWeight() {
        return cargoWeight;
    }

    public void setCargoWeight(BigDecimal cargoWeight) {
        this.cargoWeight = cargoWeight;
    }

    public Integer getCargoNum() {
        return cargoNum;
    }

    public void setCargoNum(Integer cargoNum) {
        this.cargoNum = cargoNum;
    }

    public BigDecimal getCargoValue() {
        return cargoValue;
    }

    public void setCargoValue(BigDecimal cargoValue) {
        this.cargoValue = cargoValue;
    }

    public BigDecimal getCargoHeight() {
        return cargoHeight;
    }

    public void setCargoHeight(BigDecimal cargoHeight) {
        this.cargoHeight = cargoHeight;
    }

    public BigDecimal getCargoWidth() {
        return cargoWidth;
    }

    public void setCargoWidth(BigDecimal cargoWidth) {
        this.cargoWidth = cargoWidth;
    }

    public BigDecimal getCargoLength() {
        return cargoLength;
    }

    public void setCargoLength(BigDecimal cargoLength) {
        this.cargoLength = cargoLength;
    }

    public Integer getCargoCount() {
        return cargoCount;
    }

    public void setCargoCount(Integer cargoCount) {
        this.cargoCount = cargoCount;
    }

    public String getCargoName() {
        return cargoName;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName == null ? null : cargoName.trim();
    }

    public BigDecimal getCargoPrice() {
        return cargoPrice;
    }

    public void setCargoPrice(BigDecimal cargoPrice) {
        this.cargoPrice = cargoPrice;
    }

    public String getCargoUnit() {
        return cargoUnit;
    }

    public void setCargoUnit(String cargoUnit) {
        this.cargoUnit = cargoUnit == null ? null : cargoUnit.trim();
    }

    public String getCargoPickupInfo() {
        return cargoPickupInfo;
    }

    public void setCargoPickupInfo(String cargoPickupInfo) {
        this.cargoPickupInfo = cargoPickupInfo == null ? null : cargoPickupInfo.trim();
    }

    public String getCargoDeliveryInfo() {
        return cargoDeliveryInfo;
    }

    public void setCargoDeliveryInfo(String cargoDeliveryInfo) {
        this.cargoDeliveryInfo = cargoDeliveryInfo == null ? null : cargoDeliveryInfo.trim();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info == null ? null : info.trim();
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle == null ? null : invoiceTitle.trim();
    }

    public String getOriginMark() {
        return originMark;
    }

    public void setOriginMark(String originMark) {
        this.originMark = originMark == null ? null : originMark.trim();
    }

    public String getOriginMarkNo() {
        return originMarkNo;
    }

    public void setOriginMarkNo(String originMarkNo) {
        this.originMarkNo = originMarkNo == null ? null : originMarkNo.trim();
    }

    public Integer getIsUseInsurance() {
        return isUseInsurance;
    }

    public void setIsUseInsurance(Integer isUseInsurance) {
        this.isUseInsurance = isUseInsurance;
    }

    public Integer getIsFinishCodeNeeded() {
        return isFinishCodeNeeded;
    }

    public void setIsFinishCodeNeeded(Integer isFinishCodeNeeded) {
        this.isFinishCodeNeeded = isFinishCodeNeeded;
    }

    public Integer getDelayPublishTime() {
        return delayPublishTime;
    }

    public void setDelayPublishTime(Integer delayPublishTime) {
        this.delayPublishTime = delayPublishTime;
    }

    public Integer getIsDirectDelivery() {
        return isDirectDelivery;
    }

    public void setIsDirectDelivery(Integer isDirectDelivery) {
        this.isDirectDelivery = isDirectDelivery;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatement() {
        return statement;
    }

    public void setStatement(Integer statement) {
        this.statement = statement;
    }

    public BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(BigDecimal deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public BigDecimal getTips() {
        return tips;
    }

    public void setTips(BigDecimal tips) {
        this.tips = tips;
    }

    public BigDecimal getCouponFee() {
        return couponFee;
    }

    public void setCouponFee(BigDecimal couponFee) {
        this.couponFee = couponFee;
    }

    public BigDecimal getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(BigDecimal insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public BigDecimal getActualFee() {
        return actualFee;
    }

    public void setActualFee(BigDecimal actualFee) {
        this.actualFee = actualFee;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public BigDecimal getDeductFee() {
        return deductFee;
    }

    public void setDeductFee(BigDecimal deductFee) {
        this.deductFee = deductFee;
    }

    public Integer getCoordinateType() {
        return coordinateType;
    }

    public void setCoordinateType(Integer coordinateType) {
        this.coordinateType = coordinateType;
    }

    public Integer getDeliveryServiceCode() {
        return deliveryServiceCode;
    }

    public void setDeliveryServiceCode(Integer deliveryServiceCode) {
        this.deliveryServiceCode = deliveryServiceCode;
    }

    public Integer getIsInvoiced() {
        return isInvoiced;
    }

    public void setIsInvoiced(Integer isInvoiced) {
        this.isInvoiced = isInvoiced;
    }

    public Integer getOrderPaymentStatus() {
        return orderPaymentStatus;
    }

    public void setOrderPaymentStatus(Integer orderPaymentStatus) {
        this.orderPaymentStatus = orderPaymentStatus;
    }

    public Integer getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(Integer orderPaymentMethod) {
        this.orderPaymentMethod = orderPaymentMethod;
    }

    public Integer getIsAgentPayment() {
        return isAgentPayment;
    }

    public void setIsAgentPayment(Integer isAgentPayment) {
        this.isAgentPayment = isAgentPayment;
    }

    public Date getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(Date expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
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

    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}