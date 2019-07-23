package com.lbx.distribution.manageserver.entity;

public class BusinessType {
    /**
     * 业务类型编码
     */
    private Integer id;

    /**
     * 业务类型名称
     */
    private String name;

    public BusinessType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public BusinessType() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}