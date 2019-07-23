package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.order.OrderPredictPrice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 订单预估价mapper
 */
@Mapper
public interface OrderPredictPriceMapper {

    /**
     * 根据订单id和渠道id查询预估价格
     * @param orderPredictPrice
     * @return
     */
    OrderPredictPrice selectByPrimaryKey(OrderPredictPrice orderPredictPrice);

    /**
     * 查询预估价
     * @param orderIdsParams
     * @return
     */
    List<OrderPredictPrice> queryOrderPredictPrice(Map<String, Object> orderIdsParams);
}