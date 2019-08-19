package com.util.file.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.util.file.common.BaseCommon;

import java.util.Map;

/**
 * 参数转换方法
 */
public class ConversionParam implements BaseCommon {

    /**
     * Obj转Map
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String,String> Obj2Map(Object obj) throws Exception{
        /*Map<String,String> map=export HashMap<String, String>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj).toString());
        }*/
        JSONObject json = (JSONObject)JSONObject.toJSON(obj);
        Map<String, String> map = JSONObject.parseObject(json.toJSONString(), new TypeReference<Map<String, String>>(){});
        return map;

    }
}
