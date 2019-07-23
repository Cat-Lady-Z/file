package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.Region;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 省市区
 */
@Mapper
public interface RegionMapper {

    /**
     * 查询省份
     * @return
     */
    List<Region> queryProvince();

    /**
     * 根据省份id查询城市
     * @return
     */
    List<Region> queryCityByProvinceId(String ProvinceId);

    /**
     * 根据城市id查询区县
     * @return
     */
    List<Region> queryAreaByCityId(String cityId);

    /**
     * 根据区县id查询乡镇
     * @return
     */
    List<Region> queryTownByAreaId(String areaId);
}
