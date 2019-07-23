package com.lbx.distribution.manageserver.mapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;
import java.util.Set;

/**
 * @Interface: 统计订单Mapper
 * @Description:
 */
@Mapper
public interface OrderStatMapper {
    /**
     * 统计总订单
     * @param
     * @return
     */
    Integer statOrderCount(Map<String, Object> map);

    /**
     * 统计今日完成订单
     * @param
     * @return
     */
    Integer statFinishOrderCountByDate(Map<String, Object> map);

    /**
     * 统计今日待处理订单
     * @param
     * @return
     */
    Integer statUnFinishedOrderCountByDate(Map<String, Object> map);

    /**
     * 统计今日待处理订单
     * @param
     * @return
     */
    Set<String> statCancelOrderIdByDate(Map<String, Object> map);

    /**
     * 统计未正式发单的预约单
     * @param requestMap
     * @return
     */
    Integer statUnFinishedExpectedOrderCount(Map<String, Object> requestMap);

    /**
     * 统计未正式发单就取消的预约单
     * @param requestMap
     * @return
     */
    Set<String> statCancelExpectedOrderId(Map<String, Object> requestMap);
}
