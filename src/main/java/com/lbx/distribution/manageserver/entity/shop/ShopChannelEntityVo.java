package com.lbx.distribution.manageserver.entity.shop;

/**
 * @ClassName: 门店渠道实体（）
 * @Description: //
 */
public class ShopChannelEntityVo {
    //门店id
    private Integer shopId;

    //渠道id
    private Integer channelId;

    //渠道name
    private String channelName;

    //渠道对应的门店编码
    private String channelShopid;

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelShopid() {
        return channelShopid;
    }

    public void setChannelShopid(String channelShopid) {
        this.channelShopid = channelShopid;
    }
}
