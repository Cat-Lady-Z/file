package com.lbx.distribution.manageserver.common;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName:
 * @Description: //
 */
public class OrderExcelTitle {

    public static final String titleString = "平台订单号,商户订单号,运单号,订单类型,商户,公司,发货门店,创建日期,配送渠道,订单状态,配送价格,基础价,距离加价,时间加价,重量加价,配送距离,订单来源,省份,城市,县区,门店地址,发件人电话,顾客名称,顾客地址,顾客电话,骑手姓名,骑手电话,货品名称,货品单价,数量,金额,备注";

    public static final String titleKeyString = "orderId,originId,channelDeliveryId,orderTypeName,enterpriseName,companyName,shopName,createTime,channelName,statusMsg,predict_price,distance,originSourceName,provinceName,cityName,areaName,address,contactPhone,receiverName,receiverAddress,receiverPhone,courierName,courierPhone,itemName,itemPrice,itemQuantity,money,info";

    public static List<String> getTitleList(){
        List<String> titleList = Arrays.asList(titleString.split(","));
        return titleList;
    }

    public static List<String> getTitleKey(){
        List<String> titleKey = Arrays.asList(titleKeyString.split(","));
        return titleKey;
    }

}
