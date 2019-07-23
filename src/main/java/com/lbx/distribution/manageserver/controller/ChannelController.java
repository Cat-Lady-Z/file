package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.service.ChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Validated
@Api(value = "渠道管理")
@RequestMapping(value="channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @PostMapping(value = "channelList")
    @ApiOperation(value = "获取渠道列表", notes = "获取渠道列表（支持分页）")
    public String channelConfigList(@RequestBody Map<String, Object> request){
        return channelService.getChannelList(request);
    }

    @PostMapping(value = "availableChannelList")
    @ApiOperation(value = "获取可配置的渠道列表", notes = "获取可配置的渠道列表")
    public String availableChannelList(@RequestBody Map<String, Object> request){
        return channelService.availableChannelList(request);
    }
}
