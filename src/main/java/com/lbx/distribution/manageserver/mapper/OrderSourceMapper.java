package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.order.OrderSourceEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Interface: 商户订单来源Mapper
 * @Description:
 */
@Mapper
public interface OrderSourceMapper {
    /**
     * 查询商户订单来源列表
     * @param
     * @return
     */
    List<OrderSourceEntity> queryOrderSourceTypeList(Map<String, Object> params);


    /**
     * 查询所有商户订单来源列表
     * @param
     * @return
     */
    List<OrderSourceEntity> queryAllOrderSourceTypeList();



}
