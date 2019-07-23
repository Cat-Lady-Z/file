package com.lbx.distribution.manageserver.entity.shop;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntityVo;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 门店实体
 */
public class ShopEntityVo implements Serializable {

    /**
     * 商户门店Id
     */
    private String originShopId;

    /**
     * 区县编码
     */
    private String areaid;

    /**
     * 省编码
     */
    private String provinceid;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 门店类型
     */
    private String shopType;


    //门店ID
    private Integer shopId;

    //商户ID
    private Integer merchantId;

    //管理公司ID.（可为空，针对商户没有公司只有一个门店的情况）
    @NotNull(message = "公司ID不能为空！")
    private Integer companyId;

    //门店名称
    @NotNull(message = "门店名称不能为空！")
    @Size(max = 20)
    private String shopName;

    //?? 是否有改动？
    //业务类型(食品小吃-1,饮料-2,鲜花-3,文印票务-8,便利店-9,水果生鲜-13,
    //同城电商-19, 医药-20,蛋糕-21,酒品-24,小商品市场-25,服装-26,汽修零
    //配-27,数码-28,小龙虾-29, 其他-5)
    @NotNull(message = "业务类型不能为空！")
    private String businessType;

    //业务类型名称
    private String businessTypeName;

    //城市名称(如,上海)
    private String cityName;

    //城市id
    private String cityCode;


    //区域名称(如,浦东新区)
    private String areaName;

    //门店地址
    @NotNull(message = "门店地址不能为空！")
    private String address;

    //门店经度
    @NotNull(message = "经度不能为空！")
    private Double lng;

    //门店纬度
    @NotNull(message = "纬度不能为空！")
    private Double lat;

    //0: 高德地图坐标；
    private Integer locationType;

    //联系人姓名
    @NotNull(message = "联系人姓名不能为空！")
    //@Size(max = 4)
    private String contactName;

    //联系人电话
    @NotNull(message = "联系人电话不能为空！")
    @Size(min = 11,max = 11,message = "请输入正确的手机号！")
    @Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}",message = "请输入正确的手机号！")
    private String contactPhone;

    //配置渠道列表(往上查询上层所有的配置渠道 [门店自定义渠道 + 上层公司渠道] )
    List<ChannelConfigEntityVo> channelConfigList;

    //第三方渠道门店信息
    @NotNull(message = "第三方门店编码不能为空！")
    List<ShopChannelEntityVo> shopChannels;

    //联系人身份证
    //@NotNull(message = "联系人身份证不能为空！")
    private String idCard;

    //门店创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //更新时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    //门店状态（1-门店激活，0-门店下线）
    private Integer status;

    //备注
    private String remarks;

    public String getOriginShopId() {
        return originShopId;
    }

    public void setOriginShopId(String originShopId) {
        this.originShopId = originShopId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
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

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
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

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Integer getLocationType() {
        return locationType;
    }

    public void setLocationType(Integer locationType) {
        this.locationType = locationType;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public List<ChannelConfigEntityVo> getChannelConfigList() {
        return channelConfigList;
    }

    public void setChannelConfigList(List<ChannelConfigEntityVo> channelConfigList) {
        this.channelConfigList = channelConfigList;
    }

    public List<ShopChannelEntityVo> getShopChannels() {
        return shopChannels;
    }

    public void setShopChannels(List<ShopChannelEntityVo> shopChannels) {
        this.shopChannels = shopChannels;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }
}
