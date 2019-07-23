package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.MenuRequest;
import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntityVo;
import com.lbx.distribution.manageserver.entity.merchant.MerchantToken;
import com.lbx.distribution.manageserver.entity.shop.ShopEntity;
import com.lbx.distribution.manageserver.entity.shop.ShopEntityVo;
import com.lbx.distribution.manageserver.helper.CommonHelper;
import com.lbx.distribution.manageserver.service.ShopService;
import com.lbx.distribution.manageserver.util.ManageException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Validated
@Api(value = "门店管理")
@RequestMapping(value="shop")
public class ShopController {
    private static Logger logger = LoggerFactory.getLogger(ShopController.class);
    @Autowired
    private ShopService shopService;
    @Autowired
    private CommonHelper commonHelper;

    @PostMapping(value = "addShop")
    @ApiOperation(value = "新增门店", notes = "新增单个门店")
    public String addShop(@RequestBody ShopEntityVo shopEntityVo, HttpServletRequest httpServletRequest){
        //日志记录操作
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起新增门店操作，shopName: %s, merchantId: %s, companyId: %s;",
                    httpServletRequest.getRemoteAddr(),merchantId, user.getMerchantName(), shopEntityVo.getShopName(), shopEntityVo.getMerchantId(), shopEntityVo.getCompanyId()));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }

        //判断上级机构(商户/公司)是否在启用状态，禁用则不允许新增
        commonHelper.isAvailableHighLevelStatus(shopEntityVo.getMerchantId(), shopEntityVo.getCompanyId());

        return shopService.addShop(shopEntityVo);
    }

    @PostMapping(value = "updateShopStatus")
    @ApiOperation(value = "更新门店状态", notes = "更新门店状态")
    public String updateShopStatus(@RequestBody Map<String, Object> request, HttpServletRequest httpServletRequest){
        //日志记录操作
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起更新门店状态操作，shopId: %s, status: %s;",
                    httpServletRequest.getRemoteAddr(),merchantId, user.getMerchantName(), request.get("shopId"), request.get("status")));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }

        return shopService.updateShopStatus(request);
    }

    @PostMapping(value = "updateShop")
    @ApiOperation(value = "编辑门店", notes = "根据门店ID编辑单个门店")
    public String updateShop(@RequestBody ShopEntityVo shopEntityVo, HttpServletRequest httpServletRequest){
        //日志记录操作
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起编辑门店操作，shopId: %s;",
                    httpServletRequest.getRemoteAddr(),merchantId, user.getMerchantName(), shopEntityVo.getShopId()));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }
        return shopService.updateShop(shopEntityVo);
    }

    @PostMapping(value = "deleteShop")
    @ApiOperation(value = "删除门店", notes = "根据门店ID删除单个门店")
    public String deleteShop(@RequestBody Map<String, Object> request ){
        return shopService.deleteByShopId(request);
    }

    @PostMapping(value = "shopList")
    @ApiOperation(value = "查询门店列表", notes = "查询门店列表(支持多条件/分页查询)")
    public String shopList(@RequestBody Map<String, Object> request){
        return shopService.queryShopList(request);
    }

    @PostMapping(value = "queryShopByShopName")
    @ApiOperation(value = "根据门店名称查询门店", notes = "根据门店名称分页获取门店列表，支持模糊查询")
    public String queryShopByShopName(@RequestBody Map<String, Object> request){
        return shopService.queryShopByShopName(request);
    }

    @PostMapping(value = "shopDetail")
    @ApiOperation(value = "查询单个门店", notes = "根据门店ID查询")
    public String shopDetail(@RequestBody ShopEntity shopEntity ){
        return shopService.queryShopByShopId(shopEntity.getShopId());
    }

    @PostMapping(value = "shopMenu")
    @ApiOperation(value = "获取门店菜单", notes = "获取门店菜单")
    public String shopMenu(@RequestBody MenuRequest menuRequest){
        return shopService.shopMenu(menuRequest);
    }

    @PostMapping(value = "businessTypeMenu")
    @ApiOperation(value = "获取业务类型菜单", notes = "获取业务类型下拉菜单")
    public String businessTypeMenu( ){
        return shopService.getBusinessTypeMenu();
    }

    /*@PostMapping(value = "shopTypeMenu")
    @ApiOperation(value = "获取门店类型菜单", notes = "获取门店类型下拉菜单")
    public String shopTypeMenu(@RequestBody Map<String, Object> request ){
        return shopService.getShopTypeMenu();
    }*/

    /*@PostMapping(value = "bindChannel")
    @ApiOperation(value = "绑定渠道【暂不开放】", notes = "绑定渠道")
    public String bindChannel(@RequestBody ChannelConfigEntityVo cv ){
        return shopService.bindChannel(cv);
    }*/
}
