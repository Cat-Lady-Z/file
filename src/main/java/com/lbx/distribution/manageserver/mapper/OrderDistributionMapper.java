package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.order.OrderDistribution;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface OrderDistributionMapper {

    /**
     * 查询订单id
     * @param params
     * @return
     */
    Set<String> queryOrderIds(Map<String, Object> params);

    /**
     * 多条件筛选
     * @param map
     * @return
     */
    OrderDistribution queryOrderDistribution(Map<String, Object> map);

    /**
     *
     * @param orderIdsParams
     * @return
     */
    List<OrderDistribution> queryOrderDistributionList(Map<String, Object> orderIdsParams);
}