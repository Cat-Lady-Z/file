package com.lbx.distribution.manageserver.entity;

import java.util.Date;

public class CmsUrlInfo {
    /**
     * 自增id
     */
    private Long id;

    /**
     * 参数值
     */
    private String url;

    /**
     * 备注
     */
    private String remark;

    /**
     * 开关,0：关闭，1：开启
     */
    private Integer status;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 有权限的角色((-1:公共uri，0.管理员;1:普通商户))
     */
    private Integer role;

    public CmsUrlInfo(Long id, String url, String remark, Integer status, Date updateTime, Date createTime, Integer role) {
        this.id = id;
        this.url = url;
        this.remark = remark;
        this.status = status;
        this.updateTime = updateTime;
        this.createTime = createTime;
        this.role = role;
    }

    public CmsUrlInfo() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}