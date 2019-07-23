package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.merchant.MerchantToken;
import com.lbx.distribution.manageserver.entity.order.OrderVo;
import com.lbx.distribution.manageserver.entity.orderExpected.OrderExpected;
import com.lbx.distribution.manageserver.helper.CommonHelper;
import com.lbx.distribution.manageserver.service.ExpectedOrderService;
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

/**
 * @ClassName: 预约单
 * @Description:
 */
@RestController
@Validated
@Api(value = "预约单管理")
@RequestMapping(value="expectedOrder")
public class ExpectedOrderController {
    private static Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private ExpectedOrderService expectedOrderService;
    @Autowired
    private CommonHelper commonHelper;

    @PostMapping(value = "expectedOrderList")
    @ApiOperation(value = "查询预约订单列表", notes = "分页查询预约订单列表")
    public String expectedOrderList(@RequestBody Map<String, Object> request ){
        return expectedOrderService.expectedOrderList(request);
    }

    @PostMapping(value = "expectedOrderDetail")
    @ApiOperation(value = "查询单个预约订单详情", notes = "根据订单id查询订单详情")
    public String expectedOrderDetail(@RequestBody Map<String, String> request ){
        return expectedOrderService.queryExpectedOrderDetail(request);
    }

    @PostMapping(value = "cancelExpectedOrder")
    @ApiOperation(value = "取消预约单", notes = "根据订单id，取消预约单")
    public String cancelExpectedOrder(@RequestBody OrderExpected orderExpected, HttpServletRequest httpServletRequest){
        //判断当前账号是否在启用状态，若没有启用则不允许取消订单
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起取消预约订单，orderId: %s, ;",
                    httpServletRequest.getRemoteAddr(),merchantId, user.getMerchantName(), orderExpected.getOrderId()));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }

        return expectedOrderService.cancelExpectedOrderByOrderId(orderExpected.getOrderId());
    }
}
