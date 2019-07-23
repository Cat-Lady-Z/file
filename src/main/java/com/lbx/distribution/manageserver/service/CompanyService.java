package com.lbx.distribution.manageserver.service;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.MenuEntity;
import com.lbx.distribution.manageserver.entity.MenuRequest;
import com.lbx.distribution.manageserver.entity.PageResult;
import com.lbx.distribution.manageserver.entity.company.CompanyContactShop;
import com.lbx.distribution.manageserver.entity.company.CompanyEntity;
import com.lbx.distribution.manageserver.helper.CommonHelper;
import com.lbx.distribution.manageserver.mapper.CompanyMapper;
import com.lbx.distribution.manageserver.mapper.MerchantMapper;
import com.lbx.distribution.manageserver.mapper.ShopMapper;
import com.lbx.distribution.manageserver.util.DateUtil;
import com.lbx.distribution.manageserver.util.ManageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * @ClassName:
 * @Description: //
 */

@Service
public class CompanyService {

    private static Logger logger = LoggerFactory.getLogger(CompanyService.class);

    @Autowired
    private CompanyMapper comapnyMapper;
    @Autowired
    private CompanyCommonService companyCommonService;
    @Autowired
    private ManageCommonService manageCommonService;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    protected MerchantMapper merchantMapper;
    @Autowired
    private CommonHelper commonHelper;

    /**
     * 获取公司分页列表
     * @return
     * @Description: 用于组织机构模块，显示
     */
    public String queryCompanyList(Map<String, Object> request) {

        PageResult result = companyCommonService.getCompanyList(request);

        String resp =  manageCommonService.getRespBody(result);

        return resp;
    }

    /**
     * 新增公司
     * @param companyEntity
     * @return
     */
    @Transactional
    public String addCompany(CompanyEntity companyEntity) {
        Date updateTime = null;
        try {
            updateTime = DateUtil.getCurrentDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        companyEntity.setCreateTime(updateTime);
        companyEntity.setUpdateTime(updateTime);

        CompanyEntity company = companyCommonService.insertCompany(companyEntity);

        logger.info(String.format("addCompany success. merchantId: %s /companyId: %s , companyName: %s;",
                companyEntity.getMerchantId(), companyEntity.getCompanyId(), companyEntity.getCompanyName() ));
        String resp = manageCommonService.getRespBody(company);

        return resp;
    }

    /**
     *  根据公司ID更新公司状态(1.active;0:disable)最后一级可以进行此操作【下层门店也会被置禁用状态】（isEnd: 1：最后一级，0：不是）
     * @param request
     * @return
     */
    @Transactional
    public String updateCompanyStatus(Map<String, Object> request) {
        this.checkPermission(request);

        Timestamp updateTime = DateUtil.getCurrentTimestamp();
        request.put("updateTime", updateTime);

        if (comapnyMapper.updateCompanyStatus( request ) <= 0 ){
            logger.warn(String.format("updateCompanyStatus fail. merchantId: %s /companyId: %s , status: %s;",
                    request.get("merchantId"), request.get("companyId"), request.get("status") ));

            throw new ManageException(ManageResultCode.REQUEST_FAIL, "公司状态更新失败！");
        }

        //将下层门店也禁用/启用
        shopMapper.updateShopStatus(request);

        logger.info(String.format("updateCompanyStatus success. merchantId: %s /companyId: %s , status: %s;",
                request.get("merchantId"), request.get("companyId"), request.get("status") ));

        String str = "公司状态更新成功！";
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    private void checkPermission(Map<String, Object> map) {
        Object statusObject = map.get("status");
        Object companyIdObject = map.get("companyId");
        Object isEndObject = map.get("isEnd");

        if (statusObject == null || companyIdObject == null || isEndObject == null ) {
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误！");
        }

        Integer status = (Integer) statusObject;
        //如果不是最后一级不允许禁用
        Integer isEnd = (Integer) isEndObject;
        if ( status == 0 && isEnd != 1 ) {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "请求非法！该公司不是最后一级");
        }

        //如果不是最后一级不允许删除
        if ( status == -1 && isEnd != 1 ){
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "请求非法！该公司不是最后一级");
        }

        Integer companyId = (Integer) companyIdObject;
        CompanyEntity companyEntity = comapnyMapper.queryCompanyByCompanyId(companyId);
        //判断上级机构(商户/公司)是否在启用状态，禁用则不允许更改状态
        commonHelper.isAvailableHighLevelStatus(companyEntity.getMerchantId(), companyEntity.getParentId());
    }

    /**
     * 根据公司ID删除公司，最后一级可以进行此操作【下层门店也会被置删除状态】（非物理删除，状态：-1）（isEnd: 1：最后一级，0：不是）
     * @return
     * @Description:
     */
    @Transactional
    public String deleteByCompanyId(Map<String, Object> request) {
        this.checkPermission(request);

        Timestamp updateTime = DateUtil.getCurrentTimestamp();
        request.put("updateTime", updateTime);

        if ( comapnyMapper.updateCompanyStatus( request ) <= 0 ){
            logger.warn(String.format("deleteByCompanyId fail. merchantId: %s /companyId: %s , status: %s;",
                    request.get("merchantId"), request.get("companyId"), request.get("status") ));

            throw new ManageException(ManageResultCode.REQUEST_FAIL, "删除失败！");
        }

        //将下层门店都删除
        shopMapper.updateShopStatus(request);

        logger.info(String.format("deleteByCompanyId success. merchantId: %s /companyId: %s , status: %s;",
                request.get("merchantId"), request.get("companyId"), request.get("status") ));

        String str = "删除成功！";
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    /**
     * 关联门店
     * @param companyContactShop
     * @return
     */
    @Transactional
    public String contactShops(CompanyContactShop companyContactShop) {
        return companyCommonService.contactShops(companyContactShop);
    }

    /**
     * 获取组织机构菜单
     * @param menuRequest
     * @return
     */
    public String companyMenu(MenuRequest menuRequest) {
        //判断是否已机构类型
        Integer type = menuRequest.getType();
        if (type == null){
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "没有type信息！");
        }
        List<MenuEntity> resultList = companyCommonService.queryMenu(menuRequest);


        String resp =  manageCommonService.getRespBody(resultList);

        return resp;
    }

    /**
     * 获取公司详情
     * @param request
     * @return
     */
    public String companyDetail(Map<String, Object> request) {
        Object companyId_o = request.get("companyId");

        if (companyId_o == null) {
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误！");
        }

        Integer companyId = (Integer) companyId_o;
        if (companyId <= 0) {
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数公司id非法！");
        }

        CompanyEntity company = comapnyMapper.queryCompanyByCompanyId(companyId);


        String resp =  manageCommonService.getRespBody(company);

        return resp;
    }

    /**
     * 更新公司信息
     * @param companyEntity
     * @return
     */
    @Transactional
    public String updateCompany(CompanyEntity companyEntity) {
        String companyName = companyEntity.getCompanyName();
        if (companyName == null || companyName.replaceAll(" ", "").length()==0 ) {
            logger.warn(String.format("请求参数错误!公司名称为空."));
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误！公司名称为空.");
        }
        String companyNameReplace = companyName.replaceAll(" ", "");
        companyEntity.setCompanyName(companyNameReplace);

        CompanyEntity company= comapnyMapper.queryCompanyByCompanyId(companyEntity.getCompanyId());
        //判断当前是否在启用状态，禁用则不允许更改
        commonHelper.isAvailableCompanyStatus(companyEntity.getCompanyId());
        //判断商户和上级公司是否在启用状态，禁用则不允许更改
        commonHelper.isAvailableHighLevelStatus(company.getMerchantId(), company.getParentId());

        try {
            companyEntity.setUpdateTime(DateUtil.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if ( comapnyMapper.updateCompany(companyEntity) < 0 ) {
            logger.warn(String.format("updateCompany fail. companyId: %s , companyName: %s;",
                    companyEntity.getCompanyId(), companyEntity.getCompanyName() ));

            throw new ManageException(ManageResultCode.REQUEST_FAIL, "更新失败！");
        }

        logger.info(String.format("updateCompany success. companyId: %s , companyName: %s;",
                companyEntity.getCompanyId(), companyEntity.getCompanyName() ));

        String resp =  manageCommonService.getRespBody("更新成功");

        return resp;
    }
}
