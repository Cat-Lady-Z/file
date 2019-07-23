package com.lbx.distribution.manageserver.entity.company;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntityVo;
import com.lbx.distribution.manageserver.entity.channel.ChannelEntity;
import com.lbx.distribution.manageserver.entity.shop.ShopEntityVo;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: 用于显示的公司列表
 * @Description: //
 */
public class CompanyEntityVo implements Serializable {

    //是否是最后一级公司
    private Integer isEnd;

    //公司id
    private Integer companyId;

    //公司名称
    private String companyName;

    //状态（1-激活，0-下线）
    private Integer status;

    //创建日期
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    //已配置的渠道列表
    List<ChannelConfigEntityVo> channelList;

    //已关联门店列表
    List<ShopEntityVo> shopList;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<ChannelConfigEntityVo> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<ChannelConfigEntityVo> channelList) {
        this.channelList = channelList;
    }

    public List<ShopEntityVo> getShopList() {
        return shopList;
    }

    public void setShopList(List<ShopEntityVo> shopList) {
        this.shopList = shopList;
    }

    public Integer getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Integer isEnd) {
        this.isEnd = isEnd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
