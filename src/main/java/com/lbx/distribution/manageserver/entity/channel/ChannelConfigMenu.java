package com.lbx.distribution.manageserver.entity.channel;

/**
 * @ClassName: 配置渠道下拉菜单
 * @Description: //
 */

public class ChannelConfigMenu {

    /**
     * 渠道id
     */
    private Integer channelId;

    /**
     * 渠道名称
     */
    private String channelName;

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
}
