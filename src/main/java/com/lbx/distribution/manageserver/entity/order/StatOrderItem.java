package com.lbx.distribution.manageserver.entity.order;

import java.util.List;

/**
 * @ClassName: 首页 - 订单统计 - 图形显示（点元素）
 * @Description: //
 */
public class StatOrderItem {

    //渠道标记
    private String channelName;

    //渠道名称
    private String txt;

    //订单数量数据
    private List<Integer> data;

    //渠道 - 图形线颜色
    private String color;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
