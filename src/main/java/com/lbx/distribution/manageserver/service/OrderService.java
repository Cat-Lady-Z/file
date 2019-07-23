package com.lbx.distribution.manageserver.service;

import com.alibaba.fastjson.JSONObject;
import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.common.OrderDistributionStatusEnum;
import com.lbx.distribution.manageserver.common.ParamEnum;
import com.lbx.distribution.manageserver.entity.merchant.MerchantConfig;
import com.lbx.distribution.manageserver.entity.PageResult;
import com.lbx.distribution.manageserver.entity.order.OrderDetail;
import com.lbx.distribution.manageserver.helper.OrderHelper;
import com.lbx.distribution.manageserver.helper.SignHelper;
import com.lbx.distribution.manageserver.mapper.MerchantConfigMapper;
import com.lbx.distribution.manageserver.mapper.OrderMapper;
import com.lbx.distribution.manageserver.mapper.OrderStatusMapper;
import com.lbx.distribution.manageserver.mapper.ParamMapper;
import com.lbx.distribution.manageserver.util.ManageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * @ClassName: 订单service
 * @Description:
 */
@Service
public class OrderService {
    private static Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
   @Autowired
   private MerchantConfigMapper merchantConfigMapper;
    @Autowired
    private OrderCommonService orderCommonService;
    @Autowired
    private ManageCommonService manageCommonService;
    @Autowired
    private ParamMapper paramMapper;
    @Autowired
    OrderHelper orderHelper;

    /**
     * 获取订单列表（商户）
     * @param request
     * @return
     */
    public String queryOrderList(Map<String, Object> request) {
        PageResult pageResult = orderCommonService.getOrderList( request );
        String resp = manageCommonService.getRespBody(pageResult);
        return resp;
    }

    /**
     * 根据订单id，查询订单详情
     * @param request
     * @return
     */
    public String queryOrderByOrderId(Map<String, Object> request ) {
        OrderDetail order = orderCommonService.queryOrderDetailByOrderId(request);

        String resp = manageCommonService.getRespBody(order);

        return resp;
    }

    /**
     * 根据订单id取消订单
     * @param orderId
     * @return
     */
    public String cancelOrderByOrderId(String orderId) {
        //检验订单状态是否可取消
        //参数封装
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);
        OrderDetail orderDetail = orderMapper.queryOrderDetailByOrderId( data );
        if (orderDetail == null) {
            logger.info(String.format("订单号不存在. orderId: %s;",
                    orderId));
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "订单号不存在!");
        }

        Integer distributeStatus = orderDetail.getDistributeStatus();
        if ( OrderDistributionStatusEnum.isAbnormalStatus(distributeStatus) ){
            logger.info(String.format("订单已取消/异常，不能再取消. orderId: %s,distributeStatus:%s;",
                    orderId, distributeStatus));
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "订单已取消/异常，不能再取消!");
        }

        //发起取消订单请求.order_type， 1: 即时单(尽快送达，限当日订单) 2: 预约单
        int orderType = 1;
        String resultJson = orderHelper.cancelOrderByOrderId(orderId, orderDetail.getMerchantId(), orderType);
        return  resultJson;
    }
}
