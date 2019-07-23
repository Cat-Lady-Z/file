package com.lbx.distribution.manageserver.service;

import com.lbx.distribution.manageserver.common.RegionEnum;
import com.lbx.distribution.manageserver.entity.Region;
import com.lbx.distribution.manageserver.mapper.RegionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 省市区
 */
@Service
public class RegionService{

    @Autowired
    private RegionMapper regionMapper;
    @Autowired
    private ManageCommonService manageCommonService;


    /**
     * 查询区域
     * @return
     */
    public String queryRegion(Region regionParam){
        List<Region> regionList = new ArrayList<>();
        String regionLevel = regionParam.getRegionLevel();
        if(StringUtils.equals(RegionEnum.PROVINCE_LEVEL,regionLevel)){
            regionList = regionMapper.queryProvince();
        }else if(StringUtils.equals(RegionEnum.CITY_LEVEL,regionLevel)){
            regionList = regionMapper.queryCityByProvinceId(regionParam.getRegionCode());
        }else if(StringUtils.equals(RegionEnum.AREA_LEVEL,regionLevel)){
            regionList = regionMapper.queryAreaByCityId(regionParam.getRegionCode());
        }else if(StringUtils.equals(RegionEnum.TOWN_LEVEL,regionLevel)){
            regionList = regionMapper.queryTownByAreaId(regionParam.getRegionCode());
        }
        String resp = manageCommonService.getRespBody(regionList);
        return resp;
    }

}
