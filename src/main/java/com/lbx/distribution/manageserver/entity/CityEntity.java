package com.lbx.distribution.manageserver.entity;

import org.springframework.stereotype.Component;

@Component
public class CityEntity {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 城市编码
     */
    private String cityid;

    /**
     * 城市名称
     */
    private String city;

    /**
     * 所属省份编码
     */
    private String provinceid;

    /**
     * crm_城市编码
     */
    private String crmCitiesCode;

    public CityEntity(Integer id, String cityid, String city, String provinceid, String crmCitiesCode) {
        this.id = id;
        this.cityid = cityid;
        this.city = city;
        this.provinceid = provinceid;
        this.crmCitiesCode = crmCitiesCode;
    }

    public CityEntity() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid == null ? null : cityid.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid == null ? null : provinceid.trim();
    }

    public String getCrmCitiesCode() {
        return crmCitiesCode;
    }

    public void setCrmCitiesCode(String crmCitiesCode) {
        this.crmCitiesCode = crmCitiesCode == null ? null : crmCitiesCode.trim();
    }
}