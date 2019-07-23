package com.lbx.distribution.manageserver.helper;

import com.alibaba.fastjson.JSONObject;
import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.common.OrderDistributionStatusEnum;
import com.lbx.distribution.manageserver.common.OrderStatusEnum;
import com.lbx.distribution.manageserver.common.ParamEnum;
import com.lbx.distribution.manageserver.entity.merchant.MerchantConfig;
import com.lbx.distribution.manageserver.entity.order.OrderDetail;
import com.lbx.distribution.manageserver.entity.order.OrderDistribution;
import com.lbx.distribution.manageserver.entity.order.OrderDistributionStatusVo;
import com.lbx.distribution.manageserver.entity.order.OrderListItem;
import com.lbx.distribution.manageserver.mapper.*;
import com.lbx.distribution.manageserver.service.CompanyCommonService;
import com.lbx.distribution.manageserver.util.DateUtil;
import com.lbx.distribution.manageserver.util.HttpClient;
import com.lbx.distribution.manageserver.util.ManageException;
import com.lbx.distribution.manageserver.util.RandomUtils;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName:
 * @Description: 处理跟订单状态相关的请求
 */
@Component
public class OrderHelper {
    private static Logger logger = LoggerFactory.getLogger(OrderHelper.class);

    private final String APP_ID = "app_id";
    private final String SECRET_KEY = "secret_key";
    private final String FORMAT_KEY = "format";
    private final String TIMESTAMP_KEY = "timestamp";
    private final String SIGNATURE_KEY = "signature";
    private final String PARAM_DATA = "data";
    private final String SOURCE_ID = "source_id";
    private final String CANCEL_SOURCE = "3";

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDistributionMapper orderDistributionMapper;
    @Autowired
    private OrderDistributionStatusMapper orderDistStatusMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private ChannelMapper channelMapper;
    @Autowired
    private CompanyCommonService companyCommonService;
    @Autowired
    private OrderExpectedMapper orderExpectedMapper;
    @Autowired
    private ParamMapper paramMapper;
    @Autowired
    private MerchantConfigMapper merchantConfigMapper;

    /**
     * 获取满足条件的订单id，并封装到map里面
     * @param request
     * @return
     */
    public Map<String, Object> getOrderIdsParams(Map<String, Object> request) {
        Map<String, Object> orderIdsParams = new HashMap<>();

        //1、设置唯一条件标志。如果选择了商家订单号/平台订单号/运单号查询，其他的条件都不管（模糊查询，除了statusCode）
        Boolean onlyFlag = false;
        Boolean originIdFlag = false;
        Boolean distributionOrderIdFlag = false;
        Boolean channelPeisongIdFlag = false;
        Object originId = request.get("originId");
        Object distributionOrderId = request.get("distributionOrderId");
        Object channelPeisongId = request.get("channelPeisongId");
        if ( originId != null && !"".equals(String.valueOf(originId).replaceAll(" ", "")) ) {
            //查：tb_order、tb_order_status、tb_order_distribution
            onlyFlag = true;
            originIdFlag = true;
            String originIdString = String.valueOf(originId).replaceAll(" ", "");
            request.put("originId" , originIdString);
        } else if (distributionOrderId != null && !"".equals( String.valueOf(distributionOrderId).replaceAll(" ", "") )){
            //查：tb_order、tb_order_status、tb_order_distribution
            onlyFlag = true;
            distributionOrderIdFlag = true;
            String distributionOrderIdString = String.valueOf(distributionOrderId).replaceAll(" ", "");
            request.put("distributionOrderId" , distributionOrderIdString);
        } else if(channelPeisongId != null && !"".equals( String.valueOf(channelPeisongId).replaceAll(" ", "") )) {
            onlyFlag = true;
            channelPeisongIdFlag = true;
            String channelPeisongIdString = String.valueOf(channelPeisongId).replaceAll(" ", "");
            request.put("channelPeisongId" , channelPeisongIdString);
        }

        //2、查订单id
        //1) 查：tb_order【主表】(条件：originId、distributionOrderId、merchantId、companyId、companyIds、shopId、cityCode、sourceType、sourceMerchantId)
        Set<String> orderIdsSet_tbOrder = this.getOrderIdsFromTbOrder(request, originIdFlag, distributionOrderIdFlag);
        //2) 查：tb_order_status - 条件：channelId, startTime、endTime - tb_order_expected + tb_order_status
        Set<String> orderIdsSet_tbOrderStatus = null;
        if (!onlyFlag) {
            orderIdsSet_tbOrderStatus = this.getOrderIdsFromTbOrderStatus(request);
        }
        //3)查：tb_order_distribution：channelPeisongId
        Set<String> orderIdsSet_tbOrderDistribution = null;
        if (channelPeisongIdFlag) {
            orderIdsSet_tbOrderDistribution = this.getOrderIdsFromTbOrderDistribution(request, channelPeisongIdFlag);
        }

        Set<String> orderIdsSet_temp = new HashSet<>();
        if (orderIdsSet_tbOrderDistribution!=null) {
            orderIdsSet_temp.addAll(orderIdsSet_tbOrderDistribution);
        } else {
            if (orderIdsSet_tbOrder != null) {
                if (orderIdsSet_tbOrderStatus!=null) {
                    for (String orderId:orderIdsSet_tbOrder) {
                        if (orderIdsSet_tbOrderStatus.contains(orderId) ) {
                            orderIdsSet_temp.add(orderId);
                        }
                    }
                } else {
                    orderIdsSet_temp.addAll(orderIdsSet_tbOrder);
                }
            }
        }

        //3) 筛选订单状态
        //tb_order_status - statusCode: 待接单＝1(0-未分发)，已完成＝4，已取消＝5，系统异常=1000(-1, 6); 待取货＝2(2,100),配送中＝3 + tb_order_expected: send_id=5 的订单
        Object statusObject = request.get("status");
        Set<String> orderIdsSet_status = null;
        if (statusObject != null) {
            orderIdsSet_status = this.getOrderIdsByStatus(request);
        }

        //3、汇总
        List<String> orderIds = new ArrayList<>();
        if (orderIdsSet_status != null) {
            for (String orderId:orderIdsSet_temp ) {
                if (orderIdsSet_status.contains(orderId)) {
                    orderIds.add(orderId);
                }
            }
        } else {
            orderIds.addAll(orderIdsSet_temp);
        }

        //封装参数
        if (orderIds != null && orderIds.size() > 0) {
            if (orderIds.size() > 1) {
                orderIdsParams.put("orderIds", orderIds);
            } else {
                //如果满足条件的只有一个订单号，只查这一个订单（精准查）
                orderIdsParams.put("orderId", orderIds.get(0));
            }
            logger.info(String.format(">>>>>>>>>>>>>>>>>>>>>订单ids：%s>>>>>>>>>>>>>>>>>>>>>>>>>>>>",orderIds.size()));
        }

        return orderIdsParams;
    }

    /**
     *
     */
    private Set<String> getOrderIdsByStatus(Map<String, Object> request) {
        Map<String, Object> params = new HashMap<>();
        Set<String> orderIdsSet_status = new HashSet<>();
        Set<String> orderIdsSet_tbOrderStatus = null;
        Set<String> orderIdsSet_tbOrderDistribution = null;

        Object statusObject = request.get("status");
        //待接单＝1(0-未分发)，已完成＝4，已取消＝5，系统异常=1000(-1, 6)
        Integer statusCode = (Integer) statusObject;
        if (OrderStatusEnum.isInTbOrderStatus(statusCode)) {
            if (statusCode==1) {
                statusCode = 0;
            }
            params.put("status", statusCode);
            orderIdsSet_tbOrderStatus = orderStatusMapper.queryOrderIds(params);

            //处理未正式发单就取消的预约单
            List<String> orders_tbOrderExcepted = null;
            if (statusCode == 5) {
                orders_tbOrderExcepted = orderExpectedMapper.queryCancelExpectedOrders(params);
            }
            if (orders_tbOrderExcepted != null) {
                orderIdsSet_tbOrderStatus.addAll(orders_tbOrderExcepted);
            }
        } else {
            //查：tb_order_distribution (条件：筛选status条件（待取货＝2(2,100),配送中＝3）)
            orderIdsSet_tbOrderDistribution = this.getOrderIdsFromTbOrderDistribution(request, false);
        }
        if (orderIdsSet_tbOrderStatus != null) {
            orderIdsSet_status.addAll(orderIdsSet_tbOrderStatus);
        }
        if (orderIdsSet_tbOrderDistribution != null) {
            orderIdsSet_status.addAll(orderIdsSet_tbOrderDistribution);
        }
        return orderIdsSet_status;
    }

    /**
     * 查tb_order_distribution：channelPeisongId, 筛选status条件（待取货＝2(2,100),配送中＝3）
     */
    private Set<String> getOrderIdsFromTbOrderDistribution(Map<String, Object> request, Boolean channelPeisongIdFlag) {
        Map<String, Object> params = new HashMap<>();
        if (channelPeisongIdFlag) {
            Object channelPeisongId = request.get("channelPeisongId");
            if (channelPeisongId == null) {
                return null;
            }
            params.put("channelPeisongId", channelPeisongId);
        }
        Object statusObject = request.get("status");
        if (statusObject != null && !channelPeisongIdFlag) {
            //待接单＝1(0-未分发)，已完成＝4，已取消＝5，系统异常=1000(-1, 6)
            Integer statusCode = (Integer) statusObject;
            if (!OrderStatusEnum.isInTbOrderStatus(statusCode)) {
                params.put("status", statusCode);
            }
        }

        Set<String> orderIdsSet_tbOrderDistribution = orderDistributionMapper.queryOrderIds(params);
        return orderIdsSet_tbOrderDistribution;
    }

    /**
     * 查tb_order_status：channelId、startTime、endTime,statusCode: 待接单＝1(0-未分发)，已完成＝4，已取消＝5，系统异常=1000(-1, 6)
     */
    private Set<String> getOrderIdsFromTbOrderStatus(Map<String, Object> request) {
        Set<String> orderIdsSet_total = null;
        Boolean channelIdFlag = false;
        Boolean timeFlag = false;
        Object channelIdObject = request.get("channelId");
        Object startTimeObject = request.get("startTime");
        Object endTimeObject = request.get("endTime");
        if (channelIdObject == null && startTimeObject == null && endTimeObject==null) {
            return null;
        }

        //搜索选择全部渠道时，前台传入channelId == 0
        Map<String, Object> channelParams = new HashMap<>();
        Set<String> orderIdsSet_tbOrderStatus_channel = null;
        if (channelIdObject!=null) {
            String channelIdString = channelIdObject+"";
            Integer channelId = Integer.valueOf(channelIdString);
            if (channelId == 0) {
                channelParams.put("channelId", null);
            } else {
                channelParams.put("channelId", channelId);
            }
            channelIdFlag = true;
            orderIdsSet_tbOrderStatus_channel = orderStatusMapper.queryOrderIds(channelParams);
        }

        Map<String, Object> timeParams = new HashMap<>();
        Set<String> orderIdsSet_tbOrderExpected_time = null;
        Set<String> orderIdsSet_tbOrderStatus_time = null;
        if (startTimeObject != null && !"".equals(endTimeObject) && endTimeObject != null && !"".equals(endTimeObject)) {
            timeFlag =true;
            Long startTimeLong = new Long( String.valueOf( startTimeObject ) );
            Long endTimeLong = new Long( String.valueOf( endTimeObject ) );
            timeParams.put("startTime", DateUtil.fromUnixTime(startTimeLong));
            timeParams.put("endTime", DateUtil.fromUnixTime(endTimeLong));
            //查预约单：按照时间维度筛选已配送/未正式发单就已取消的订单
            orderIdsSet_tbOrderExpected_time = orderExpectedMapper.queryExpectedOrdersByTime(timeParams);
            //查即时单
            orderIdsSet_tbOrderStatus_time = orderStatusMapper.queryOrderIds(timeParams);
        }

        if (!channelIdFlag) {
            orderIdsSet_total = new HashSet<>();
            orderIdsSet_total.addAll(orderIdsSet_tbOrderExpected_time);
            orderIdsSet_total.addAll(orderIdsSet_tbOrderStatus_time);
        }
        if (!timeFlag && channelIdFlag) {
            orderIdsSet_total = new HashSet<>();
            orderIdsSet_total.addAll(orderIdsSet_tbOrderStatus_channel);
        }
        if (timeFlag && channelIdFlag) {
            orderIdsSet_total = new HashSet<>();
            for (String orderId:orderIdsSet_tbOrderStatus_channel ) {
                if (orderIdsSet_tbOrderExpected_time.contains(orderId) || orderIdsSet_tbOrderStatus_time.contains(orderId)) {
                    orderIdsSet_total.add(orderId);
                }
            }
        }

        return orderIdsSet_total;
    }

    /**
     * 查tb_order: originId、distributionOrderId、merchantId、companyId、companyIds、shopId、cityCode、sourceType、sourceMerchantId
     */
    private Set<String> getOrderIdsFromTbOrder(Map<String, Object> request, Boolean originIdFlag, Boolean distributionOrderIdFlag) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderType", request.get("orderType"));
        params.put("distributionOrderId", request.get("distributionOrderId"));

        //封装参数
        if (!originIdFlag && !distributionOrderIdFlag) {
            params.put("merchantId", request.get("merchantId"));
            params.put("shopId", request.get("shopId"));
            params.put("cityCode", request.get("cityCode"));
            params.put("sourceType", request.get("sourceType"));
            params.put("sourceMerchantId", request.get("sourceMerchantId"));

            // 判断多条件查询的情况
            Object companyId = request.get("companyId");
            params.put("companyId", companyId);
            if (companyId != null) {
                Integer id = (Integer) companyId;

                //获取所有的下层公司的id
                List<Integer> companyIds = null;
                if (id >=0 ) {
                    companyIds = companyCommonService.querySuperCompanyIdList(id);

                    if (companyIds.size() > 1) {
                        params.put("companyIds", companyIds);
                        params.remove("companyId");
                    }
                }
            }
        } else{
            params.put("originId", request.get("originId"));
            params.put("distributionOrderId", request.get("distributionOrderId"));
        }

        Set<String> orderIdSet = orderMapper.queryOrderIds(params);

        return orderIdSet;
    }

    /**
     * 根据订单列表中已获取的渠道id，填充渠道名称
     */
    private List<OrderListItem> setDistributionData(List<OrderListItem> orderList) {
        if (orderList == null) {
            return null;
        }

        for (OrderListItem item:orderList  ) {
            Integer channelId = item.getChannelId();
            String orderId = item.getOrderId();
            Integer distributeStatus = item.getDistributeStatus();

            Map<String, Object> result =  this.getDistributeData(channelId, orderId, distributeStatus);
            Object channelPeisongIdObject = result.get("channelPeisongId");
            Object channelNameObject = result.get("channelName");
            Object statusCodeObject = result.get("statusCode");
            if (statusCodeObject != null) {
                //订单配送状态
                Integer statusCode = (Integer) statusCodeObject;
                item.setStatusCode(statusCode);
                //转换为显示用的订单状态
                item.setStatusMsg( OrderStatusEnum.getStatusMsg(statusCode) );
            }
            if (channelPeisongIdObject != null) {
                String channelPeisongId = (String) channelPeisongIdObject;
                //运单号
                item.setChannelPeisongId(channelPeisongId);
            } else {
                item.setChannelPeisongId("-");
            }
            if (channelNameObject != null) {
                String channelName = (String) channelNameObject;
                //配送渠道
                item.setChannelName(channelName);
            } else {
                item.setChannelName("-");
            }
        }

        return orderList;
    }

    /**
     * 获取订单配送信息
     */
    private Map<String, Object> getDistributeData(Integer channelId, String orderId, Integer distributeStatus) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("orderId", orderId);

        Integer statusCode = null;

        if ( distributeStatus > 0 && channelId != 0 ) {
            //订单最终配送渠道的配送状态(根据订单id和渠道id确认)
            OrderDistribution od = orderDistributionMapper.queryOrderDistribution( params );

            if(od == null) {
                /*throw new ManageException(ManageResultCode.REQUEST_FAIL, "后台数据错误!");*/
                return null;
            }

            statusCode = od.getStatusCode();

            //渠道运单号
            String channelPeisongId = od.getChannelPeisongId();
            result.put("channelPeisongId", channelPeisongId);

            //配送渠道名称
            String channelName= channelMapper.queryChannelNameByChannelId(channelId);
            result.put("channelName", channelName);

            if ( statusCode == 100 ) {
                //100：骑手进店，处理为待取货
                statusCode = 2;
            }
        } else if (distributeStatus == 0){
            //再分发成功之前都显示待接单状态(已下单=0 待接单＝1 待取货＝2 配送中＝3 已完成＝4 已取消＝5 已过期＝7 指派单=8 退回中=9 退回完成=10 系统异常=1000 ; ）
            statusCode = 1;
        } else {
            //在分发成功之前：取消的订单，系统异常订单，失败订单（-1）等
            statusCode = distributeStatus;
        }
        result.put("statusCode", statusCode);

        return result;
    }

    /**
     * 生成唯一的订单配送状态id
     * @param ids
     * @return
     */
    public Long getId(List<Long> ids) {
        Long result = 0L;
        int id = RandomUtils.getInstance().generateValue(1000, 1000000000);

        if (ids == null) {
            return Integer.toUnsignedLong(id);
        }

        if (!ids.contains(id)) {
            result = Integer.toUnsignedLong(id);
        } else {
            int id2 = RandomUtils.getInstance().generateValue(1000, 1000000000);
            result = Integer.toUnsignedLong(id2);
        }

        return result;
    }

    /**
     *  对正常流程和异常流程返回列表的区分
     * @param tempList
     * @param orderDetail
     * @return
     */
    public List<OrderDistributionStatusVo> getResultList(List<OrderDistributionStatusVo> tempList, OrderDetail orderDetail) {
        List<OrderDistributionStatusVo> resultList = new ArrayList<>();

        List<Long> ids = new ArrayList<>();
        Map<Integer, Integer> statusList = new HashMap<>(); //记录状态码和状态的index
        Integer activeStatusId = 0;
        Integer activeStatus = 0;
        //判断订单是否在正常流程（门店发单-> 骑手接单 -> 骑手取货 -> 骑手送达 ）
        boolean flag = true;  //true：正常状态； flag：非正常状态
        if (tempList != null){
            for (int i = 0; i < tempList.size(); i++) {
                OrderDistributionStatusVo odv = tempList.get(i);
                //记录订单配送状态id
                ids.add(odv.getId());

                Integer statusCode = odv.getStatusCode();
                statusList.put(statusCode, i);
                Boolean abnormalStatus = OrderDistributionStatusEnum.isAbnormalStatus(statusCode);
                if ( abnormalStatus ) {
                    flag = false;
                    break;
                }
            }
        }

        if ( flag && tempList.size() != 4 ) {
            //resultList.addAll(tempList);

            //在正常流程就返回固定的4个元素
            // 0. 门店发单-> 1. 骑手接单 -> 2.骑手取货 -> 3.骑手送达
            for (int i = 0; i <= 3 ; i++) {
                OrderDistributionStatusVo orderDistributionStatusVo = new OrderDistributionStatusVo();

                if ( i == 0 ) {
                    //配送状态有可能跳过一些状态，要做区分
                    if ( statusList.containsKey(OrderDistributionStatusEnum.WAIT_ORDER_CODE)) {
                        Integer index = statusList.get(OrderDistributionStatusEnum.WAIT_ORDER_CODE);
                        orderDistributionStatusVo = tempList.get(index);
                        activeStatusId = 0;
                        activeStatus = orderDistributionStatusVo.getStatusCode();
                    } else {
                        orderDistributionStatusVo.setStatusMsg("门店发单");
                        orderDistributionStatusVo.setCreateTime(orderDetail.getCreateTime());

                        //生成唯一的订单配送状态id
                        Long id = this.getId(ids);

                        orderDistributionStatusVo.setId(id);
                    }
                    resultList.add(orderDistributionStatusVo);
                }

                if ( i == 1 ) {
                    if ( statusList.containsKey(OrderDistributionStatusEnum.WAIT_DELIVERY_CODE)) {
                        Integer index = statusList.get(OrderDistributionStatusEnum.WAIT_DELIVERY_CODE);
                        orderDistributionStatusVo = tempList.get(index);
                        activeStatusId = 1;
                        activeStatus = orderDistributionStatusVo.getStatusCode();
                    } else {
                        orderDistributionStatusVo.setStatusMsg("骑手接单");
                        //生成唯一的订单配送状态id
                        Long id = this.getId(ids);

                        orderDistributionStatusVo.setId(id);
                    }
                    resultList.add(orderDistributionStatusVo);
                }

                if (i == 2 ) {
                    if ( statusList.containsKey(OrderDistributionStatusEnum.DISTRIBUTION_CODE)) {
                        Integer index = statusList.get(OrderDistributionStatusEnum.DISTRIBUTION_CODE);
                        orderDistributionStatusVo = tempList.get(index);
                        activeStatusId = 2;
                        activeStatus = orderDistributionStatusVo.getStatusCode();
                    } else {
                        orderDistributionStatusVo.setStatusMsg("骑手取货");
                        //生成唯一的订单配送状态id
                        Long id = this.getId(ids);
                        orderDistributionStatusVo.setId(id);
                    }
                    resultList.add(orderDistributionStatusVo);
                }

                if (i == 3 ) {
                    if ( statusList.containsKey(OrderDistributionStatusEnum.FINISH_CODE)) {
                        int index = statusList.get(OrderDistributionStatusEnum.FINISH_CODE);
                        orderDistributionStatusVo = tempList.get(index);
                        activeStatusId = 3;
                        activeStatus = orderDistributionStatusVo.getStatusCode();
                    } else {
                        orderDistributionStatusVo.setStatusMsg("骑手送达");
                        //生成唯一的订单配送状态id
                        Long id = this.getId(ids);
                        orderDistributionStatusVo.setId(id);
                    }

                    resultList.add(orderDistributionStatusVo);
                }

            }

        } else {
            //不在正常流程，就返回全部的状态
            resultList.addAll(tempList);
            activeStatusId = tempList.size()-1;
            activeStatus = tempList.get(activeStatusId).getStatusCode();
        }

        if ( resultList != null ) {
            orderDetail.setActiveStatusId( activeStatusId );
            orderDetail.setActiveStatus(activeStatus);
        }

        return resultList;
    }

    /**
     * 发起取消订单请求
     * @param orderId
     * @param orderType
     * @return
     */
    public String cancelOrderByOrderId(String orderId, Integer merchantId,  int orderType) {
        MerchantConfig merchantConfig = merchantConfigMapper.selectByPrimaryKey(merchantId);
        if ( merchantConfig == null ) {
            if (orderType==2) {
                logger.warn(String.format("cancelExpectedOrderByOrderId fail.Because there is no merchantConfig.orderId: %s,merchantId:%s;",
                        orderId, merchantId));
            } else {
                logger.warn(String.format("cancelOrderByOrderId fail.Because there is no merchantConfig.orderId: %s,merchantId:%s;",
                        orderId, merchantId));
            }
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "后台数据异常!");
        }
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> params = new HashMap<>();

        //封装参数
        data.put("order_id", orderId);
        data.put("cancel_source", CANCEL_SOURCE);
        params.put(APP_ID , merchantConfig.getAppKey());
        params.put(SOURCE_ID, merchantConfig.getMerchantId());
        params.put(SECRET_KEY, merchantConfig.getSecret());

        //组装请求url
        //String url = DOMIAN + CANCEL_APIURL;

        //查询门店url
        String cancelOrderUrl = paramMapper.queryParamByKey(ParamEnum.CANCEL_ORDER);

        //获取请求参数
        JSONObject requestParams = this.assembleParam(data, params);
        String resultJson = HttpClient.postBody(cancelOrderUrl,JSONObject.toJSONString(requestParams));

        return resultJson;
    }

    /**
     * 获取请求参数
     * @param cancelOrderParams
     * @return
     */
    private JSONObject assembleParam(Map<String, Object> data, Map<String, Object> cancelOrderParams) {
        //请求接口参数json
        JSONObject paramsJson = new JSONObject();
        try {
            String timestamp = DateUtil.unixTime().toString();

            JSONObject dataJson = (JSONObject)JSONObject.toJSON( data );
            paramsJson.put(PARAM_DATA,dataJson);

            //传入参数，
            // format（数据类型，默认json）,timestamp（时间戳）,app_id（商家唯一标识）,
            // source_id(商户编号)
            paramsJson.put(TIMESTAMP_KEY, timestamp);
            paramsJson.put(APP_ID, cancelOrderParams.get(APP_ID));
            paramsJson.put(SOURCE_ID, cancelOrderParams.get(SOURCE_ID));
            //参数签名
            Object secretObject = cancelOrderParams.get(SECRET_KEY);
            String secret_key = (String)secretObject;
            String signature = SignHelper.generateSign(paramsJson, secret_key);
            paramsJson.put(SIGNATURE_KEY,signature);
        } catch (Exception e) {
            logger.error(String.format("获取请求参数异常！"));
            e.printStackTrace();
        }
        return paramsJson;
    }
}
