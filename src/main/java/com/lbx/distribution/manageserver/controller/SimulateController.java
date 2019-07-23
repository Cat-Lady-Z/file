package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.common.OrderDistributionStatusEnum;
import com.lbx.distribution.manageserver.entity.order.OrderDetail;
import com.lbx.distribution.manageserver.mapper.OrderMapper;
import com.lbx.distribution.manageserver.util.DateUtil;
import com.lbx.distribution.manageserver.util.ManageException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.lbx.distribution.manageserver.simulate.TestHttpClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Validated
@Api(value = "模拟")
@RequestMapping(value="simulate")
public class SimulateController {

    @Autowired
    OrderMapper orderMapper;

    @PostMapping(value = "testCall")
    @ApiOperation(value = "模拟更改订单状态", notes = "模拟更改订单状态")
    public String testCall(@RequestBody Map<String, String> request) {
        String orderId = request.get("order_id");
        String channelIdStr = request.get("channel_id");
        Integer channelID = Integer.valueOf(channelIdStr).intValue();

        String statusCodeStr = request.get("status_code");
        Integer statusCode =Integer.valueOf(statusCodeStr).intValue();

//        Integer channelID = 3;   // 美团：1， 蜂鸟：2  达达：3
//        Integer statusCode = 2;  // 订单状态：（待取货：2， 配送中：3， 已完成：4）

        Map<String, Object> callMap =new HashMap<String, Object>();
        callMap.put("order_id", orderId);
        callMap.put("channel_id", channelID);
        callMap.put("status_code", statusCode);

        String result = TestHttpClient.callBackOrder("http://47.107.255.9/lbx/develop/test_call_back", callMap);

        System.out.println(result);

        return result;
    }

    @PostMapping(value = "cancelOrder")
    @ApiOperation(value = "模拟取消订单", notes = "模拟取消订单")
    public String cancelOrder(@RequestBody Map<String, String> request) {
        String orderId = request.get("order_id");
        //判断订单是否已取消/异常/已完成
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("orderId", orderId);
        OrderDetail orderDetail = orderMapper.queryOrderDetailByOrderId( queryMap );
        if (orderDetail == null) {
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "订单号不存在!");
        }
        Integer distributeStatus = orderDetail.getDistributeStatus();
        if ( distributeStatus == 4 ){
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "订单已完成，不能再取消!");
        }
        if ( distributeStatus == 5 ){
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "订单已取消，请勿重复操作!");
        }

        String cancelSourceStr = request.get("cancel_source");
        Integer cancelSource = Integer.valueOf(cancelSourceStr).intValue();
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("order_id", orderId);
        dataMap.put("cancel_source", cancelSource);


        Map<String, Object> newResultMap = new HashMap<String, Object>();

        newResultMap.put("data", dataMap);
        newResultMap.put("timestamp", "1559613060");
        newResultMap.put("app_id", "b704920c-8fa3-418a-8da1-458de383532a");
        newResultMap.put("source_id", "73753");
        newResultMap.put("app_secret", "19209b2e48a088f9c7b8d17c4e5c86d3");

        String result = TestHttpClient.cancelOrder("http://47.107.255.9/lbx/order/cancelOrder", newResultMap);

        System.out.println(result);

        return result;
    }

    @PostMapping(value = "createOrder")
    @ApiOperation(value = "模拟生成订单", notes = "模拟生成订单")
    public String createOrder(@RequestBody Map<String, String> request) {
        // 经常修改的参数
        String orderId = request.get("order_id");
        String origin_sourceStr = request.get("origin_source");
        String city_code = request.get("city_code");
        String shopIdStr = request.get("shop_id");
        Integer shopId = Integer.valueOf(shopIdStr).intValue();
        Integer origin_source = Integer.valueOf(origin_sourceStr).intValue();
        String lng = request.get("lng");
        String lat = request.get("lat");
        //预约送达时间
        String expected_delivery_time = request.get("expected_delivery_time");
        //订单类型，1: 即时单(尽快送达，限当日订单) 2: 预约单
        String order_typeStr = request.get("order_type");
        Integer order_type = Integer.valueOf(order_typeStr).intValue();

        String delivery_id = orderId;
        int shopID = shopId;
        String longStr  = lng;
        String latStr = lat;

        Map<String, Object> transportMap =new HashMap<String, Object>();
        transportMap.put("transport_remark", "remark");

        Map<String, Object> itemsMap =new HashMap<String, Object>();
        itemsMap.put("item_name", "水杨酸苯酚贴膏");
        itemsMap.put("item_quantity", "1");
        itemsMap.put("item_price", "0.01");
        itemsMap.put("item_actual_price", "0.01");
        itemsMap.put("is_need_package", "0");
        itemsMap.put("is_agent_purchase", "0");

        List list = new ArrayList();
        list.add(itemsMap);

        Map<String, Object> dataMap =new HashMap<String, Object>();
        dataMap.put("expected_delivery_time", expected_delivery_time);
        dataMap.put("order_type", order_type);
        dataMap.put("order_actual_amount", "0.01");
        dataMap.put("origin_id", orderId);
        dataMap.put("cargo_price", "0.01");
        dataMap.put("city_code", city_code);
        dataMap.put("receiver_name", "测试一");
        dataMap.put("receiver_phone", "18948312082");
        dataMap.put("receiver_address", "深圳市明澜公路养护工程有限公司观澜公路工区观澜");
        dataMap.put("receiver_lng", longStr);
        dataMap.put("receiver_lat", latStr);
        dataMap.put("shop_no", shopID);
        dataMap.put("transport_info", transportMap);
        dataMap.put("items_json", list);
        dataMap.put("cargo_num", "1");
        dataMap.put("goods_count", "1");
        dataMap.put("goods_value", "100");
        dataMap.put("origin_source", origin_source);

        dataMap.put("is_prepay", "0");
        dataMap.put("info", "info");
        dataMap.put("cargo_weight", "1");
        dataMap.put("coordinate_type", "0");
        dataMap.put("is_invoiced", "0");
        dataMap.put("order_payment_status", "1");
        dataMap.put("order_payment_method", "1");
        dataMap.put("is_agent_payment", "0");
        dataMap.put("delivery_id", delivery_id);
        dataMap.put("callback_url", "http://47.107.255.9:9994/v1/order/notice-logistics");
        dataMap.put("delivery_service_code", "4002");

        Map<String, Object> newResultMap=new HashMap<String, Object>();

        newResultMap.put("data", dataMap);
        newResultMap.put("timestamp", "1559613060");
        newResultMap.put("app_id", "b704920c-8fa3-418a-8da1-458de383532a");
        newResultMap.put("source_id", "73753");
        newResultMap.put("app_secret", "19209b2e48a088f9c7b8d17c4e5c86d3");

        String result = TestHttpClient.makeOrder("http://47.107.255.9/lbx/order/distributeOrder", newResultMap);

        System.out.println(result);

        return result;
    }
}
