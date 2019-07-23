package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.channel.DistributionConfigVo;
import com.lbx.distribution.manageserver.entity.merchant.MerchantToken;
import com.lbx.distribution.manageserver.helper.CommonHelper;
import com.lbx.distribution.manageserver.service.DistributeConfigService;
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
import java.util.Map;

@RestController
@Validated
@Api(value = "分发设置")
@RequestMapping(value="distributeConfig")
public class DistributeConfigController {
    private static Logger logger = LoggerFactory.getLogger(DistributeConfigController.class);

    @Autowired
    private DistributeConfigService distributeConfigService;
    @Autowired
    private CommonHelper commonHelper;

    @PostMapping(value = "distributeConfigList")
    @ApiOperation(value = "获取分发规则列表", notes = "获取分发规则列表")
    public String distributeConfigList(@RequestBody Map<String, Object> request){
        return distributeConfigService.getDistrConfigList(request);
    }

    @PostMapping(value = "addDistributeConfig")
    @ApiOperation(value = "新增分发规则", notes = "新增分发规则")
    public String addDistributeConfig(@RequestBody DistributionConfigVo distributionConfigVo, HttpServletRequest httpServletRequest){
        //判断上级机构(商户/公司)是否在启用状态，禁用则不允许新增
        commonHelper.isAvailableHighLevelStatus(distributionConfigVo.getMerchantId(), distributionConfigVo.getCompanyId());

        //日志记录操作
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起新增分发规则操作，merchantId: %s，companyId: %s;",
                    httpServletRequest.getRemoteAddr(), merchantId, user.getMerchantName(),distributionConfigVo.getMerchantId(), distributionConfigVo.getCompanyId()));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }

        return distributeConfigService.addDistributeConfig(distributionConfigVo);
    }

    @PostMapping(value = "editDistributeConfig")
    @ApiOperation(value = "编辑分发规则", notes = "编辑分发规则")
    public String editDistributeConfig(@RequestBody DistributionConfigVo distributionConfigVo, HttpServletRequest httpServletRequest){
        //判断上级机构(商户/公司)是否在启用状态，禁用则不允许新增
        commonHelper.isAvailableHighLevelStatus(distributionConfigVo.getMerchantId(), distributionConfigVo.getCompanyId());

        //日志记录操作
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起编辑分发规则操作,confId: %s，merchantId: %s，companyId: %s;",
                    httpServletRequest.getRemoteAddr(), merchantId, user.getMerchantName(),distributionConfigVo.getConfId(),distributionConfigVo.getMerchantId(), distributionConfigVo.getCompanyId()));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }

        return distributeConfigService.editDistributeConfig(distributionConfigVo);
    }

    @PostMapping(value = "updateStatus")
    @ApiOperation(value = "更新分发规则状态", notes = "更新分发规则状态")
    public String updateStatus(@RequestBody DistributionConfigVo distributionConfigVo, HttpServletRequest httpServletRequest){
        //日志记录操作
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起更新分发规则状态操作,confId: %s，status: %s, merchantId: %s，companyId: %s;",
                    httpServletRequest.getRemoteAddr(), merchantId, user.getMerchantName(),distributionConfigVo.getConfId(),distributionConfigVo.getStatus(),
                    distributionConfigVo.getMerchantId(), distributionConfigVo.getCompanyId()));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }

        return distributeConfigService.updateStatus(distributionConfigVo);
    }

    @PostMapping(value = "delete")
    @ApiOperation(value = "删除分发规则", notes = "删除分发规则")
    public String delete(@RequestBody Map<String, Object> request, HttpServletRequest httpServletRequest){
        //日志记录操作
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起删除分发规则操作,confId: %s;",
                    httpServletRequest.getRemoteAddr(), merchantId, user.getMerchantName(),request.get("confId")));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }

        return distributeConfigService.delete(request);
    }
}
