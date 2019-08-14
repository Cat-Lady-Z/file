package com.lbx.distribution.manageserver.service;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.ActiveUser;
import com.lbx.distribution.manageserver.entity.LoginResult;
import com.lbx.distribution.manageserver.entity.merchant.MerchantToken;
import com.lbx.distribution.manageserver.entity.merchant.MerchantEntity;
import com.lbx.distribution.manageserver.mapper.MerchantMapper;
import com.lbx.distribution.manageserver.mapper.MerchantTokenMapper;
import com.lbx.distribution.manageserver.util.DateUtil;
import com.lbx.distribution.manageserver.util.MD5Utils;
import com.lbx.distribution.manageserver.util.ManageException;
import com.lbx.distribution.manageserver.util.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 渠道service
 */
@Service
public class LoginService {

    private static Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private MerchantTokenMapper merchantTokenMapper;

    @Autowired
    ManageCommonService manageCommonService;

    /**
     * 用户登录验证，用户登录状态（1: 登录，0: 退出，-1: 过期）
     * @param request
     * @return
     */
    public String login(Map<String, Object> request) {
        MerchantEntity merchant = this.check(request);

        Integer merchantId = merchant.getMerchantId();

        String token = TokenUtils.getToken("" + merchantId);
        String merchantName = merchant.getMerchantName();
        Integer role = merchant.getRole();
        MerchantToken merchantToken = new MerchantToken( token, merchantId, merchantName, role, 1);

        //把用户信息和token存进数据库里面
        merchantTokenMapper.insert(merchantToken);

        logger.info(String.format("The User login success. role: %s, merchantId: %s, merchantName: %s, token: %s;",
                role, merchantId, merchantName,token));

        LoginResult loginResult = new LoginResult();
        loginResult.setToken(token );

        String resp = manageCommonService.getRespBody(loginResult);

        return resp;
    }

    /**
     * 验证账号是否存在
     * @param request
     * @return
     */
    public String isExist(Map<String, Object> request) {
        Object merchantName = request.get("merchantName");
        if (merchantName == null) {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "请输入账号!");
        }

        String name = (String) merchantName;

        if (merchantMapper.isExist(name) == null  ) {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "账号不存在!");
        }

        String str = "账号存在！";
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }


    /**
     * 验证密码是否正确
     * @param request
     * @return
     */
    public String checkPassword(Map<String, Object> request) {
        MerchantEntity merchant = this.check(request);

        String str = "验证通过！";
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    /**
     * 密码验证
     * @param request
     * @return
     */
    private MerchantEntity check(Map<String, Object> request) {
        Object merchantName = request.get("merchantName");
        Object password = request.get("password");

        if (merchantName == null) {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "请输入账号!");
        }
        if (password == null) {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "密码不能为空!");
        }

        String name = (String) merchantName;
        String pass = (String) password;
        String password_MD5 = MD5Utils.getPassword(pass, name);

        MerchantEntity merchant = merchantMapper.isExist(name);
        if (merchant == null) {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "账号不存在!");
        }

        if (merchant.getStatus() == 0) {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "账号已被禁用!");
        }

        if (merchant.getStatus() == -1) {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "账号不存在!");
        }

        if ( !password_MD5.equals(merchant.getPassword())) {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "密码错误!");
        }

        return merchant;
    }

    /**
     * 退出登录（1: 登录，0: 退出，-1: 过期）
     * @param request
     * @return
     */
    public String exit(HttpServletRequest request, HttpServletResponse response ) {
        // 已经登录的情况
        String token = request.getHeader("X-Token");
        if(token != null){
            Map<String, Object> params = new HashMap<>();
            params.put("token", token);
            params.put("status", 0);
            try {
                params.put("updateTime", DateUtil.getCurrentDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            merchantTokenMapper.updateStatus( params );

            MerchantToken user = merchantTokenMapper.selectByPrimaryKey(token);

            logger.info(String.format("The User exit. role: %s, merchantId: %s, merchantName: %s, token: %s;",
                    user.getRole(), user.getMerchantId(), user.getMerchantName(),token));
        }

        response.setHeader("X-Token", null);
        String str = "已退出！";
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }


    /**
     * 获取用户信息
     * @param request
     * @return
     */
    public String getUser(Map<String, Object> request) {
        Object token = request.get("token");
        if ( token == null ) {
            throw new ManageException(ManageResultCode.TOKEN_Exception, "无token信息!");
        }

        String tokenStr = (String) token;

        MerchantToken user = merchantTokenMapper.selectByPrimaryKey(tokenStr);

        if ( user == null) {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "请先登录!");
        }

        ActiveUser userData = new ActiveUser( user.getMerchantId(), user.getMerchantName() , user.getRole());
        String resp = manageCommonService.getRespBody(userData);

        return resp;
    }
}
