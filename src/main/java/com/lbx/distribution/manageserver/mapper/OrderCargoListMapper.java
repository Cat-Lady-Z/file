package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.order.OrderCargoItem;
import com.lbx.distribution.manageserver.entity.order.OrderExcelCargoItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单货物mapper
 */
@Mapper
public interface OrderCargoListMapper {
    /**
     * 根据订单号获取订单货物列表
     * @param orderId
     * @return
     */
    List<OrderCargoItem> selectByOrderId(@Param("orderId") String orderId);

    /**
     *
     * @param orderId
     * @return
     */
    List<OrderExcelCargoItem> selectOrderExcelCargoItemByOrderId(@Param("orderId")String orderId);
}