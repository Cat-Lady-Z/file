package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.entity.Region;
import com.lbx.distribution.manageserver.service.RegionService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@Api(value = "城市列表")
@RequestMapping(value="region")
public class RegionController {

    @Autowired
    private RegionService regionService;

    /**
     * 查询区域信息
     */
    @RequestMapping(value = "/queryRegion")
    public String queryRegion(@RequestBody Region region){
        return regionService.queryRegion(region);
    }
}
