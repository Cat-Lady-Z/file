package com.lbx.distribution.manageserver.common;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName:
 * @Description: //
 */
public class OrderExcelTitle {

    public static final String titleString = "平台订单号,商户订单号,运单号,订单类型,商户,公司,发货门店,创建日期,配送渠道,订单状态,配送价格,基础价,距离加价,时间加价,重量加价,配送距离,订单来源,省份,城市,县区,门店地址,发件人电话,顾客名称,顾客地址,顾客电话,骑手姓名,骑手电话,货品名称,货品单价,数量,金额,备注";

    public static final String titleString2 = "平台订单号,商户订单号,运单号,订单类型,商户,公司,发货门店,创建日期,配送渠道,订单当前状态,配送价格,基础价,距离加价,时间加价,重量加价,配送距离,订单来源,省份,城市,县区,门店地址,发件人电话,顾客名称,顾客地址,顾客电话,骑手姓名,骑手电话,货品明细,备注,门店发单,骑手接单,骑手取货,骑手送达,已取消,系统异常";

    //    oe.order_id, oe.origin_id,tod.channel_peisong_id ,oe.order_type, mer.enterprise_name, com.company_name,shop.shop_name,
    //    os.create_time,tc.channel_name, source.source_desc as origin_source_name,
    //    oe.distance,
    //    os.distribute_status,  os.update_time,
    //    tod.status_code, tod.create_time as status_create_time
    public static final String titleStringTemp = "平台订单号,商户订单号,运单号,订单类型,商户,公司,发货门店,创建日期,配送渠道,订单来源,配送距离,订单状态,状态时间";

    public static final String titleKeyString = "orderId,originId,channelPeisongId,orderTypeName,enterpriseName,companyName,shopName,createTime,channelName,statusMsg,predict_price,distance,originSourceName,provinceName,cityName,areaName,address,contactPhone,receiverName,receiverAddress,receiverPhone,courierName,courierPhone,itemName,itemPrice,itemQuantity,money,info";

   /* public static List<String> getTitleList(){
        List<String> titleList = Arrays.asList(titleString.split(","));
        return titleList;
    }*/

    public static List<String> getTitleKey(){
        List<String> titleKey = Arrays.asList(titleKeyString.split(","));
        return titleKey;
    }

    public static List<String> getTitleListTemp(){
        List<String> titleList = Arrays.asList(titleStringTemp.split(","));
        return titleList;
    }

    public static List<String> getTitleList2(){
        List<String> titleList = Arrays.asList(titleString2.split(","));
        return titleList;
    }
}
