package com.lbx.distribution.manageserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 渠道匹配国标码
 */
@Mapper
public interface CityMatchMapper {

    /**
     * 查询国家编码
     * @return
     */
    String queryCityNationalCodeByName(@Param("cityName") String cityName);

    /**
     * 查询渠道城市名称
     * @return
     */
    List<String> queryChannelCityName();

    /**
     * 更新城市id
     * @param params
     * @return
     */
    Integer updateMtCityIdByName(Map<String, String> params);

    /**
     * 根据城市id查询城市名
     * @param cityCode
     * @return
     */
    String queryCityNameByCityCode(@Param("cityCode")String cityCode);

    /**
     * 根据id查询区域名
     * @param areaId
     * @return
     */
    String queryAreasNameByAreaId(@Param("areaId") String areaId);

    /**
     * 根据id查询省份名
     * @param provinceId
     * @return
     */
    String queryProvincesNameByPprovinceid(@Param("provinceId") Integer provinceId);
}
