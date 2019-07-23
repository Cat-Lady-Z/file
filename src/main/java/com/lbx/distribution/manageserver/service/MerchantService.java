package com.lbx.distribution.manageserver.service;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.MenuEntity;
import com.lbx.distribution.manageserver.entity.MenuRequest;
import com.lbx.distribution.manageserver.entity.merchant.MerchantConfig;
import com.lbx.distribution.manageserver.entity.PageResult;
import com.lbx.distribution.manageserver.entity.merchant.MerchantEntity;
import com.lbx.distribution.manageserver.entity.merchant.MerchantEntityVo;
import com.lbx.distribution.manageserver.mapper.CompanyMapper;
import com.lbx.distribution.manageserver.mapper.MerchantMapper;
import com.lbx.distribution.manageserver.mapper.ShopMapper;
import com.lbx.distribution.manageserver.util.DateUtil;
import com.lbx.distribution.manageserver.util.MD5Utils;
import com.lbx.distribution.manageserver.util.ManageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户service
 */
@Service
public class MerchantService {
    private static Logger logger = LoggerFactory.getLogger(MerchantService.class);

    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    ManageCommonService manageCommonService;
    @Autowired
    private MerchantCommonService merchantCommonService;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private ShopMapper shopMapper;

    /**
     * 新增商户
     * @return
     * @Description: 密码用MD5加密
     */
    @Transactional
    public String addMerchant(MerchantEntityVo merchantEntityVo){
        //确认输入数据是否符合创建条件
        this.checkDataLegal(merchantEntityVo);
        this.checkPassword(merchantEntityVo);
        this.checkMerchantName(merchantEntityVo.getMerchantName());

        //密码加密
        String password_md5Code = MD5Utils.getPassword(merchantEntityVo.getPassword(), merchantEntityVo.getMerchantName());
        merchantEntityVo.setPassword(password_md5Code);

        //新增用户，获取生成的appKey和密钥
        MerchantConfig merchantConfig = merchantCommonService.insertMerchant(merchantEntityVo);

        logger.info(String.format("新增商户成功. merchantId: %s;", merchantConfig.getMerchantId()));
        String resp = manageCommonService.getRespBody(merchantConfig);

        return resp;
    }

    /**
     * 校验必要参数
     * @param merchantEntityVo
     */
    private void checkDataLegal(MerchantEntityVo merchantEntityVo) {
        String merchantName = merchantEntityVo.getMerchantName();
        if (merchantName == null && merchantName.replaceAll(" ", "").length() == 0) {
            logger.warn("账号不能为空! ");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "账号不能为空！");
        }
        String merchantNameReplace = merchantName.replaceAll(" ", "");
        merchantEntityVo.setMerchantName(merchantNameReplace);

        String enterpriseName = merchantEntityVo.getEnterpriseName();
        if (enterpriseName == null && enterpriseName.replaceAll(" ", "").length() == 0) {
            logger.warn("公司名称不能为空! ");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "公司名称不能为空！");
        }
        String enterpriseNameReplace = enterpriseName.replaceAll(" ", "");
        merchantEntityVo.setEnterpriseName(enterpriseNameReplace);

        String contractName = merchantEntityVo.getContractName();
        if (contractName == null && contractName.replaceAll(" ", "").length() == 0) {
            logger.warn("联系人姓名不能为空! ");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "联系人姓名不能为空！");
        }
        String contractNameReplace = contractName.replaceAll(" ", "");
        merchantEntityVo.setContractName(contractNameReplace);
    }

    private void checkPassword(MerchantEntityVo merchantEntityVo) {
        //判断两次输入的密码是否一致
        String checkPassword = merchantEntityVo.getCheckPassword();
        String password = merchantEntityVo.getPassword();
        if (password == null || password.replaceAll(" ", "").length() == 0 || checkPassword == null
                || checkPassword.replaceAll(" ", "").length() == 0) {
            logger.warn(String.format("密码不能为空! password: %s, checkPassword: %s;", password, checkPassword));
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "密码不能为空！");
        }
        String passwordReplace = password.replaceAll(" ", "");
        String checkPasswordReplace = checkPassword.replaceAll(" ", "");
        merchantEntityVo.setPassword(passwordReplace);
        merchantEntityVo.setCheckPassword(checkPasswordReplace);

        if (!checkPassword.equals(password)) {
            logger.warn(String.format("两次输入的密码不一致! password: %s, checkPassword: %s;", password, checkPassword));
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "两次输入的密码不一致！");
        }
    }

    private void checkMerchantName(String merchantName) {
        //判断账号（商户名称）是否已存在
        if (merchantMapper.isExist( merchantName ) != null ) {
            logger.warn(String.format("账户已存在. merchantName: %s;", merchantName));
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "账户已存在！");
        }
    }

    /**
     * 获取商户列表(/分页)
     * @return
     */
    public String queryMerchantList(Map<String, Object> manageSimpleRequest) {
        String resp = new String();

        PageResult pageResult = merchantCommonService.getMerchantList(manageSimpleRequest);

        if (pageResult == null) {
            logger.info("queryMerchantList: 无商户信息!");
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "无商户信息");
        }

        resp = manageCommonService.getRespBody(pageResult);

        return resp;
    }

    /**
     * 根据ID查询单个商户
     * @return
     */
    public String queryMerchantByMerchantId(Integer merchantId) {
        MerchantEntity merchantEntity = merchantMapper.queryMerchantByMerchantId(merchantId);

        if (merchantEntity == null ){
            logger.warn(String.format("无商户信息. merchantId: %s;", merchantId));
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "无商户信息");
        }

        String resp = manageCommonService.getRespBody(merchantEntity);

        return resp;
    }

    /**
     * 更新商户信息
     * @return
     * @Description: 目前只需要更新密码，直接调用更新密码的方法
     */
    @Transactional
    public String updateMerchant(MerchantEntityVo merchantEntityVo) {
        return updatePasswordByID(merchantEntityVo);
    }

    /**
     * 根据商户ID更新商户状态(1.active;0:disable)
     * @return
     */
    @Transactional
    public String updateMerchantStatus(MerchantEntityVo merchantEntityVo) {
        merchantEntityVo.setUpdateTime(DateUtil.getCurrentTimestamp());

        //更新商户状态
        if (merchantCommonService.updateMerchantStatus(merchantEntityVo) <= 0 ){
            logger.warn(String.format("状态更新失败! merchantId: %s, status: %s;", merchantEntityVo.getMerchantId(), merchantEntityVo.getStatus()));
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "状态更新失败");
        }

        Map<String,Object> params = new HashMap<>();
        params.put("merchantId", merchantEntityVo.getMerchantId());
        params.put("status", merchantEntityVo.getStatus());
        params.put("updateTime", merchantEntityVo.getUpdateTime());
        //禁用/启用/删除商户下所有公司
        companyMapper.updateCompanyStatus(params);
        //禁用/启用/删除商户下所有门店
        shopMapper.updateShopStatus(params);

        String str = "商户状态更新成功！";
        logger.info(String.format("商户状态更新成功. merchantId: %s, status: %s;", merchantEntityVo.getMerchantId(), merchantEntityVo.getStatus()));
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    /**
     * 根据商户ID更新密码
     * @return
     */
    @Transactional
    public String updatePasswordByID(MerchantEntityVo merchantEntityVo) {
        //确认两次输入密码是否一致
        this.checkPassword(merchantEntityVo);

        String password_md5Code = MD5Utils.getPassword(merchantEntityVo.getPassword(), merchantEntityVo.getMerchantName());
        try {
            merchantEntityVo.setUpdateTime( DateUtil.getCurrentDate() );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        merchantEntityVo.setPassword(password_md5Code);

        if (merchantMapper.updatePasswordByMerchantId(merchantEntityVo) < 0 ){
            logger.error(String.format("密码更新失败. merchantId: %s;", merchantEntityVo.getMerchantId()));
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "密码更新失败！");
        }

        String str = "密码更新成功！";
        logger.info(String.format("密码更新成功! merchantId: %s;", merchantEntityVo.getMerchantId()));
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    /**
     * 根据商户ID删除单个商户
     * @return
     */
    @Transactional
    public String deleteByMerchantId(MerchantEntity merchant) {
        try {
            merchant.setUpdateTime(DateUtil.getCurrentDate());
            if (merchantMapper.deleteByMerchantId(merchant) <= 0 ){
                logger.error(String.format("删除商户失败! merchantId: %s;", merchant.getMerchantId()));
                throw new ManageException(ManageResultCode.REQUEST_FAIL, "删除商户失败");
            }
        } catch (ParseException e) {
            logger.error(String.format("删除商户失败! merchantId: %s;", merchant.getMerchantId()));
            e.printStackTrace();
        }

        String str = "删除商户成功！";
        logger.info(String.format("删除商户成功! merchantId: %s;", merchant.getMerchantId()));
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    /**
     * 根据账户（商户名称）查询商户是否已存在
     * @param merchantName
     * @return
     */
    public String isExist(String merchantName) {
        if (merchantMapper.isExist(merchantName) != null ) {
            logger.warn(String.format("账户已存在! merchantName: %s;", merchantName));
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "账户已存在！");
        }

        String str = "账户可用！";
        logger.info(String.format("账户可用! merchantName: %s;", merchantName));
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    /**
     * 获取商户菜单（根据角色(0.管理员;1:普通商户)区分返回内容）
     * @param menuRequest
     * @return
     */
    public String merchantMenu(MenuRequest menuRequest) {
        return merchantCommonService.getMerchantMenu(menuRequest);
    }

    /**
     * 获取没有被禁用、删除，渠道配置没有满配的商户
     * @param request
     * @return
     */
    public String availableMerchantList(Map<String, Object> request) {

        List<MenuEntity> availableMerchantList = merchantCommonService.getAvailableMerchantList(request);

        String resp = manageCommonService.getRespBody(availableMerchantList);

        return resp;
    }
}
