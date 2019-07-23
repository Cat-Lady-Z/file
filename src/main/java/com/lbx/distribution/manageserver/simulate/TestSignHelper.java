package com.lbx.distribution.manageserver.simulate;

import com.alibaba.fastjson.JSONObject;
import com.lbx.distribution.manageserver.util.MD5Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 签名计算工具类
 */
public class TestSignHelper {

    /**
     * 签名方法
     * @return
     */
    public static String generateSign(JSONObject jsonObj,String secretKey){
        StringBuffer paramBuffer = new StringBuffer();
        Set<String> keySet = jsonObj.keySet();
        List<String> keyList = new ArrayList<>(keySet);
        //参数按字典排序
        Collections.sort(keyList);
        //参数key value拼接,前后加上secret
        paramBuffer.append(secretKey);
        for(String key : keyList){
            paramBuffer.append(key).append(jsonObj.get(key));
        }
        paramBuffer.append(secretKey);
        String paramStr = paramBuffer.toString();
        //MD5加密,并转成大写
        String sign = MD5Utils.getMD5Code(paramStr).toUpperCase();
        return sign;
    }

    public static String generateCallBackSign(String clientID,String orderID,Integer updateTime){
        List<String> valueList = new ArrayList<>();
        valueList.add(clientID);
        valueList.add(orderID);
        valueList.add(String.valueOf(updateTime));
        Collections.sort(valueList);
        StringBuffer paramBuffer = new StringBuffer();
        for(String value : valueList){
            paramBuffer.append(value);
        }
        String paramStr = paramBuffer.toString();
        //MD5加密,并转成大写
        String sign = MD5Utils.getMD5Code(paramStr);
        return sign;
    }
}
