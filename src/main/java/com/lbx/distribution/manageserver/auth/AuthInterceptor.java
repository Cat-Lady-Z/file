package com.lbx.distribution.manageserver.auth;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.merchant.MerchantToken;
import com.lbx.distribution.manageserver.helper.AuthHelper;
import com.lbx.distribution.manageserver.util.DateUtil;
import com.lbx.distribution.manageserver.util.ManageException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 权限管理
 * @version  0.0.1
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthHelper authHelper;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse rep, Object handler) {

        //如果是预链接的请求，是就直接放行
        String method = req.getMethod();
        if ("OPTIONS".equals(method)) {
            return true;
        }

        String uri = req.getRequestURI();

        // 不需要权限验证的 uri, 直接放行
        List<String> accessUris = authHelper.getCommonUri();
        if (accessUris.contains(uri)){
            return true;
        }

        //检测是否是导出uri，导出的uri是get请求，token放在地址里面
        List<String> exportUris = authHelper.getExportUri();
        String token = null;
        if (exportUris.contains(uri) && "GET".equals(method)){
            token = req.getParameter("X-Token");
        } else {
            token = req.getHeader("X-Token");
        }

        // token 检测，需要检测权限的uri，没有 token拦截
        if (StringUtils.isBlank(token)){
            //跨域设置
            String origin = req.getHeader("Origin");
            rep.setHeader("Access-Control-Allow-Origin", origin);

            logger.info("======================token is null, uri : {}========================",uri);
            throw new ManageException(ManageResultCode.TOKEN_Exception, "token为空！");

        }

        // 到 数据库 去查找，找不到，拦截
        MerchantToken user = authHelper.getUser(token);
        if (user == null){
            rep.setHeader("X-Token", null);

            String origin = req.getHeader("Origin");
            rep.setHeader("Access-Control-Allow-Origin", origin);

            logger.info("======================用户不存在或者未登录, uri : {}======================",uri);
            throw new ManageException(ManageResultCode.TOKEN_Exception, "该用户不存在或者未登录！");
        }

        //判断用户状态是否处于登录状态（1: 登录，0: 退出，-1: 过期）
        Integer status = user.getStatus();
        if (status != 1 ){
            String origin = req.getHeader("Origin");
            rep.setHeader("Access-Control-Allow-Origin", origin);

            logger.info("======================用户已退出或登录已过期, uri : {}======================",uri);
            throw new ManageException(ManageResultCode.TOKEN_Exception, "已经退出登录或登录已过期，请重新登录！");
        }

        //判断token是否过期（默认有效时间3个小时）
        Date createDate = user.getCreateTime();
        long createTime = createDate.getTime() / 1000;
        Integer expireTime = user.getExpireTime();
        Long currentTime = DateUtil.unixTime();
        if (createTime + expireTime < currentTime ){
            authHelper.invalidateToken(rep, token, -1);

            String origin = req.getHeader("Origin");
            rep.setHeader("Access-Control-Allow-Origin", origin);

            logger.info("======================token 过期, uri : {}======================",uri);
            throw new ManageException(ManageResultCode.TOKEN_Exception, "登录过期，请重新登录！");
        }

        //判断用户是否有权限访问该接口
        if ( !authHelper.getAuthorization(token, uri) ){

            String origin = req.getHeader("Origin");
            rep.setHeader("Access-Control-Allow-Origin", origin);

            logger.info("======================无权限, uri : {}======================",uri);
            throw new ManageException(ManageResultCode.TOKEN_Exception, "没有权限访问此接口！");
        }
        req.setAttribute("user", user);

        // 只有返回true才会继续向下执行，返回false取消当前请求
        return true;
    }
}
