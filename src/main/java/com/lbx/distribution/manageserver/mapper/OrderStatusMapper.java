package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.order.OrderStatus;
import com.lbx.distribution.manageserver.entity.order.StatOrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 订单最终状态mapper
 */
@Mapper
public interface OrderStatusMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(OrderStatus record);

    OrderStatus selectByPrimaryKey(String orderId);

    /**
     * 根据订单id查询订单状态
     * @param orderId
     * @return
     */
    OrderStatus queryByOrderId(@Param("orderId") String orderId);

    /**
     * 获取订单统计数据
     * @param
     * @return
     */
    List<StatOrderEntity> queryOrderStat();

    /**
     * 查订单号
     * @param params
     * @return
     */
    Set<String> queryOrderIds(Map<String, Object> params);
}