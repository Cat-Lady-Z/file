package com.lbx.distribution.manageserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.common.OrderDistributionStatusEnum;
import com.lbx.distribution.manageserver.entity.PageResult;
import com.lbx.distribution.manageserver.entity.order.OrderCargoItem;
import com.lbx.distribution.manageserver.entity.order.OrderDistributionStatusVo;
import com.lbx.distribution.manageserver.entity.orderExpected.OrderExpectedDetail;
import com.lbx.distribution.manageserver.entity.orderExpected.OrderExpected;
import com.lbx.distribution.manageserver.entity.orderExpected.OrderExpectedListItem;
import com.lbx.distribution.manageserver.helper.OrderHelper;
import com.lbx.distribution.manageserver.mapper.MerchantConfigMapper;
import com.lbx.distribution.manageserver.mapper.OrderCargoListMapper;
import com.lbx.distribution.manageserver.mapper.OrderExpectedMapper;
import com.lbx.distribution.manageserver.mapper.ParamMapper;
import com.lbx.distribution.manageserver.util.DateUtil;
import com.lbx.distribution.manageserver.util.ManageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName:
 * @Description: 预约单service
 */
@Service
public class ExpectedOrderService {
    private static Logger logger = LoggerFactory.getLogger(ExpectedOrderService.class);

    @Autowired
    private OrderExpectedMapper orderExpectedMapper;
    @Autowired
    private ManageCommonService manageCommonService;
    @Autowired
    private CityMatchService cityMatchService;
    @Autowired
    private OrderCargoListMapper orderCargoListMapper;
    @Autowired
    private OrderHelper orderHelper;
    @Autowired
    private MerchantConfigMapper merchantConfigMapper;
    @Autowired
    private ParamMapper paramMapper;

    /**
     * 获取预约单列表
     * @param request
     * @return
     */
    public String expectedOrderList(Map<String, Object> request) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>开始执行分页获取预约订单列表操作>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        long beginTime = new Date().getTime();

        List<OrderExpectedListItem> expectedOrderList = null;
        PageResult pageResult = new PageResult();

        //判断是否需要分页
        Object isPage = request.get("isPage");

        if (isPage == null) {
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "没有isPage!");
        }

        //1、封装查询参数
        Map<String, Object> dataParams = new HashMap<>();
        dataParams.put("merchantId", request.get("merchantId"));
        dataParams.put("originId", request.get("originId"));
        dataParams.put("sendId", request.get("sendId"));
        dataParams.put("distributionOrderId", request.get("distributionOrderId"));
        Object startTimeObject = request.get("startTime");
        Object endTimeObject = request.get("endTime");
        if (startTimeObject != null && !"".equals(endTimeObject) && endTimeObject != null && !"".equals(endTimeObject)) {
            Long startTimeLong = new Long( String.valueOf( startTimeObject ) );
            Long endTimeLong = new Long( String.valueOf( endTimeObject ) );
            dataParams.put("startTime", DateUtil.fromUnixTime(startTimeLong));
            dataParams.put("endTime", DateUtil.fromUnixTime(endTimeLong));
        }

        //2、根据订单id查询预约订单列表的信息
        Integer judgePage = (Integer) isPage;
        if (judgePage == 1 ){
            Integer pageSize = (Integer) request.get("pageSize");
            Integer pageNum = (Integer) request.get("pageNum");

            if ( pageSize == null || pageNum == null ) {
                logger.warn(String.format("没有pageSize或pageNum! page_size: %s, pageNum: %s ;", pageSize, pageNum ));
                throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "缺少参数：pageSize/pageNum!");
            }

            //分页处理
            PageHelper.startPage( pageNum, pageSize );

            expectedOrderList = this.queryExpectedOrderList( dataParams );

            //取记录总条数
            PageInfo<OrderExpectedListItem> pageInfo = new PageInfo<>( expectedOrderList );
            pageResult.setTotal( pageInfo.getTotal() );
            pageResult.setPageNum( pageInfo.getPageNum() );
        } else {
            expectedOrderList = this.queryExpectedOrderList( dataParams );

            if (expectedOrderList != null){
                pageResult.setTotal( expectedOrderList.size() );
            } else {
                pageResult.setTotal(0);
            }
        }


        //填充城市名称
        expectedOrderList = this.setCityName( expectedOrderList );

        //填充订单类型order_type， 1: 即时单(尽快送达，限当日订单) 2: 预约单
        expectedOrderList = this.setOrderTypeName( expectedOrderList );

        pageResult.setList( expectedOrderList );

        long endTime = new Date().getTime();
        logger.info(">>>>>>>>>>>>>>>>>>>>>结束执行分页获取预约订单列表数量操作，执行时间："+ (endTime - beginTime)  +"毫秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        String resp = manageCommonService.getRespBody(pageResult);

        return resp;
    }

    /**
     * 填充订单类型名称
     */
    private List<OrderExpectedListItem> setOrderTypeName(List<OrderExpectedListItem> expectedOrderList) {
        if (expectedOrderList == null) {
            return null;
        }

        for (OrderExpectedListItem orderExpectedListItem:expectedOrderList) {
            orderExpectedListItem.setOrderTypeName("预约单");
        }

        return expectedOrderList;
    }

    private List<OrderExpectedListItem> setCityName(List<OrderExpectedListItem> expectedOrderList) {
        if ( expectedOrderList == null ) {
            return null;
        }

        for (OrderExpectedListItem orderExpected:expectedOrderList ) {
            String cityName = cityMatchService.queryCityName( orderExpected.getCityCode() );
            orderExpected.setCityName(cityName);
        }

        return expectedOrderList;
    }

    /**
     * 获取预约订单列表
     */
    private List<OrderExpectedListItem> queryExpectedOrderList(Map<String, Object> dataParams) {
        return orderExpectedMapper.queryExpectedOrders(dataParams);
    }

    /**
     * 获取订单详情
     * @param request
     * @return
     */
    public String queryExpectedOrderDetail(Map<String, String> request) {
        String orderId = request.get("orderId");

        if ( orderId == null ) {
            logger.warn(String.format("缺少订单id! order_id: %s;", orderId ));
            throw new ManageException( ManageResultCode.DATA_REQUEST_ERROR, "缺少订单id" );
        }

        //根据订单id
        OrderExpectedDetail orderExpectedDetail = orderExpectedMapper.queryExpectedOrderByOrderId( orderId );

        if(orderExpectedDetail != null ) {
            //填充城市信息
            String cityName = cityMatchService.queryCityName( orderExpectedDetail.getCityCode() );
            orderExpectedDetail.setCityName(cityName);

            //填充货物列表
            List<OrderCargoItem> orderCargoItems = orderCargoListMapper.selectByOrderId( orderId );
            orderExpectedDetail.setOrderCargoItems(orderCargoItems);

            //填充配送信息，分 已分发和未分发两种情况进行处理
            List<OrderDistributionStatusVo> orderDisStatusList = new ArrayList<>();
            orderExpectedDetail.setPredictPrice( null );

            OrderDistributionStatusVo placedStatusVo = new OrderDistributionStatusVo();
            Long placedId = orderHelper.getId(null);
            placedStatusVo.setStatusCode(OrderDistributionStatusEnum.PLACED_ORDER_CODE);
            placedStatusVo.setId(placedId);
            placedStatusVo.setCreateTime(orderExpectedDetail.getCreateTime());
            orderDisStatusList.add(placedStatusVo);
            //如果已取消，就再手动添加
            if (orderExpectedDetail.getSendId() == 5) {
                OrderDistributionStatusVo cancelStatusVo = new OrderDistributionStatusVo();
                Long cancelId = orderHelper.getId(null);
                cancelStatusVo.setStatusCode(OrderDistributionStatusEnum.CANCEL_CODE);
                placedStatusVo.setId(cancelId);
                placedStatusVo.setCreateTime(orderExpectedDetail.getUpdateTime());
                orderDisStatusList.add(cancelStatusVo);
            }

            orderExpectedDetail.setCourierName("-");
            orderExpectedDetail.setCourierPhone("-");
            orderExpectedDetail.setChannelPeisongId("-");
            orderExpectedDetail.setChannelName("-");
            List<OrderDistributionStatusVo> resultList = orderHelper.getResultList(orderDisStatusList, orderExpectedDetail);

            if ( resultList != null && resultList.size() > 0) {
                //做配送状态描述的映射
                for (OrderDistributionStatusVo odv:resultList ) {
                    Integer statusCode = odv.getStatusCode();
                    if ( statusCode != null ) {
                        String msg = null;
                        msg = OrderDistributionStatusEnum.getStatusMsg( statusCode );
                        odv.setStatusMsg(msg);
                    }
                }
            }

            //填充配送状态列表
            orderExpectedDetail.setOrderDistStatusList( resultList );

        }

        String resp = manageCommonService.getRespBody(orderExpectedDetail);

        return resp;
    }

    /**
     * 取消预约单
     * @param orderId
     * @return
     */
    public String cancelExpectedOrderByOrderId(String orderId) {
        //检验订单状态是否存在
        OrderExpected orderExpected = orderExpectedMapper.selectByPrimaryKey(orderId);
        if (orderExpected == null) {
            logger.info(String.format("订单号不存在. orderId: %s;", orderId));
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "订单号不存在!");
        }

        //发起取消订单请求
        int orderType = 2;
        String resultJson = orderHelper.cancelOrderByOrderId(orderId, orderExpected.getMerchantId(), orderType);
        return  resultJson;
    }
}
