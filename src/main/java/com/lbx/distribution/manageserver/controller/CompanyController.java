package com.lbx.distribution.manageserver.controller;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.MenuRequest;
import com.lbx.distribution.manageserver.entity.company.CompanyContactShop;
import com.lbx.distribution.manageserver.entity.company.CompanyEntity;
import com.lbx.distribution.manageserver.entity.merchant.MerchantToken;
import com.lbx.distribution.manageserver.helper.CommonHelper;
import com.lbx.distribution.manageserver.service.CompanyService;
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
@Api(value = "公司管理")
@RequestMapping(value="company")
public class CompanyController {
    private static Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CommonHelper commonHelper;

    @PostMapping(value = "addCompany")
    @ApiOperation(value = "新增公司", notes = "新增单个公司")
    public String addCompany(@RequestBody CompanyEntity companyEntity ){
        //判断上级机构(商户/公司)是否在启用状态，禁用则不允许导入
        commonHelper.isAvailableHighLevelStatus(companyEntity.getMerchantId(), companyEntity.getParentId());

        return companyService.addCompany(companyEntity);
    }

    @PostMapping(value = "updateCompanyStatus")
    @ApiOperation(value = "更新公司状态", notes = "根据公司ID更新公司状态(1.active;0:disable,-1:删除)")
    public String updateCompanyStatus(@RequestBody Map<String, Object> map, HttpServletRequest httpServletRequest){
        //日志记录操作
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起更新公司状态操作，companyId: %s，status: %s;",
                    httpServletRequest.getRemoteAddr(), merchantId, user.getMerchantName(),map.get("companyId"), map.get("status")));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }
        return companyService.updateCompanyStatus(map);
    }

    @PostMapping(value = "updateCompany")
    @ApiOperation(value = "更新公司信息", notes = "根据公司ID更新公司信息")
    public String updateCompany(@RequestBody CompanyEntity companyEntity, HttpServletRequest httpServletRequest){
        //日志记录操作
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起更新公司信息操作，companyId: %s，status: %s;",
                    httpServletRequest.getRemoteAddr(), merchantId, user.getMerchantName(),companyEntity.getCompanyId(), companyEntity.getStatus()));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }
        return companyService.updateCompany(companyEntity);
    }

    @PostMapping(value = "contactShop")
    @ApiOperation(value = "关联门店", notes = "根据门店ID，关联门店")
    public String contactShop(@RequestBody CompanyContactShop companyContactShop){
        return companyService.contactShops(companyContactShop);
    }

    @PostMapping(value = "deleteCompany")
    @ApiOperation(value = "删除公司", notes = "根据公司ID删除单个公司")
    public String deleteCompany(@RequestBody Map<String, Object> request ){
        return companyService.deleteByCompanyId(request);
    }

    @PostMapping(value = "companyList")
    @ApiOperation(value = "查询公司列表", notes = "查询公司列表，组织机构显示用(支持分页查询)")
    public String companyList(@RequestBody Map<String, Object> request ){
        return companyService.queryCompanyList(request);
    }

    @PostMapping(value = "companyDetail")
    @ApiOperation(value = "查询公司详情", notes = "查询公司详情")
    public String companyDetail(@RequestBody Map<String, Object> request ){
        return companyService.companyDetail(request);
    }

    @PostMapping(value = "multiCondQueryCompanys")
    @ApiOperation(value = "多条件查询公司", notes = "多条件分页查询公司列表")
    public String multiCondQueryCompanies(@RequestBody Map<String, Object> request){
        return companyService.queryCompanyList(request);
    }

    @PostMapping(value = "companyMenu")
    @ApiOperation(value = "获取公司菜单", notes = "获取公司菜单")
    public String companyMenu(@RequestBody MenuRequest menuRequest){
        return companyService.companyMenu(menuRequest);
    }
}
