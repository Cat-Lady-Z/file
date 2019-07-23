package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.MenuRequest;
import com.lbx.distribution.manageserver.entity.merchant.MerchantEntity;
import com.lbx.distribution.manageserver.entity.merchant.MerchantEntityVo;
import com.lbx.distribution.manageserver.entity.merchant.MerchantToken;
import com.lbx.distribution.manageserver.helper.CommonHelper;
import com.lbx.distribution.manageserver.service.MerchantService;
import com.lbx.distribution.manageserver.util.ManageException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Validated
@Api(value = "商户管理")
@RequestMapping(value="merchant")
public class MerchantController {
    private static Logger logger = LoggerFactory.getLogger(MerchantController.class);

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CommonHelper commonHelper;

    @PostMapping(value = "addMerchant")
    @ApiOperation(value = "新增商户", notes = "创建商户")
    public String addMerchant(@RequestBody MerchantEntityVo merchantEntityVo){
        return merchantService.addMerchant(merchantEntityVo);
    }

    @PostMapping(value = "updateMerchant")
    @ApiOperation(value = "更新商户", notes = "更新商户，目前只是更新密码")
    public String updateMerchant(@RequestBody MerchantEntityVo merchantEntityVo ){
        return merchantService.updateMerchant(merchantEntityVo);
    }

    @PostMapping(value = "updateMerchantStatus")
    @ApiOperation(value = "更新商户状态", notes = "根据商户ID更新商户状态(1.active;0:disable, -1:删除商户)")
    public String updateMerchantStatus(@RequestBody MerchantEntityVo merchantEntityVo, HttpServletRequest httpServletRequest){
        //日志记录操作
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%S, merchantId:%s ,merchantName:%s 发起更新商户状态操作，merchantId: %s, status: %s;",
                    httpServletRequest.getRemoteAddr(),merchantId, user.getMerchantName(), merchantEntityVo.getMerchantId(), merchantEntityVo.getStatus()));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }
         return merchantService.updateMerchantStatus(merchantEntityVo);
    }

    @PostMapping(value = "updatePasswordByID")
    @ApiOperation(value = "更新商户密码", notes = "根据商户ID更新密码")
    public String updatePasswordByID(@RequestBody MerchantEntityVo merchantEntityVo){
        return merchantService.updatePasswordByID(merchantEntityVo);
    }

    @PostMapping(value = "merchantList")
    @ApiOperation(value = "查询商户列表", notes = "查询商户列表(/分页)")
    public String merchantList(@RequestBody Map<String, Object> manageSimpleRequest){
        return merchantService.queryMerchantList(manageSimpleRequest);
    }

    @PostMapping(value = "merchantDetail")
    @ApiOperation(value = "查询单个商户", notes = "根据商户ID查询")
    public String merchantDetail(@RequestBody MerchantEntity merchantEntity ){
        return merchantService.queryMerchantByMerchantId(merchantEntity.getMerchantId());
    }

    @PostMapping(value = "isExist")
    @ApiOperation(value = "查询账户是否已存在", notes = "根据商户名称查询商户是否已存在")
    public String isExist(@RequestBody MerchantEntity merchantEntity ){
        return merchantService.isExist(merchantEntity.getMerchantName());
    }

    @PostMapping(value = "merchantMenu")
    @ApiOperation(value = "获取商户菜单", notes = "获取商户菜单")
    public String merchantMenu(@RequestBody MenuRequest menuRequest ){
        return merchantService.merchantMenu(menuRequest);
    }

    @PostMapping(value = "availableMerchantList")
    @ApiOperation(value = "查询可用商户列表", notes = "查询商户列表(/分页)")
    public String availableMerchantList(@RequestBody Map<String, Object> request){
        return merchantService.availableMerchantList(request);
    }
}
