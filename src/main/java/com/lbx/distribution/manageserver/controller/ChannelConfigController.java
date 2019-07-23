package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.SimpleRequest;
import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntity;
import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntityVo;
import com.lbx.distribution.manageserver.entity.merchant.MerchantToken;
import com.lbx.distribution.manageserver.helper.CommonHelper;
import com.lbx.distribution.manageserver.service.ChannelConfigService;
import com.lbx.distribution.manageserver.util.ManageException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

@RestController
@Validated
@Api(value = "渠道配置")
@RequestMapping(value="channelConfig")
public class ChannelConfigController {
    private static Logger logger = LoggerFactory.getLogger(ChannelConfigController.class);

    @Autowired
    private ChannelConfigService channelConfigService;
    @Autowired
    private CommonHelper commonHelper;

    @PostMapping(value = "channelConfigList")
    @ApiOperation(value = "获取渠道配置列表", notes = "获取商户/组织机构配置的渠道列表（支持分页）")
    public String channelConfigList(@RequestBody Map<String, Object> request){
        return channelConfigService.getChannelConfigList(request);
    }

    @PostMapping(value = "channelConfigMenu")
    @ApiOperation(value = "获取渠道配置下拉菜单", notes = " 获取门店可用的渠道配置下拉菜单 ")
    public String channelConfigMenu(@RequestBody Map<String, Object> request){
        return channelConfigService.getChannelConfigMenu(request);
    }

    @PostMapping(value = "addChannelConfig")
    @ApiOperation(value = "新增渠道", notes = "商户/组织机构配置新渠道")
    public String addChannelConfig(@RequestBody ChannelConfigEntityVo vo, HttpServletRequest httpServletRequest){
        //日志记录操作
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起新增渠道操作，channelId: %s，merchantId: %s，companyId: %s;",
                    httpServletRequest.getRemoteAddr(),merchantId, user.getMerchantName(), vo.getChannelId(), vo.getMerchantId(), vo.getCompanyId()));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }
        //判断上级机构(商户/公司)是否在启用状态，禁用则不允许新增
        commonHelper.isAvailableHighLevelStatus(vo.getMerchantId(), vo.getCompanyId());

        return channelConfigService.addChannelConfig(vo);
    }

    @PostMapping(value = "updateChannelConfig")
    @ApiOperation(value = "编辑渠道", notes = "商户/组织机构配置新渠道")
    public String updateChannelConfig(@RequestBody ChannelConfigEntity channelConfigEntity, HttpServletRequest httpServletRequest){
        //判断上级机构(商户/公司)是否在启用状态，禁用则不允许编辑
        commonHelper.isAvailableHighLevelStatus(channelConfigEntity.getMerchantId(), channelConfigEntity.getCompanyId());

        //日志记录操作
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起编辑渠道操作，confId: %s;",
                    httpServletRequest.getRemoteAddr(), merchantId, user.getMerchantName(),channelConfigEntity.getConfId()));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }

        return channelConfigService.updateChannelConfig(channelConfigEntity);
    }

    @PostMapping(value = "updateStatus")
    @ApiOperation(value = "更改渠道状态", notes = "更改渠道状态")
    public String updateStatus(@RequestBody ChannelConfigEntity channelConfigEntity, HttpServletRequest httpServletRequest){
        //日志记录操作
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起更改渠道状态操作，confId: %s，status: %s;",
                    httpServletRequest.getRemoteAddr(), merchantId, user.getMerchantName(),channelConfigEntity.getConfId(), channelConfigEntity.getStatus()));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }
        return channelConfigService.updateStatus(channelConfigEntity);
    }

    @PostMapping(value = "deleteChannelConfig")
    @ApiOperation(value = "删除配置渠道", notes = "删除配置渠道")
    public String deleteChannelConfig(@RequestBody  Map<String, Object> request, HttpServletRequest httpServletRequest){
        return channelConfigService.deleteChannelConfig(request, httpServletRequest);
    }

    @PostMapping(value = "addPriceConfig")
    @ApiOperation(value = "新增价格配置", notes = "新增价格配置")
    public String addPriceConfig(@RequestBody SimpleRequest request, HttpServletRequest httpServletRequest){
        return channelConfigService.addPriceConfig(request, httpServletRequest, 1);
    }

    @PostMapping(value = "updatePriceConfig")
    @ApiOperation(value = "更新价格配置", notes = "更新价格配置")
    public String updatePriceConfig(@RequestBody SimpleRequest request, HttpServletRequest httpServletRequest){
        return channelConfigService.updatePriceConfig(request, httpServletRequest);
    }

    @PostMapping(value = "queryPriceConfig")
    @ApiOperation(value = "查询价格配置", notes = "查询城市价格配置")
    public String queryPriceConfig(@RequestBody SimpleRequest request){
        return channelConfigService.queryPriceConfig(request);
    }
}
