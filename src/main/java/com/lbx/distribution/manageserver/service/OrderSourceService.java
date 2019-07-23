package com.lbx.distribution.manageserver.service;

import com.lbx.distribution.manageserver.entity.order.OrderSourceEntity;
import com.lbx.distribution.manageserver.mapper.OrderSourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName:
 * @Description: 商户订单来源Service
 */

@Service
public class OrderSourceService {

    @Autowired
    private OrderSourceMapper orderSourceMapper;

    @Autowired
    private ManageCommonService manageCommonService;

    /**
     * 查询商户订单来源列表
     * @return
     * @Description: 查询商户订单来源列表
     */
    public String queryOrderSourceList(Map<String, Object> request) {

        List<OrderSourceEntity> orderSourceTypeList = orderSourceMapper.queryOrderSourceTypeList(request);

        return manageCommonService.getRespBody(orderSourceTypeList);
    }

    /**
     * 查询所有商户订单来源列表
     * @return
     * @Description: 查询所有商户订单来源列表
     */
    public List<OrderSourceEntity> queryAllOrderSourceTypeList() {
        return orderSourceMapper.queryAllOrderSourceTypeList();
    }
}
