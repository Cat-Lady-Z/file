package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.orderExpected.OrderExpectedDetail;
import com.lbx.distribution.manageserver.entity.orderExpected.OrderExpected;
import com.lbx.distribution.manageserver.entity.orderExpected.OrderExpectedListItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface OrderExpectedMapper {

    OrderExpected selectByPrimaryKey(@Param("orderId") String orderId);

    /**
     * 查询订单列表
     * @param dataParams
     * @return
     */
    List<OrderExpectedListItem> queryExpectedOrders(Map<String, Object> dataParams);

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    OrderExpectedDetail queryExpectedOrderByOrderId(@Param("orderId") String orderId);

    /**
     *  查询取消的预约订单
     * @param params
     * @return
     */
    List<String> queryCancelExpectedOrders(Map<String, Object> params);

    /**
     * 按照时间维度筛选已配送/未正式发单就已取消的订单
     * @param params
     * @return
     */
    Set<String> queryExpectedOrdersByTime(Map<String, Object> params);

    /**
     * 查询所有预约单（根据订单id）
     * @param params
     * @return
     */
    List<OrderExpectedListItem> queryAllTypeExpectedOrders(Map<String, Object> params);
}