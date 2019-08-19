/*
package com.util.file.service;

import com.util.file.util.DateUtil;
import com.util.file.util.ManageException;
import com.util.file.util.excel.excel.ExportExcelUtil;
import com.util.file.common.ManageResultCode;
import com.util.file.common.OrderExcelTitle;
import com.util.file.common.OrderStatusEnum;
import com.util.file.common.ShopExportExcelTitle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

*/
/**
 * 数据导入
 *//*

@Service
public class DataExportService {

    private static Logger logger = LoggerFactory.getLogger(DataExportService.class);

    @Autowired
    private OrderCommonService orderCommonService;
   @Autowired
   private ShopMapper shopMapper;

    */
/**
     *
     * @param params
     * @param httpServletRequest
     * @return
     *//*

    private Map<String, Object> setParams(Map<String, Object> params, HttpServletRequest httpServletRequest) {
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer role = user.getRole();
            //如果不是超级管理员就筛选本商户下的订单
            if (role != 0) {
                params.put("merchantId", user.getMerchantId());
            }
        }

        params.put("orderType", httpServletRequest.getParameter("orderType"));
        params.put("originId", httpServletRequest.getParameter("originId"));
        params.put("sourceMerchantId", httpServletRequest.getParameter(  "sourceMerchantId"));
        params.put("sourceType", httpServletRequest.getParameter("sourceType"));
        params.put("cityCode", httpServletRequest.getParameter("cityCode"));
        params.put("channelId", httpServletRequest.getParameter("channelId"));
        params.put("merchantId", httpServletRequest.getParameter("merchantId"));
        params.put("companyId", httpServletRequest.getParameter("companyId"));
        params.put("shopId", httpServletRequest.getParameter("shopId"));
        String statusStr = httpServletRequest.getParameter("status");
        if (statusStr != null && !"".equals(statusStr)) {
            Integer status = Integer.valueOf(statusStr);
            params.put("status", status);
        } else {
            params.put("status", null);
        }
        params.put("originId", httpServletRequest.getParameter("originId"));
        params.put("startTime", httpServletRequest.getParameter("startTime"));
        params.put("endTime", httpServletRequest.getParameter("endTime"));
        params.put("distributionOrderId" , httpServletRequest.getParameter("distributionOrderId"));
        params.put("channelPeisongId" , httpServletRequest.getParameter("channelPeisongId"));
        return params;
    }

    */
/**
     * 导出门店信息
     * @param
     *//*

    public void exportShop(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        //封装参数
        Map<String, Object> params = new HashMap<>();
        String merchantId = httpServletRequest.getParameter("merchantId");
        if (merchantId == null || "".equals(merchantId)) {
            logger.info("请求参数错误。无商户id!");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误。无商户id!");
        }
        params.put("merchantId",merchantId);

        logger.info(">>>>>>>>>>>>>>>>>>>>>开始执行导出门店操作>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        long beginTime = new Date().getTime();

        List<ShopExport> shopList = shopMapper.queryShopExcelList(params);
        if ( shopList == null ) {
            logger.info("无门店信息!");
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "无门店信息!");
        }

        //excel标题
        List<String> title = ShopExportExcelTitle.getTitleList();

        //sheet名
        String sheetName = "导出门店表";

        //shop_id,shop_name,origin_shop_id,create_time
        List<List<String>> content = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < shopList.size(); i++) {
            ShopExport shop = shopList.get(i);
            List<String> item = new ArrayList<>();
            item.add(shop.getOriginShopId()+ "");
            item.add(shop.getShopId()+ "");
            item.add(shop.getShopName()+ "");
            Date createTime = shop.getCreateTime();
            String createTimeFormat = sdf.format(createTime);
            item.add( createTimeFormat );
            content.add(item);
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExportExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //excel文件名
        String fileName = null;
        try {
            fileName = "导出门店表 "+ DateUtil.getCurrentDateString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ExportExcelUtil.exportExcel(fileName, response, wb, httpServletRequest);

        long endTime = new Date().getTime();
        logger.info(">>>>>>>>>>>>>>>>>>>>>结束执行导出门店操作，执行时间："+ (endTime - beginTime)  +"毫秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    */
/**
     * 导出门店导入模型
     * @param response
     *//*

    public void exportShopModle(HttpServletResponse response, HttpServletRequest httpServletRequest) {
        String fileName = "门店导入模板.xlsx";
        String filePath = "/excel/";

        InputStream stream = getClass()
                .getClassLoader()
                .getResourceAsStream(filePath + fileName);

        ExportExcelUtil.downloadInstream(stream, fileName, response, httpServletRequest);
    }

    */
/**
     * 导出公司模板
     * @param response
     *//*

    public void exportCompanyModle(HttpServletResponse response, HttpServletRequest httpServletRequest) {
        String fileName = "公司导入模板.xlsx";
        String filePath = "excel/";

        //打包成jar以后，实际上文件是存在于jar这个文件里面的资源文件，在磁盘是没有真实路径的。采用流的方式进行处理
        InputStream stream = getClass()
                .getClassLoader()
                .getResourceAsStream(filePath + fileName);

        ExportExcelUtil.downloadInstream(stream, fileName, response, httpServletRequest);
    }

    */
/**
     * 导出订单表
     * @param httpServletRequest
     * @param response
     *//*

    public void exportOrder(HttpServletRequest httpServletRequest, HttpServletResponse response) {
        //封装参数
        Map<String, Object> params = new HashMap<>();
        params = this.setParams(params, httpServletRequest);

        logger.info(">>>>>>>>>>>>>>>>>>>>>开始执行导出订单操作>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        long beginTime = new Date().getTime();

        List<OrderExcelListItem> orderExcelList = orderCommonService.queryOrderExcelList( params );

        if ( orderExcelList == null ) {
            logger.info("无相关订单信息，请重新选择筛选条件!");
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "无相关订单信息，请重新选择筛选条件!");
        }

        //excel标题
        List<String> title = OrderExcelTitle.getTitleList2();

        //sheet名
        String sheetName = "订单表";

        List<List<String>> content = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < orderExcelList.size(); i++) {
            OrderExcelListItem order = orderExcelList.get(i);
            List<OrderExcelCargoItem> orderCargoItems = order.getOrderCargoItems();
            List<String> item = new ArrayList<>();
            item.add(order.getOrderId()+ "");
            item.add(order.getOriginId()+ "");
            item.add(order.getChannelPeisongId()+ "");
            item.add(order.getOrderTypeName() +"");
            item.add(order.getEnterpriseName()+ "");
            item.add(order.getCompanyName()+ "");
            item.add(order.getShopName()+ "");
            Date createTime = order.getCreateTime();
            String createTimeFormat = sdf.format(createTime);
            item.add( createTimeFormat );
            item.add(order.getChannelName()+ "");
            item.add(order.getStatusMsg()+ "");
            BigDecimal predictPrice = order.getPredictPrice();
            if (predictPrice != null) {
                item.add(predictPrice+ "");
            } else {
                item.add("-");
            }
            BigDecimal basePrice = order.getBasePrice();
            if (basePrice != null) {
                item.add(basePrice+ "");
            } else {
                item.add("-");
            }
            BigDecimal addDistancePrice = order.getAddDistancePrice();
            if (addDistancePrice != null) {
                item.add(addDistancePrice+ "");
            } else {
                item.add("-");
            }
            BigDecimal addTimePrice = order.getAddTimePrice();
            if (addTimePrice != null) {
                item.add(addTimePrice+ "");
            } else {
                item.add("-");
            }
            BigDecimal addWeightPrice = order.getAddWeightPrice();
            if (addWeightPrice != null) {
                item.add(addWeightPrice+ "");
            } else {
                item.add("-");
            }
            item.add(order.getDistance()+ "");
            item.add(order.getOriginSourceName()+"");
            item.add(order.getProvinceName()+ "");
            item.add(order.getCityName()+ "");
            item.add(order.getAreaName()+ "");
            item.add(order.getAddress()+ "");
            item.add(order.getContactPhone()+ "");
            item.add(order.getReceiverName()+ "");
            item.add(order.getReceiverAddress()+ "");
            item.add(order.getReceiverPhone()+ "");
            item.add(order.getCourierName()+ "");
            item.add(order.getCourierPhone()+ "");
            //货物拼接
            if (orderCargoItems == null) {
                item.add("");
            } else {
                StringBuffer cargoStringBuffer = new StringBuffer();
                for (int j = 0; j < orderCargoItems.size(); j++) {
                    OrderExcelCargoItem cargo = orderCargoItems.get(j);
                    cargoStringBuffer.append(cargo.getItemName() + "*" + cargo.getItemQuantity());
                    if (j != (orderCargoItems.size() -1)) {
                        cargoStringBuffer.append(",");
                    }
                }
                String cargoString = cargoStringBuffer.toString();
                item.add(cargoString);
            }
            item.add(order.getInfo()+ "");
            //状态及对应时间
            item = this.addStatusList(order, item);
            content.add(item);
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExportExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //excel文件名
        String fileName = null;
        try {
            fileName = "订单表 "+ DateUtil.getCurrentDateString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ExportExcelUtil.exportExcel(fileName, response, wb, httpServletRequest);

        long endTime = new Date().getTime();
        logger.info(">>>>>>>>>>>>>>>>>>>>>结束执行导出订单操作，执行时间："+ (endTime - beginTime)  +"毫秒>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    */
/**
     * 订单状态列表填充
     * 导出签名：OrderId+channelId+statusCode
     *//*

    private List<String> addStatusList(OrderExcelListItem order, List<String> item) {
        Map<Integer, OrderDistributionStatusVo> statusVoMap = order.getOrderDistributionStatusVoMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (statusVoMap == null) {
            return item;
        }

        //导出固定状态：门店发单,骑手接单,骑手取货,完成,已取消,系统异常
        OrderDistributionStatusVo statusVo_placed = statusVoMap.get(OrderStatusEnum.WAIT_ORDER_CODE);
        if (statusVo_placed != null) {
            Date createTime = statusVo_placed.getCreateTime();
            String createTimeFormat = sdf.format(createTime);
            item.add(createTimeFormat);
        } else {
            item.add("-");
        }
        //骑手接单
        OrderDistributionStatusVo statusVo_order = statusVoMap.get(OrderStatusEnum.WAIT_DELIVERY_CODE);
        if (statusVo_order != null ) {
            Date createTime = statusVo_order.getCreateTime();;
            String createTimeFormat = sdf.format(createTime);
            item.add(createTimeFormat);
        } else {
            item.add("-");
        }
        //骑手取货
        OrderDistributionStatusVo statusVo_delivery = statusVoMap.get(OrderStatusEnum.DISTRIBUTION_CODE);
        if (statusVo_delivery != null ) {
            Date createTime = statusVo_delivery.getCreateTime();;
            String createTimeFormat = sdf.format(createTime);
            item.add(createTimeFormat);
        } else {
            item.add("-");
        }
        //完成
        OrderDistributionStatusVo statusVo_finish = statusVoMap.get(OrderStatusEnum.FINISH_CODE);
        if (statusVo_finish != null ) {
            Date createTime = statusVo_finish.getCreateTime();;
            String createTimeFormat = sdf.format(createTime);
            item.add(createTimeFormat);
        } else {
            item.add("-");
        }
        //取消
        OrderDistributionStatusVo statusVo_cancel = statusVoMap.get(OrderStatusEnum.CANCEL_CODE);
        if (statusVo_cancel != null ) {
            Date createTime = statusVo_cancel.getCreateTime();;
            String createTimeFormat = sdf.format(createTime);
            item.add(createTimeFormat);
        } else {
            item.add("-");
        }
        //系统异常
        OrderDistributionStatusVo statusVo_exception = statusVoMap.get(OrderStatusEnum.SYS_EXCEPTION_CODE);
        if (statusVo_exception != null ) {
            Date createTime = statusVo_exception.getCreateTime();;
            String createTimeFormat = sdf.format(createTime);
            item.add(createTimeFormat);
        } else {
            item.add("-");
        }

        return item;
    }
}
*/
