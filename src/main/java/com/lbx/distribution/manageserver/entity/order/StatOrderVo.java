package com.lbx.distribution.manageserver.entity.order;

import java.util.List;

/**
 * @ClassName: 订单统计返回实体 -- 图形
 * @Description:
 */
public class StatOrderVo {

    //时间单位
    private List<String> time;

    //总统计列表
    private List<StatOrderItem> dataList;

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<StatOrderItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<StatOrderItem> dataList) {
        this.dataList = dataList;
    }
}
