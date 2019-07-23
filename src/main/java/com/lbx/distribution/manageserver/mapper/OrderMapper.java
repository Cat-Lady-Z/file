package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.order.OrderDetail;
import com.lbx.distribution.manageserver.entity.order.OrderEntity;
import com.lbx.distribution.manageserver.entity.order.OrderExcelListItem;
import com.lbx.distribution.manageserver.entity.order.OrderListItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface OrderMapper {
    /**
     * 查询订单列表(支持多条件查询、分页查询)
     * @param params
     * @return
     */
    List<OrderListItem> queryOrderList(Map<String, Object> params);

    /**
     * 多条件查询订单详情
     * @param request
     * @return
     */
    OrderDetail queryOrderDetailByOrderId(Map<String, Object> request);

    /**
     * 根据订单id查询订单列表（导出）
     * @param orderIdsParams
     * @return
     */
    List<OrderExcelListItem> queryOrderExcelList(Map<String, Object> orderIdsParams);

    /**
     * 查询订单id
     * @param params
     * @return
     */
    Set<String> queryOrderIds(Map<String, Object> params);
}