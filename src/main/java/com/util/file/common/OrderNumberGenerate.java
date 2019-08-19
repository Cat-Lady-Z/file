package com.util.file.common;


import com.util.file.util.RandomUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单号生产
 */
public class OrderNumberGenerate {

    /**
     * 生产订单号
     * 订单号规则：yyMMddHHmmss + 门店code + 4位随机码
     * @return
     */
    public static String generateOrderNum(String code){
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        String orderNum = "";
        try{
            orderNum = sdf.format(new Date()) + code + RandomUtils.getInstance().generateValue(1000, 10000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return orderNum;
    }

}
