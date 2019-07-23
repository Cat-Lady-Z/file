package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.entity.order.OrderSourceEntity;
import com.lbx.distribution.manageserver.service.ManageCommonService;
import com.lbx.distribution.manageserver.service.OrderSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Validated
@Api(value = "订单来源")
@RequestMapping(value="orderSource")
public class OrderSourceController {

    @Autowired
    private OrderSourceService orderSourceService;

    @Autowired
    private ManageCommonService manageCommonService;

    @PostMapping(value = "sourceList")
    @ApiOperation(value = "查询订单来源列表", notes = "查询订单来源列表")
    public String sourceList(@RequestBody Map<String, Object> request) {
        return orderSourceService.queryOrderSourceList(request);
    }

    @PostMapping(value = "allSourceList")
    @ApiOperation(value = "查询所有订单来源列表", notes = "查询公司列表，组织机构显示用(支持分页查询)")
    public String allSourceList(@RequestBody Map<String, Object> request) {
//        Object merchantIdObj = manageSimpleRequest.get("merchantId");
//
//        Integer merchantId = (Integer)merchantIdObj;

//        System.out.println(String.format("%d", merchantId));
//
        List<OrderSourceEntity> list  = orderSourceService.queryAllOrderSourceTypeList();

        return manageCommonService.getRespBody(list);
    }
}