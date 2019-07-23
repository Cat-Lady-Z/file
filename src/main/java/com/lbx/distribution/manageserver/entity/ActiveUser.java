package com.lbx.distribution.manageserver.entity;

/**
 * @ClassName:
 * @Description: //
 */
public class ActiveUser {
    private static final long serialVersionUID = 1L;
    private Integer merchantId;//用户id（主键）
    private String merchantName;// 用户名称
    private Integer role;// 角色()
    private String userStatus;// 用户状态

    public ActiveUser(Integer merchantId, String merchantName, Integer role) {
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.role = role;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
}
