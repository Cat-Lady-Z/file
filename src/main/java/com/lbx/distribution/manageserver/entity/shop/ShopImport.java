package com.lbx.distribution.manageserver.entity.shop;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 门店导入实体
 */
public class ShopImport implements Serializable {

    /**
     * 商户门店Id
     */
    private String originShopId;

    /**
     * 门店id
     */
    private Integer shopId;

    /**
     * 商户id
     */
    private Integer merchantId;

    /**
     * 分公司id
     */
    private Integer companyId;

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 门店地址
     */
    private String shopAddress;

    /**
     * 门店类型
     */
    private String shopType;

    /**
     * 省份编码
     */
    private String provinceId;

    /**
     * 省份编码
     */
    private String provinceName;

    /**
     * 城市编码
     */
    private String cityId;

    /**
     * 城市编码
     */
    private String cityName;

    /**
     * 地区编码
     */
    private String areaId;

    /**
     * 地区名称
     */
    private String areaName;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 联系人电话
     */
    private String contactPhone;

    /**
     * 联系人名称
     */
    private String contactName;

    public String getOriginShopId() {
        return originShopId;
    }

    public void setOriginShopId(String originShopId) {
        this.originShopId = originShopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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
