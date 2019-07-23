package com.lbx.distribution.manageserver.entity.merchant;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @ClassName: 商户实体Vo
 * @Description: //
 */
@Component
public class MerchantEntityVo {

    @NotNull(message = "确认密码不能为空")
    private String checkPassword;

    //商户ID
    private Integer merchantId;

    //角色(0.管理员;1:普通商户)
    private Integer role;

    //商户名称
   @NotNull(message = "账户不能为空")
    private String merchantName;

    //商户密码.密码不能小于6位大于16位，目前只校验长度
    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 16)
    private String password;

    //注册商户手机号，用于登陆商户后台. 第1位必须为1，第2位不能是1/2/6/9
    //@NotNull(message = "商户手机号不能为空！")
    //@Size(min = 11,max = 11,message = "请输入正确的手机号！")
   // @Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}",message = "请输入正确的手机号！")
    private String mobile;

    //城市名称
    private String cityName;

    //企业全称
    @NotNull(message = "公司名称不能为空")
    private String enterpriseName;

    //企业地址
    private String enterpriseAddress;

    //营业执照（预留）
    private String license;

    //联系人姓名
    @NotNull(message = "联系人不能为空")
    private String contractName;

    //联系人手机号
    private String contractPhone;

    //邮箱地址
    private String email;

    //商户创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //更新时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    //商户状态(1.active;0:disable)
    private Integer status;

    //备注
    private String remarks;

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getEnterpriseAddress() {
        return enterpriseAddress;
    }

    public void setEnterpriseAddress(String enterpriseAddress) {
        this.enterpriseAddress = enterpriseAddress;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractPhone() {
        return contractPhone;
    }

    public void setContractPhone(String contractPhone) {
        this.contractPhone = contractPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
