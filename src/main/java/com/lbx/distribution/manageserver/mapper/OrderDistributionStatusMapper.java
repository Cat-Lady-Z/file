package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.order.OrderDistributionStatus;
import com.lbx.distribution.manageserver.entity.order.OrderDistributionStatusVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 订单实时配送状态记录mapper
 */
@Mapper
public interface OrderDistributionStatusMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OrderDistributionStatus record);

    int insertSelective(OrderDistributionStatus record);

    OrderDistributionStatus selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderDistributionStatus record);

    int updateByPrimaryKey(OrderDistributionStatus record);

    /**
     * 根据订单id查询订单实时配送状态
     * @param request
     * @return
     */
    OrderDistributionStatus queryDistStatus(Map<String, Object> request);

    /**
     * 根据订单id查询订单实时配送状态列表
     * @param request
     * @return
     */
    List<OrderDistributionStatusVo> queryDistStatusList(Map<String, Object> request);

    /**
     * 查询骑手信息
     * @param params
     * @return
     */
    List<OrderDistributionStatus> queryCourierData(Map<String, Object> params);
}