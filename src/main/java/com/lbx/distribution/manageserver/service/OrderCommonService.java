package com.lbx.distribution.manageserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.common.OrderDistributionStatusEnum;
import com.lbx.distribution.manageserver.common.OrderStatusEnum;
import com.lbx.distribution.manageserver.entity.PageResult;
import com.lbx.distribution.manageserver.entity.order.*;
import com.lbx.distribution.manageserver.entity.orderExpected.OrderExpected;
import com.lbx.distribution.manageserver.entity.orderExpected.OrderExpectedListItem;
import com.lbx.distribution.manageserver.helper.OrderHelper;
import com.lbx.distribution.manageserver.mapper.*;
import com.lbx.distribution.manageserver.util.ManageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName: 订单List service
 * @Description:
 */
@Service
public class OrderCommonService {
    private static Logger logger = LoggerFactory.getLogger(OrderCommonService.class);

    @Autowired
    OrderHelper orderHelper;
    @Autowired
    private ManageCommonService manageCommonService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private OrderPredictPriceMapper orderPredictPriceMapper;
    @Autowired
    private CityMatchMapper cityMatchMapper;
    @Autowired
    private OrderCargoListMapper orderCargoListMapper;
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
    private CityMatchService cityMatchService;
    @Autowired
    private OrderExpectedMapper orderExpectedMapper;

    /**
     * 分页获取订单列表
     * @param request
     * @return
     */
    public PageResult getOrderList(Map<String, Object>  request ) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>开始执行分页获取订单列表操作>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        long beginTime = new Date().getTime();

        List<OrderListItem> orderList = null;
        PageResult pageResult = new PageResult();

        //判断是否需要分页
        Object isPage = request.get("isPage");

        if (isPage == null) {
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "没有isPage!");
        }

        //1、先确定满足条件的订单id
        Map<String, Object> params = orderHelper.getOrderIdsParams(request);
        if ( params.get("orderIds") == null && params.get("orderId") == null){
            //没有满足条件的订单
            pageResult.setTotal(0);
            return pageResult;
        }
        //获取其中的预约单信息
        Map<String, OrderExpectedListItem> orderExpectMap = this.getAllTypeExpectedOrders(params);

        long getOrderIdsTime = new Date().getTime();
        logger.info(">>>>>>>>>>>>>>>>>>>>>满足条件的订单id，执行时间："+ (getOrderIdsTime - beginTime)  +"毫秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        //2、根据订单id查询订单列表的信息
        Integer judgePage = (Integer) isPage;
        if (judgePage == 1 ){
            Integer pageSize = manageCommonService.getPageSize( request );
            Integer pageNum = manageCommonService.getPageNum( request );

            if ( pageSize == null || pageNum == null ) {
                logger.warn(String.format("没有pageSize或pageNum! pageSize: %s, pageNum: %s ;", pageSize, pageNum ));
                throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "没有pageSize或pageNum!");
            }

            //分页处理
            PageHelper.startPage( pageNum, pageSize );

            orderList = this.queryOrderList( params );

            //取记录总条数
            PageInfo<OrderListItem> pageInfo = new PageInfo<>( orderList );
            pageResult.setTotal( pageInfo.getTotal() );
            pageResult.setPageNum( pageInfo.getPageNum() );
        } else {
            orderList = this.queryOrderList( params );

            if (orderList != null){
                pageResult.setTotal( orderList.size() );
            } else {
                pageResult.setTotal(0);
            }
        }

        //填充订单状态信息和渠道名称  distribution
        orderList = this.setDistributionData( orderList, orderExpectMap );

        //如果筛选条件有订单状态，并且如状态码<=1（选择的是待接单或者已下单），此时无渠道配送的相关信息，不用管
        Object statusObject = request.get("status");
        if ( statusObject == null ) {
            //填充骑手信息 tb_order_distribution_status
            orderList = this.setCourierData( orderList );
        } else {
            Integer statusCode = (Integer) statusObject;
            if( statusCode > 1 ) {
                //填充骑手信息 tb_order_distribution_status
                orderList = this.setCourierData( orderList );
            }
        }

        //填充预估价格信息
        orderList = this.setPredictPrice(orderList);

        //填充城市名称
        orderList = this.setCityName( orderList );

        //补充订单类型。 1: 即时单(尽快送达，限当日订单) 2: 预约单
        orderList = this.setOrderTypeName(orderList);

        //做预约单的创建时间：商城的下单时间
        orderList = this.setExceptedOrderCreatTime(orderList, orderExpectMap);

        pageResult.setList( orderList );

        long endTime = new Date().getTime();
        logger.info(">>>>>>>>>>>>>>>>>>>>>结束执行分页获取订单列表数量操作，执行时间："+ (endTime - beginTime)  +"毫秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return pageResult;
    }

    /**
     * 获取所有的预约单信息
     * @param params
     * @return
     */
    private Map<String, OrderExpectedListItem> getAllTypeExpectedOrders(Map<String, Object> params) {
        List<OrderExpectedListItem> orderExpectedList= orderExpectedMapper.queryAllTypeExpectedOrders(params);
        if (orderExpectedList == null) {
            return null;
        }
        Map<String, OrderExpectedListItem> orderExpectedMap = new HashMap<>();
        for (OrderExpectedListItem orderExpectedListItem:orderExpectedList ) {
            orderExpectedMap.put(orderExpectedListItem.getOrderId(), orderExpectedListItem);
        }
        return orderExpectedMap;
    }

    /**
     * 做预约单的处理
     * @param orderList
     * @param orderExpectMap
     * @return
     */
    private List<OrderListItem> setExceptedOrderCreatTime(List<OrderListItem> orderList, Map<String, OrderExpectedListItem> orderExpectMap) {
        if (orderExpectMap == null || orderList== null) {
            return orderList;
        }

        for (OrderListItem orderListItem:orderList ) {
            //订单类型：预约单-2，1-即时单
            Integer orderType = orderListItem.getOrderType();
            Integer distributeStatus = orderListItem.getDistributeStatus();
            if (orderType == 2 && distributeStatus != null) {
                OrderExpectedListItem orderExpectedListItem = orderExpectMap.get(orderListItem.getOrderId());
                if (orderExpectedListItem != null) {
                    orderListItem.setCreateTime(orderExpectedListItem.getCreateTime());
                }
            }
        }
        return orderList;
    }

    /**
     * 填充订单类型名称，1: 即时单(尽快送达，限当日订单) 2: 预约单
     */
    private List<OrderListItem> setOrderTypeName(List<OrderListItem> orderList) {
        if (orderList == null) {
            return null;
        }

        for (OrderListItem orderListItem:orderList ) {
            Integer orderType = orderListItem.getOrderType();
            if (orderType == 1) {
                orderListItem.setOrderTypeName("即时单");
            } else if (orderType == 2){
                orderListItem.setOrderTypeName("预约单");
            }
        }
        return orderList;
    }

    /**
     * 填充价格信息（根据预估价格返回）
     * @param orderList
     * @return
     */
    private List<OrderListItem> setPredictPrice(List<OrderListItem> orderList) {
        List<OrderListItem> resultList = new ArrayList<>();

        //1、分发渠道成功的，返回预估价格，还没分发成功(/失败订单)就返回null
        for (OrderListItem orderListItem:orderList ) {
            Integer channelId = orderListItem.getChannelId();
            //如果还没分发成功，就不填写
            if (orderListItem.getDistributeStatus() == null || channelId == 0) {
                orderListItem.setPredictPrice(null);
                continue;
            }

            Integer distributeStatus = orderListItem.getDistributeStatus();
            if (distributeStatus <= 0) {
                orderListItem.setPredictPrice(null);
            } else {
                String orderId = orderListItem.getOrderId();

                //封装参数
                OrderPredictPrice orderPredictPrice = new OrderPredictPrice();
                orderPredictPrice.setOrderId(orderId);
                orderPredictPrice.setChannelId(channelId);
                orderPredictPrice = orderPredictPriceMapper.selectByPrimaryKey( orderPredictPrice );

                orderListItem.setPredictPrice( orderPredictPrice.getPredictPrice() );
            }
            resultList.add(orderListItem);
        }

        return orderList;
    }

    /**
     * 填充骑手信息
     * @param orderList
     * @return
     */
    private List<OrderListItem> setCourierData(List<OrderListItem> orderList) {

        if ( orderList == null ) {
            return null;
        }

        for (OrderListItem vo:orderList ) {
            if (vo.getDistributeStatus() == null || vo.getChannelId() == 0 ){
                //未分发成功骑手及骑手电话默认为"-"
                vo.setCourierName( "-" );
                vo.setCourierPhone( "-" );
                continue;
            }

            Map<String, Object> params = new HashMap<>();
            params.put("channelId",  vo.getChannelId());
            params.put("orderId", vo.getOrderId());
            params.put("statusCode", vo.getStatusCode());

            //分发成功以后才会有配送信息（分发状态(0:未分发;1:分发渠道成功;4:订单已完成;5:订单已取消;6:订单异常;-1:失败订单))）
            if ( vo.getDistributeStatus() > 0 ) {
                OrderDistributionStatus status = orderDistStatusMapper.queryDistStatus( params );

                if( status == null ) {
                    continue;
                }

                vo.setCourierName( status.getCourierName() );
                vo.setCourierPhone( status.getCourierPhone() );
            }
        }

        return orderList;
    }

    /**
     * 填充城市信息
     * @param orderList
     * @return
     */
    private List<OrderListItem> setCityName(List<OrderListItem> orderList ) {

        if ( orderList == null ) {
            return null;
        }

        for (OrderListItem vo:orderList ) {
            String cityName = cityMatchService.queryCityName( vo.getCityCode() );
            vo.setCityName(cityName);
        }

        return orderList;
    }

    public List<OrderListItem> queryOrderList(Map<String, Object>  params) {
        return orderMapper.queryOrderList( params );
    }

    /**
     * 根据订单列表中已获取的渠道id，填充渠道名称
     * @param orderList
     * @return
     */
    private List<OrderListItem> setDistributionData(List<OrderListItem> orderList, Map<String, OrderExpectedListItem> orderExpectMap) {
        if (orderList == null) {
            return null;
        }

        for (OrderListItem item:orderList ) {
            Integer channelId = item.getChannelId();
            String orderId = item.getOrderId();
            Integer distributeStatus = item.getDistributeStatus();

            if (distributeStatus != null) {
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
            } else {
                OrderExpectedListItem orderExpected = orderExpectMap.get(item.getOrderId());
                if (orderExpected != null) {
                    item.setCreateTime(orderExpected.getCreateTime());
                    item.setUpdateTime(orderExpected.getUpdateTime());
                }
                item.setStatusCode(5);
                //转换为显示用的订单状态
                item.setStatusMsg( OrderStatusEnum.getStatusMsg(5) );
                item.setChannelName("-");
                item.setChannelPeisongId("-");
            }
        }

        return orderList;
    }

    /**
     * 获取订单配送信息
     * @param channelId
     * @param orderId
     * @param distributeStatus
     * @return
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
            if (!OrderDistributionStatusEnum.isInTbOrderDistribution(statusCode)) {
                statusCode = distributeStatus;
            }

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
     * 获取订单详情
     * @param request
     * @return
     */
    public OrderDetail queryOrderDetailByOrderId(Map<String, Object> request ) {
        Object orderIdObject = request.get("orderId");

        if ( orderIdObject == null ) {
            logger.warn(String.format("缺少订单id! orderId: %s;", orderIdObject ));
            throw new ManageException( ManageResultCode.DATA_REQUEST_ERROR, "缺少订单id" );
        }

        //根据订单id
        OrderDetail orderDetail = orderMapper.queryOrderDetailByOrderId( request );

        if(orderDetail != null ) {
            //填充城市信息
            String cityName = cityMatchService.queryCityName( orderDetail.getCityCode() );
            orderDetail.setCityName(cityName);

            //填充货物列表
            String orderId = (String) orderIdObject;
            List<OrderCargoItem> orderCargoItems = orderCargoListMapper.selectByOrderId( orderId );
            orderDetail.setOrderCargoItems(orderCargoItems);

            //填充配送信息，分 已分发成功和未分发成功两种情况进行处理
            List<OrderDistributionStatusVo> orderDisStatusList = null;
            Integer distributeStatus = orderDetail.getDistributeStatus();
            if (distributeStatus != null) {
                //填充异常信息
                if (OrderStatusEnum.isExceptionStatus(distributeStatus)) {
                    //去存异常信息的表里查异常信息
                    
                }

                //填充配送信息
                if ( distributeStatus > 0 && orderDetail.getChannelId()!=0 ) {
                    //填充渠道名称
                    String channelName = channelMapper.queryChannelNameByChannelId(orderDetail.getChannelId());
                    orderDetail.setChannelName(channelName);

                    //填充骑手信息
                    orderDetail = this.setCourierData( request, orderDetail );

                    //加上渠道信息查询，配送状态
                    request.put("channelId", orderDetail.getChannelId());
                    orderDisStatusList = orderDistStatusMapper.queryDistStatusList( request );

                    //填充预估价信息
                    OrderPredictPrice orderPredictPrice = new OrderPredictPrice();
                    orderPredictPrice.setOrderId(orderDetail.getOrderId());
                    orderPredictPrice.setChannelId( orderDetail.getChannelId() );
                    orderPredictPrice = orderPredictPriceMapper.selectByPrimaryKey(orderPredictPrice);
                    orderDetail.setPredictPrice( orderPredictPrice.getPredictPrice() );

                } else if (distributeStatus == -1 || distributeStatus == 6 || distributeStatus == 5){
                    //分发成功前就失败/异常/取消的订单，价格设置成Null
                    orderDetail.setPredictPrice( null );
                    orderDisStatusList = new ArrayList<>();

                    OrderDistributionStatusVo placedStatusVo = new OrderDistributionStatusVo();
                    Long placedId = orderHelper.getId(null);
                    OrderDistributionStatusVo currentStatusVo = new OrderDistributionStatusVo();
                    Long currentStatusId = orderHelper.getId(null);
                    if (distributeStatus == 6 || distributeStatus == -1) {
                        placedStatusVo.setStatusCode(OrderDistributionStatusEnum.PLACED_ORDER_CODE);
                        currentStatusVo.setStatusCode(OrderDistributionStatusEnum.SYS_EXCEPTION_CODE);
                    } else {
                        placedStatusVo.setStatusCode(OrderDistributionStatusEnum.PLACED_ORDER_CODE);
                        currentStatusVo.setStatusCode(OrderDistributionStatusEnum.CANCEL_CODE);
                    }
                    placedStatusVo.setId(placedId);
                    placedStatusVo.setCreateTime(orderDetail.getCreateTime());
                    currentStatusVo.setId(currentStatusId);
                    currentStatusVo.setCreateTime(orderDetail.getUpdateTime());
                    orderDisStatusList.add(placedStatusVo);
                    orderDisStatusList.add(currentStatusVo);

                    orderDetail.setCourierName("-");
                    orderDetail.setCourierPhone("-");
                    orderDetail.setChannelPeisongId("-");
                    orderDetail.setChannelName("-");
                } else {
                    //未分发成功/下单失败订单，价格设置成Null
                    orderDetail.setPredictPrice( null );
                    orderDetail.setCourierName("-");
                    orderDetail.setCourierPhone("-");
                    orderDetail.setChannelPeisongId("-");
                    orderDetail.setChannelName("-");
                }
            } else {
                //预约单，还没正式发单就取消的
                orderDisStatusList = new ArrayList<>();
                OrderExpected orderExpected = orderExpectedMapper.selectByPrimaryKey(orderDetail.getOrderId());

                orderDetail.setPredictPrice( null );
                orderDetail.setCourierName("-");
                orderDetail.setCourierPhone("-");
                orderDetail.setChannelPeisongId("-");
                orderDetail.setChannelName("-");
                OrderDistributionStatusVo placedStatusVo = new OrderDistributionStatusVo();
                Long placedId = orderHelper.getId(null);
                placedStatusVo.setStatusCode(OrderDistributionStatusEnum.PLACED_ORDER_CODE);
                placedStatusVo.setId(placedId);
                placedStatusVo.setCreateTime(orderExpected.getCreateTime());
                orderDisStatusList.add(placedStatusVo);
                //如果已取消，就再手动添加
                OrderDistributionStatusVo cancelStatusVo = new OrderDistributionStatusVo();
                Long cancelId = orderHelper.getId(null);
                cancelStatusVo.setStatusCode(OrderDistributionStatusEnum.CANCEL_CODE);
                cancelStatusVo.setId(cancelId);
                cancelStatusVo.setCreateTime(orderExpected.getUpdateTime());
                orderDisStatusList.add(cancelStatusVo);
            }

            //一个状态code可能会有多条数据，去重
            List<OrderDistributionStatusVo> tempList = new ArrayList<>();
            Set<Integer> statusCodeSet = new HashSet<>();
            if (orderDisStatusList != null && orderDisStatusList.size() > 0) {
                for (OrderDistributionStatusVo odv:orderDisStatusList ) {
                    Integer statusCode = odv.getStatusCode();
                    //100：骑手进店，处理为待取货
                    if (statusCode == 100) {
                        statusCode = 2;
                        odv.setStatusCode(2);
                    }

                    //系统异常状态码统一处理成1000
                    if (statusCode>=7 || statusCode<0) {
                        statusCode = 1000;
                        odv.setStatusCode(statusCode);
                    }
                    if ( statusCodeSet.contains(statusCode) ) {
                        continue;
                    }

                    tempList.add( odv );
                    statusCodeSet.add(odv.getStatusCode());
                }

                //运单号
                orderDetail.setChannelPeisongId(orderDisStatusList.get(0).getChannelPeisongId());
            }

            //对正常流程和异常流程返回列表的区分
            List<OrderDistributionStatusVo> resultList = orderHelper.getResultList(tempList, orderDetail);

            if ( resultList != null && resultList.size() > 0) {
                //做配送状态描述的映射
                for (OrderDistributionStatusVo odv:resultList ) {
                    Integer statusCode = odv.getStatusCode();
                    if ( statusCode != null ) {
                        String msg = null;
                        msg = OrderDistributionStatusEnum.getStatusMsg( statusCode );
                        odv.setStatusMsg(msg);
                        if (statusCode == 5) {
                            orderDetail.setCancelReason(odv.getCancelReason());
                            orderDetail.setCancelReasonId(odv.getCancelReasonId());
                        }
                    }
                }
            }

            //填充配送状态列表
            orderDetail.setOrderDistStatusList( resultList );

        }


        //填充预约送达时间,1: 即时单(尽快送达，限当日订单) 2: 预约单
        Integer orderType = orderDetail.getOrderType();
        if ( orderType == 2) {
            orderDetail.setOrderTypeName("预约单");
            OrderExpected orderExpected = orderExpectedMapper.selectByPrimaryKey(orderDetail.getOrderId());
            if (orderExpected!=null) {
                orderDetail.setExpectedDeliveryTime(orderExpected.getExpectedDeliveryTime());
            }
        } else if (orderType == 1) {
            orderDetail.setOrderTypeName("即时单");
        }

        return orderDetail;
    }

    /**
     * 填写配送信息
     * @return
     */
    private OrderDetail setCourierData(Map<String, Object> request , OrderDetail orderDetail) {
        request.put("channelId", orderDetail.getChannelId());
        List<OrderDistributionStatus> orderDistributionStatusList = orderDistStatusMapper.queryCourierData( request );

        if (orderDistributionStatusList!=null) {
            for (OrderDistributionStatus distributionStatus:orderDistributionStatusList ) {
                if (distributionStatus.getCourierName()!=null && distributionStatus.getCourierPhone()!=null) {
                    orderDetail.setCourierName(distributionStatus.getCourierName());
                    orderDetail.setCourierPhone(distributionStatus.getCourierPhone());
                    break;
                }
            }
        }
        if (orderDetail.getCourierName()==null&&orderDetail.getCourierPhone()==null){
            orderDetail.setCourierName( "-" );
            orderDetail.setCourierPhone( "-" );
        }

        return orderDetail;
    }

    /**
     * 查询满足条件的订单列表（导出excel，包括订单详情）
     * @param request
     * @return
     */
    public List<OrderExcelListItem> queryOrderExcelList(Map<String, Object> request) {
        List<OrderExcelListItem> orderExceList = null;

        //1、拿到符合条件的订单id
        Map<String, Object> orderIdsParams = orderHelper.getOrderIdsParams(request);
        if(orderIdsParams.get("orderIds") == null && orderIdsParams.get("orderId") == null){
            //没有满足条件的订单
            return null;
        }
        Map<String, OrderExpectedListItem> orderExpectMap = this.getAllTypeExpectedOrders(orderIdsParams);
        Map<String, OrderPredictPrice> orderPredictPriceMap = this.getOrderPredictPrice(orderIdsParams);

        //2、根据订单id查订单列表
        orderExceList = orderMapper.queryOrderExcelList(orderIdsParams);

        //statusMsg  courierName courierPhone itemName itemPrice itemQuantity money
        //3、填充订单状态和骑手信息
        if (orderExceList != null && orderExceList.size() != 0) {
            for (OrderExcelListItem item:orderExceList ) {
                //1）填充订单配送信息
                Integer channelId = item.getChannelId();
                String orderId = item.getOrderId();
                Integer distributeStatus = item.getDistributeStatus();

                if (distributeStatus != null) {
                    Map<String, Object> result =  this.getDistributeData(channelId, orderId, distributeStatus);
                    Object channelPeisongIdObject = result.get("channelPeisongId");
                    Object channelNameObject = result.get("channelName");
                    Object statusCodeObject = result.get("statusCode");
                    Integer statusCode = null;
                    if (statusCodeObject != null) {
                        //订单配送状态
                        statusCode = (Integer) statusCodeObject;
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
                        //渠道名称
                        item.setChannelName(channelName);
                    } else {
                        item.setChannelName("-");
                    }

                    //2）填充骑手信息
                    if ( statusCode > 1 && channelId != 0) {
                        Map<String, Object> params = new HashMap<>();
                        params.put("orderId", item.getOrderId());
                        params.put("channelId",channelId );

                        List<OrderDistributionStatus> distributionStatusList = orderDistStatusMapper.queryCourierData(params);
                        if (distributeStatus!=null) {
                            for (OrderDistributionStatus distributionStatus:distributionStatusList ) {
                                if (distributionStatus.getCourierName()!=null && distributionStatus.getCourierPhone()!=null) {
                                    item.setCourierName(distributionStatus.getCourierName());
                                    item.setCourierPhone(distributionStatus.getCourierPhone());
                                    break;
                                }
                            }
                        }
                    }

                    if (item.getCourierName()==null&&item.getCourierPhone()==null){
                        item.setCourierName( "-" );
                        item.setCourierPhone( "-" );
                    }

                    //3）填充预估价信息
                    OrderPredictPrice orderPredictPrice = orderPredictPriceMap.get(""+ item.getOrderId()+ item.getChannelId());
                    if (orderPredictPrice != null) {
                        orderPredictPrice = orderPredictPriceMapper.selectByPrimaryKey(orderPredictPrice);
                        item.setPredictPrice( orderPredictPrice.getPredictPrice() );
                        item.setBasePrice(orderPredictPrice.getBasePrice());
                        item.setAddTimePrice(orderPredictPrice.getAddTimePrice());
                        item.setAddDistancePrice(orderPredictPrice.getAddDistancePrice());
                        item.setAddWeightPrice(orderPredictPrice.getAddWeightPrice());
                    }
                } else {
                    item.setStatusCode(5);
                    //转换为显示用的订单状态
                    item.setStatusMsg( OrderStatusEnum.getStatusMsg(5) );
                    item.setChannelName("-");
                    item.setChannelPeisongId("-");
                    item.setCourierName("-");
                    item.setCourierPhone("-");
                    item.setPredictPrice( null );
                }
            }

            //4、根据订单列表查询订单的货物详情
            for (OrderExcelListItem item:orderExceList ) {
                String orderId = item.getOrderId();
                List<OrderExcelCargoItem> orderCargoItems = orderCargoListMapper.selectOrderExcelCargoItemByOrderId(orderId);
                item.setOrderCargoItems(orderCargoItems);
            }
        }

        //补充订单类型。 1: 即时单(尽快送达，限当日订单) 2: 预约单
        if (orderExceList != null) {
            for (OrderExcelListItem orderExcelListItem:orderExceList ) {
                Integer orderType = orderExcelListItem.getOrderType();
                if (orderType == 1) {
                    orderExcelListItem.setOrderTypeName("即时单");
                } else if (orderType == 2){
                    orderExcelListItem.setOrderTypeName("预约单");

                    //预约单的创建时间：商城的下单时间
                    if (orderExpectMap != null) {
                        OrderExpectedListItem orderExpected = orderExpectMap.get(orderExcelListItem.getOrderId());
                        if (orderExpected != null) {
                            orderExcelListItem.setCreateTime(orderExpected.getCreateTime());
                        }
                    }
                }
            }
        }

        return orderExceList;
    }

    /**
     * 获取订单的配送价格及加价信息
     * @param orderIdsParams
     * @return
     */
    private Map<String, OrderPredictPrice> getOrderPredictPrice(Map<String, Object> orderIdsParams) {
        List<OrderPredictPrice> orderPredictPriceList = orderPredictPriceMapper.queryOrderPredictPrice(orderIdsParams);
        if (orderPredictPriceList == null) {
            return null;
        }
        Map<String, OrderPredictPrice> orderPredictPriceMap = new HashMap<>();
        for (OrderPredictPrice orderPredictPrice:orderPredictPriceList ) {
            orderPredictPriceMap.put(""+orderPredictPrice.getOrderId()+orderPredictPrice.getChannelId(), orderPredictPrice);
        }
        return orderPredictPriceMap;
    }
}
