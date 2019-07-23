package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.merchant.MerchantToken;
import com.lbx.distribution.manageserver.entity.order.OrderEntity;
import com.lbx.distribution.manageserver.entity.order.OrderStatus;
import com.lbx.distribution.manageserver.entity.order.OrderVo;
import com.lbx.distribution.manageserver.helper.CommonHelper;
import com.lbx.distribution.manageserver.mapper.OrderMapper;
import com.lbx.distribution.manageserver.mapper.OrderStatusMapper;
import com.lbx.distribution.manageserver.service.OrderService;
import com.lbx.distribution.manageserver.util.ManageException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@Validated
@Api(value = "订单管理")
@RequestMapping(value="order")
public class OrderController {
    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private CommonHelper commonHelper;

    @PostMapping(value = "orderList")
    @ApiOperation(value = "查询订单列表", notes = "分页查询订单列表")
    public String orderListToMerchant(@RequestBody Map<String, Object> request ){
        return orderService.queryOrderList(request);
    }

    @PostMapping(value = "orderDetail")
    @ApiOperation(value = "查询单个订单详情", notes = "根据订单id以及配送渠道id查询订单详情")
    public String orderDetail(@RequestBody Map<String, Object> request ){
        return orderService.queryOrderByOrderId(request);
    }

    @PostMapping(value = "cancelOrder")
    @ApiOperation(value = "取消订单", notes = "根据订单id，取消订单")
    public String cancelOrder(@RequestBody OrderVo orderVo, HttpServletRequest httpServletRequest){
        //判断当前账号是否在启用状态，若没有启用则不允许取消订单
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起取消订单，orderId: %s, ;",
                    httpServletRequest.getRemoteAddr(),merchantId, user.getMerchantName(), orderVo.getOrderId()));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }

        return orderService.cancelOrderByOrderId(orderVo.getOrderId());
    }
}
