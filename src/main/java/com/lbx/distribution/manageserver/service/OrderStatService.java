package com.lbx.distribution.manageserver.service;

import com.alibaba.fastjson.JSONObject;
import com.lbx.distribution.manageserver.common.ChannelDataEnum;
import com.lbx.distribution.manageserver.common.TimeUnitCode;
import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntityVo;
import com.lbx.distribution.manageserver.entity.channel.ChannelConfigMenu;
import com.lbx.distribution.manageserver.entity.order.StatOrder;
import com.lbx.distribution.manageserver.entity.order.StatOrderItem;
import com.lbx.distribution.manageserver.entity.order.StatOrderRequest;
import com.lbx.distribution.manageserver.entity.order.StatOrderVo;
import com.lbx.distribution.manageserver.entity.shop.ShopEntityVo;
import com.lbx.distribution.manageserver.mapper.*;
import com.lbx.distribution.manageserver.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * @ClassName:
 * @Description: 统计订单数量
 * @Created_By: Leo
 */

@Service
public class OrderStatService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderStatMapper orderStatMapper;
    @Autowired
    private StatOrderMapper statOrderMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private ChannelMapper channelMapper;
    @Autowired
    private ChannelConfigMapper channelConfigMapper;
    @Autowired
    private ChannelConfigService channelConfigService;
    @Autowired
    private ManageCommonService manageCommonService;
    @Autowired
    private OrderMapper orderMapper;


    /**
     * 查询订单统计数据
     * @return
     * @Description: 查询订单统计数据
     */
    public String statOrderCount(Integer merchantId) {

        Map<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("merchantId", merchantId);

        Integer totalCount = orderStatMapper.statOrderCount(requestMap);

        requestMap.put("startTime",  DateUtil.getTodayStartTime() );
        requestMap.put("endTime", DateUtil.getTodayEndTime());

        Integer finishOrderCount = orderStatMapper.statFinishOrderCountByDate(requestMap);
        //统计即时单以及已正式发单的预约单
        Integer unfinishOrderCount_tbOrderStatus = orderStatMapper.statUnFinishedOrderCountByDate(requestMap);
        //统计未正式发单的预约单
        Integer unfinishOrderCount_tbOrderExpected = orderStatMapper.statUnFinishedExpectedOrderCount(requestMap);
        Integer unfinishOrderCount = unfinishOrderCount_tbOrderExpected + unfinishOrderCount_tbOrderStatus;

        //统计已取消的即时单以及已正式发单的预约单
        Set<String> cancelOrderId_tbOrderStatus = orderStatMapper.statCancelOrderIdByDate(requestMap);
        //统计未正式发单就取消的预约单
        Set<String> cancelOrderId_tbOrderExpected = orderStatMapper.statCancelExpectedOrderId(requestMap);
        cancelOrderId_tbOrderStatus.addAll(cancelOrderId_tbOrderExpected);
        Integer cancelOrderCount = cancelOrderId_tbOrderStatus.size();

        logger.info(String.format("查询订单统计数据结果--finishOrderCount: %s, unfinishOrderCount: %s , cancelOrderCount: %s;",
                finishOrderCount, unfinishOrderCount,cancelOrderCount ));

        JSONObject resultJson = new JSONObject();
        resultJson.put("total_count", totalCount);
        resultJson.put("finished_count", finishOrderCount );
        resultJson.put("unfinished_count", unfinishOrderCount );
        resultJson.put("canceled_count", cancelOrderCount );

        String resultStr = manageCommonService.getRespBody(resultJson);

        return resultStr;
    }

    /**
     * 按时间段统计订单数量
     * @param request
     * @return
     */
    public String statOrderCountPeriod( Map<String, Object> request ) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>开始按时间段统计订单数量操作>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        long beginTime = new Date().getTime();

        Object channelId1Object = request.get("channelId");
        Object merchantIdObject = request.get("merchantId");
        Object companyIdObject = request.get("companyId");
        Object shopIdObject = request.get("shopId");
        Object cityCodeObject = request.get("cityCode");

        //封装参数
        StatOrderRequest orderRequest = new StatOrderRequest();
        if (channelId1Object != null){
            orderRequest.setChannelId((Integer) channelId1Object);
        }
        if (merchantIdObject != null){
            orderRequest.setMerchantId((Integer) merchantIdObject);
        }
        if (companyIdObject != null){
            orderRequest.setCompanyId((Integer) companyIdObject);
        }
        if (shopIdObject != null){
            orderRequest.setShopId((Integer) shopIdObject);
        }
        if (cityCodeObject != null){
            orderRequest.setCityCode((String)cityCodeObject);
        }

        Object startTimeObject = request.get("startTime");
        Object endTimeObject = request.get("endTime");
        if (startTimeObject == null || "".equals(endTimeObject) || endTimeObject == null || "".equals(endTimeObject)) {
            Date startTime = DateUtil.getTodayStartTime();
            Date endTime = DateUtil.getTodayEndTime();

            orderRequest.setStartTime(startTime);
            orderRequest.setEndTime(endTime);
        } else {
            Long startTimeLong = new Long( String.valueOf( startTimeObject ) );
            Long endTimeLong = new Long( String.valueOf( endTimeObject ) );
            orderRequest.setStartTime(DateUtil.fromUnixTime(startTimeLong));
            orderRequest.setEndTime(DateUtil.fromUnixTime(endTimeLong));
        }

        //当前商户/公司所有的配置渠道列表（包括下层机构有的配置渠道）
        List<Integer> channelIds = this.getAllChannelIds(orderRequest);

        //确认起始时间是同一天(24小时)/同一个月/同一年/不同年份
        Boolean isDay = false;
        Boolean isMonth = false;
        Boolean isYear = false;
        try {
            isDay = DateUtil.isDay(orderRequest.getStartTime(), orderRequest.getEndTime());
            isMonth = DateUtil.isMonth( orderRequest.getStartTime(), orderRequest.getEndTime() );
            isYear = DateUtil.isYear(orderRequest.getStartTime(), orderRequest.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //做x轴时间点的封装
        List<String> time = null;
        try {
            time = DateUtil.getTimeXAxis(orderRequest.getStartTime(), orderRequest.getEndTime());
        } catch (ParseException e) {
            logger.error("获取x轴时间点出现异常！");
            e.printStackTrace();
        }

        List<StatOrder> totalStatOrders = null;

        Map<Integer, Object> statOrderMap = new HashMap<>();
        StatOrderVo result = new StatOrderVo();
        if ( isDay ) {

            //如果起始时间是一天就按照24小时维度返回
            totalStatOrders = statOrderMapper.statOrderCountByHour(orderRequest);
            statOrderMap.put( ChannelDataEnum.TOTAL__CODE, totalStatOrders);

            for ( Integer channelId:channelIds ) {
                orderRequest.setChannelId(channelId);
                List<StatOrder> statOrders = statOrderMapper.statOrderCountByHour(orderRequest);

                statOrderMap.put(channelId, statOrders);
            }

        } else if ( isMonth ) {
            totalStatOrders = statOrderMapper.statOrderCountByDay(orderRequest);
            statOrderMap.put( ChannelDataEnum.TOTAL__CODE , totalStatOrders);

            for ( Integer channelId:channelIds ) {
                orderRequest.setChannelId(channelId);
                List<StatOrder> statOrders = statOrderMapper.statOrderCountByDay(orderRequest);

                statOrderMap.put(channelId, statOrders);
            }

        } else if ( isYear ) {

            totalStatOrders = statOrderMapper.statOrderCountByMonth(orderRequest);
            statOrderMap.put( ChannelDataEnum.TOTAL__CODE , totalStatOrders);

            for ( Integer channelId:channelIds ) {
                orderRequest.setChannelId(channelId);
                List<StatOrder> statOrders = statOrderMapper.statOrderCountByMonth(orderRequest);

                statOrderMap.put(channelId, statOrders);
            }

        } else {

            totalStatOrders = statOrderMapper.statOrderCountByYear(orderRequest);
            statOrderMap.put( ChannelDataEnum.TOTAL__CODE , totalStatOrders);

            for ( Integer channelId:channelIds ) {
                orderRequest.setChannelId(channelId);
                List<StatOrder> statOrders = statOrderMapper.statOrderCountByYear(orderRequest);

                statOrderMap.put(channelId, statOrders);
            }
        }

        //返回参数的封装
        result = this.setResult(result, time, statOrderMap);

        //对时间点列表的处理
        time = this.setTime(time, isDay, isMonth, isYear);

        result.setTime(time);

        String resultStr = manageCommonService.getRespBody(result);
        long endTime = new Date().getTime();
        logger.info(">>>>>>>>>>>>>>>>>>>>>结束执行按时间段统计订单数量操作，执行时间："+ (endTime - beginTime)  +"毫秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        return resultStr;
    }

    /**
     * 对时间点列表的显示处理
     * @param time
     * @param isDay
     * @param isMonth
     * @param isYear
     * @return
     */
    private List<String> setTime(List<String> time, Boolean isDay, Boolean isMonth, Boolean isYear) {
        if (time == null)
            return null;

        List<String> result = new ArrayList<>();
        try {
            if (isDay) {
                //单位：小时，格式：01:00
                for (String t:time ) {
                    Date date = DateUtil.getDateByString(t, TimeUnitCode.HOUR);
                    int hours = date.getHours();
                    t = hours + ":00";
                    result.add(t);
                }

            } else if (isMonth) {
                //单位：天，格式：6月5号
                for (String t:time ) {
                    Date date = DateUtil.getDateByString(t, TimeUnitCode.DAY);
                    int month = date.getMonth()+1;
                    int day = date.getDate();
                    t = month + "月" + day + "号";
                    result.add(t);
                }
            } else if (isYear) {
                //单位：月， 格式：6月
                for (String t:time ) {
                    Date date = DateUtil.getDateByString(t, TimeUnitCode.MONTH);
                    int month = date.getMonth()+1;
                    t = month + "月";
                    result.add(t);
                }

            } else {
                //单位：年，格式：2019年
                for (String t:time ) {
                    Date date = DateUtil.getDateByString(t, TimeUnitCode.YEAR);
                    int year = date.getYear() + 1900;
                    t = year + "年";
                    result.add(t);
                }
            }
        } catch (ParseException e) {
            logger.error("对时间点列表的显示处理出现异常！");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取所有的渠道(结果去重)
     * @param request
     * @return
     */
    private List<Integer> getAllChannelIds(StatOrderRequest request) {
        List<Integer> resultIds = new ArrayList<>();
        List<Integer> totalChannelIds = new ArrayList<>();
        List<Integer> merchantChannelIds = new ArrayList<>();
        List<Integer> companyChannelIds = new ArrayList<>();

        //封装参数
        Integer companyId = request.getCompanyId();
        Integer merchantId = request.getMerchantId();
        Integer shopId = request.getShopId();

        if ( companyId == null && merchantId == null && shopId == null){
            return channelMapper.queryChannelIds();
        }

        if (merchantId != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("merchantId", merchantId);
            merchantChannelIds = channelConfigMapper.queryChannelIds(params);

            List<Integer> companyIds = this.querySonCompanyIdList(params);
            params.remove("merchantId");
            if ( companyIds != null && companyIds.size() != 0) {
                if (companyIds.size() > 1) {
                    params.put("companyIds", companyIds);
                } else {
                    params.put("companyId", companyIds.get(0));
                }

                companyChannelIds = channelConfigMapper.queryChannelIds(params);
            }
        } else  if ( companyId != null && companyId != 0 ) {
            Map<String, Object> params = new HashMap<>();
            //获取组织机构所有下层的公司id
            params.put("companyId", companyId);

            List<Integer> companyIds = this.querySonCompanyIdList(params);
            if ( companyIds != null && companyIds.size() > 1 ) {
                companyIds.add(companyId);

                request.setCompanyIds(companyIds);
                request.setCompanyId(null);
                params.put("companyIds", companyIds);
                params.remove("companyId");
            }
            //获取公司及下层机构的渠道列表
            companyChannelIds = channelConfigMapper.queryChannelIds(params);
        } else if (shopId != null && shopId != 0) {
            //门店订单（商户 + 上级公司 所有渠道）
            ShopEntityVo shop = shopMapper.queryShopByShopId(shopId);
            Integer shopMerchantId = shop.getMerchantId();
            Integer shopCompanyId = shop.getCompanyId();

            Map<String, Object> params = new HashMap<>();
            params.put("merchantId", shopMerchantId);
            merchantChannelIds = channelConfigMapper.queryChannelIds(params);

            params.remove("merchantId");
            //获取上层公司所有渠道
            List<ChannelConfigEntityVo> companyChannelList = channelConfigService.getChannelConfigListByCompanyIdFromDown(shopCompanyId);
            if (companyChannelList !=null && companyChannelList.size()!=0) {
                for (ChannelConfigEntityVo channelConfig:companyChannelList  ) {
                    companyChannelIds.add(channelConfig.getChannelId());
                }
            }
        }

        if (companyChannelIds != null) {
            totalChannelIds.addAll(companyChannelIds);
        }
        if (merchantChannelIds != null) {
            totalChannelIds.addAll(merchantChannelIds);
        }

        //去重
        if (totalChannelIds != null) {
            Set<Integer> tempSet = new HashSet<>();
            for (Integer id:totalChannelIds ) {
                if ( !tempSet.contains(id)) {
                    tempSet.add(id);
                    resultIds.add(id);
                }
            }
        }

        return resultIds;
    }

    /**
     * 根据商户id/公司id查询下层所有公司的Id
     * @param idMap
     * @return
     */
    private List<Integer> querySonCompanyIdList(Map<String, Object> idMap) {
        List<Integer> companyIds = new ArrayList<>();

        Object merchantIdObject = idMap.get("merchantId");
        Object companyIdObject = idMap.get("companyId");
        if ( merchantIdObject != null ) {
            Integer merchantId = (Integer) merchantIdObject;
            companyIds = companyMapper.queryCompanyIdsByMerchantId(merchantId );
        } else if ( companyIdObject != null ) {
            Integer companyId = (Integer) companyIdObject;
            companyIds = this.querySonCompanyIdListByCompanyId( companyId );
        }

        return companyIds;
    }

    /**
     * 根据公司id查询下层机构id
     * @param companyId
     * @return
     */
    private List<Integer> querySonCompanyIdListByCompanyId(Integer companyId) {
        List<Integer> companyIds = new ArrayList<>();

        List<Integer> ids = companyMapper.findCompanyIdsByParentId(companyId);
        if ( ids == null || ids.size()==0 ) {
            return null;
        }

        for (Integer id :ids ) {
            List<Integer> sonIds = this.querySonCompanyIdListByCompanyId(id);
            if (sonIds != null && ids.size()==0) {
                companyIds.addAll(sonIds);
            }
        }

        return companyIds;
    }

    /**
     * 返回参数的封装
     * @param result
     * @param time
     * @return
     */
    private StatOrderVo setResult(StatOrderVo result, List<String> time, Map<Integer, Object> statOrderMap) {
        List<StatOrderItem> dataList = new ArrayList<>();

        //全部订单统计
        Object totalObject = statOrderMap.get(ChannelDataEnum.TOTAL__CODE);
        if (totalObject!=null) {
            List<StatOrder> totalStatOrders = (List<StatOrder>) totalObject;
            StatOrderItem total = new StatOrderItem();
            total.setChannelName(ChannelDataEnum.TOTAL_NAME);
            total.setTxt(ChannelDataEnum.TOTAL_TXT);
            total.setColor(ChannelDataEnum.TOTAL_COLOR);
            total = this.getStatOrderItemData(time, total, totalStatOrders);
            dataList.add(total);
        }

        Object dadaObject = statOrderMap.get(ChannelDataEnum.DADA__CODE);
        if (dadaObject!= null) {
            //达达订单统计
            List<StatOrder> dadaStatOrders = (List<StatOrder>) dadaObject;
            StatOrderItem dada = new StatOrderItem();
            dada.setChannelName(ChannelDataEnum.DADA__NAME);
            dada.setTxt(ChannelDataEnum.DADA__TXT);
            dada.setColor(ChannelDataEnum.DADA__COLOR);
            dada = this.getStatOrderItemData(time, dada, dadaStatOrders);
            dataList.add(dada);
        }

        Object hbirdObject = statOrderMap.get(ChannelDataEnum.HUMMER_BIRD__CODE);
        if ( hbirdObject != null ){
            //蜂鸟订单统计
            List<StatOrder> hbirdStatOrders = (List<StatOrder>) hbirdObject;
            StatOrderItem hBird = new StatOrderItem();
            hBird.setChannelName(ChannelDataEnum.HUMMER_BIRD__NAME);
            hBird.setTxt(ChannelDataEnum.HUMMER_BIRD__TXT);
            hBird.setColor(ChannelDataEnum.HUMMER_BIRD__COLOR);
            hBird = this.getStatOrderItemData(time, hBird, hbirdStatOrders);
            dataList.add(hBird);
        }


        Object meituanObject = statOrderMap.get(ChannelDataEnum.MEITUAN_CODE);
        if ( meituanObject != null ){
            //美团订单统计
            List<StatOrder> meituantatOrders = (List<StatOrder>) meituanObject;
            StatOrderItem meituan = new StatOrderItem();
            meituan.setChannelName(ChannelDataEnum.MEITUAN_NAME);
            meituan.setTxt(ChannelDataEnum.MEITUAN_TXT);
            meituan.setColor(ChannelDataEnum.MEITUAN_COLOR);
            meituan = this.getStatOrderItemData(time, meituan, meituantatOrders);
            dataList.add(meituan);
        }

        result.setDataList(dataList);

        return  result;
    }

    private StatOrderItem getStatOrderItemData(List<String> time, StatOrderItem statOrderItem,List<StatOrder> statOrders) {

        Map<String, Object> statOrdersMap = new HashMap<>();
        if (statOrders != null ){
            for (StatOrder so:statOrders ) {
                statOrdersMap.put(so.getCreateTime(), so);
            }

            //封装时间点对应的订单数
            List<Integer> data = new ArrayList<>();
            for (String t:time ) {
                Object o = statOrdersMap.get(t);
                if ( o != null && o instanceof StatOrder) {
                    StatOrder statOrder = (StatOrder)o;
                    Integer orderNum = statOrder.getOrderNum();
                    data.add(orderNum);
                }  else {
                    data.add(0);
                }
            }
            statOrderItem.setData(data);
        }

        return statOrderItem;
    }
}
