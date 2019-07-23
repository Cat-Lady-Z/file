package com.lbx.distribution.manageserver.service;

import com.lbx.distribution.manageserver.mapper.CityMatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配送渠道城市编码匹配
 */
@Service
public class CityMatchService {

    @Autowired
    private CityMatchMapper cityMatchMapper;

    public void cityMatch() {
        List<String> notFountNames = new ArrayList<>();
        List<String> cityNames = cityMatchMapper.queryChannelCityName();
        Map<String, String> params = new HashMap<>();
        for (String cityName : cityNames) {
            System.out.println(cityName + "进入查询");
            String nationalCode = cityMatchMapper.queryCityNationalCodeByName(cityName);
            if (!StringUtils.isEmpty(nationalCode)) {
                params.put("cityName", cityName);
                params.put("nationalCode", nationalCode);
                Integer i = cityMatchMapper.updateMtCityIdByName(params);
                if (i > 0) {
                    System.out.println("城市：" + cityName + "，修改城市id：" + nationalCode);
                } else {
                    System.out.println("更新失败！！！！！！  城市：" + cityName + "，修改城市id：" + nationalCode);
                }
            }else{
                notFountNames.add(cityName);
            }
        }
        StringBuffer sb = new StringBuffer("未找到的城市 : ");
        for (String notFountName : notFountNames) {
            sb.append(notFountName).append(" , ");
        }
        System.out.println(sb.toString());
    }

    /**
     * 根据编码查询省份/城市/区编码
     * @param cityCode
     * @return
     */
    public String queryCityName(String cityCode) {
        String name = "";

        name = this.queryCityNameByCityCode(cityCode);

        if (name == null || "".equals(name) ) {
            name =  this.queryCityNameByProvinceid( cityCode );
        }

        if (name == null || "".equals(name) ) {
            name =  this.queryCityNameByAreaid( cityCode );
        }

        return name;
    }

    /**
     * 根据城市code获取城市名
     * @param cityCode
     * @return
     */
    public String queryCityNameByCityCode(String cityCode) {
        if (cityCode == null || "".equals(cityCode)) {
            return "";
        }
        return cityMatchMapper.queryCityNameByCityCode(cityCode);
    }

    public String queryCityNameByProvinceid(String provinceid) {
        if (provinceid == null || "".equals(provinceid)) {
            return "";
        }
        Integer i = Integer.parseInt(provinceid);
        return cityMatchMapper.queryProvincesNameByPprovinceid( i );
    }


    public String queryCityNameByAreaid(String areaid) {
        if (areaid == null || "".equals(areaid)) {
            return "";
        }
        return cityMatchMapper.queryAreasNameByAreaId(areaid);
    }
}
