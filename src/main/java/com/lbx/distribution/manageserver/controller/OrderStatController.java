package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.service.OrderStatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Validated
@Api(value = "订单统计")
@RequestMapping(value="orderStat")
public class OrderStatController {
    @Autowired
    private OrderStatService orderStatService;

    @PostMapping(value = "calcOrder")
    @ApiOperation(value = "统计订单数量", notes = "统计订单数量，订单总数，今日订单情况统计")
    public String calcOrder(@RequestBody Map<String, Object> request) {
        Object merchantIdObj = request.get("merchantId");
        Integer merchantId = (Integer)merchantIdObj;
        String resultStr  = orderStatService.statOrderCount(merchantId);
        return resultStr;
    }

    @PostMapping(value = "calcOrderPeriod")
    @ApiOperation(value = "按时间段统计订单数量", notes = "按时间段统计订单数量（图形显示）")
    public String calcOrderPeriod(@RequestBody Map<String, Object> request) {
        return orderStatService.statOrderCountPeriod( request );
    }
}