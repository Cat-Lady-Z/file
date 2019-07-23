package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntity;
import com.lbx.distribution.manageserver.entity.channel.OrderDeliveryMarkupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 加价调价
 */
@Mapper
public interface OrderDeliveryMarkupMapper {

    //新增订单配送加价条件
    Integer insertOrderDeliveryMarkup(OrderDeliveryMarkupEntity orderDeliveryMarkup);

    //新增订单配送加价条件
    Integer updateOrderDeliveryMarkup(OrderDeliveryMarkupEntity orderDeliveryMarkup);

    //删除订单配送加价条件
    Integer deleteOrderDeliveryMarkup(Integer id);

    //删除订单配送加价条件
    Integer deleteOrderDeliveryMarkupByMerchantIdOrCompanyId(Map<String,Integer> params);

    //查询公司加价条件
    List<OrderDeliveryMarkupEntity> selectByCompanyId(@Param("companyId") Integer companyId, @Param("channelId") Integer channelId);

    //查询商户加价条件
    List<OrderDeliveryMarkupEntity> selectByMerchantId(@Param("merchantId") Integer merchantId, @Param("channelId") Integer channelId);

    //根据配置渠道信息查询加价条件
    List<OrderDeliveryMarkupEntity> updateOrderDeliveryMarkupByChannelConfig(ChannelConfigEntity channelConfig);
}