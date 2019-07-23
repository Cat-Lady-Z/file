package com.lbx.distribution.manageserver.entity;

/**
 * 菜单请求
 */
public class MenuRequest {

    //商户/组织机构/门店 id
    private Integer id;

    //实体type（商户：1， 组织机构：2， 门店：3）
    private Integer type;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}