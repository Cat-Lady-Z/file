package com.lbx.distribution.manageserver.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 省市区
 */
public class Region implements Serializable {

    private final static long serialVersionUID = 1L;

    /**
     * 地区编码（国标）
     */
    @JsonProperty(value="region_code")
    private String regionCode;

    /**
     * 地区名称
     */
    @JsonProperty(value="region_name")
    private String regionName;

    /**
     * 地区等级（1，省 2，市 3，区 4，镇）
     */
    @JsonProperty(value="region_level")
    private String regionLevel;

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionLevel() {
        return regionLevel;
    }

    public void setRegionLevel(String regionLevel) {
        this.regionLevel = regionLevel;
    }
}
