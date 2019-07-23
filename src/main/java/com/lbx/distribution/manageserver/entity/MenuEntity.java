package com.lbx.distribution.manageserver.entity;

/**
 * 菜单响应
 */
public class MenuEntity {

    //商户/组织机构/门店 id
    private Integer id;

    //商户/组织机构/门店 名称
    private String name;

    //实体type（商户：1， 组织机构：2， 门店：3）
    private Integer type;

    private Integer isEnd;

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
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Integer isEnd) {
        this.isEnd = isEnd;
    }
}