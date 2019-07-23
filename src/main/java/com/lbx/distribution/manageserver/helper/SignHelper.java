package com.lbx.distribution.manageserver.helper;

import com.alibaba.fastjson.JSONObject;
import com.lbx.distribution.manageserver.common.BaseCommon;
import com.lbx.distribution.manageserver.util.MD5Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 分发系统签名计算工具类
 */
public class SignHelper implements BaseCommon {

    /**
     * 签名方法
     * @return
     */
    public static String generateSign(JSONObject jsonObj, String secretKey){
        StringBuffer paramBuffer = new StringBuffer();
        Set<String> keySet = jsonObj.keySet();
        List<String> keyList = new ArrayList<>(keySet);
        //参数按字典排序
        Collections.sort(keyList);
        //参数key value拼接,首尾加上secret
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

}
