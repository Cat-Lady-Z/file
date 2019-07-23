package com.lbx.distribution.manageserver.helper;

import com.lbx.distribution.manageserver.entity.merchant.MerchantToken;
import com.lbx.distribution.manageserver.mapper.CmsUrlInfoMapper;
import com.lbx.distribution.manageserver.mapper.MerchantTokenMapper;
import com.lbx.distribution.manageserver.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:
 * @Description: //
 */
@Component
public class AuthHelper {

    private static final Integer COMMON_ROLE = -1;
    private static final Integer ADMIN_ROLE = 0;
    private static final Integer GENERALUSER_ROLE = 1;

    @Autowired
    private CmsUrlInfoMapper cmsUrlInfoMapper;

    @Autowired
    MerchantTokenMapper merchantTokenMapper;

    /**
     * 获取可以访问的公共的uri
     * @return
     */
    public List<String> getCommonUri() {
        List<String> accessUriList = cmsUrlInfoMapper.selectUriByRole(COMMON_ROLE);

        return accessUriList;
    }

    /**
     * 获取可以导出uri
     */
    public List<String> getExportUri() {
        List<String> exportUri = cmsUrlInfoMapper.selectUriByRole(GENERALUSER_ROLE);

        return exportUri;
    }

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    public MerchantToken getUser(String token) {

        return merchantTokenMapper.selectByPrimaryKey(token);

    }

    /**
     * 判断当前用户是否有权限访问该接口
     * @param token
     * @param uri
     * @return
     */
    public boolean getAuthorization(String token, String uri) {
        MerchantToken user = this.getUser(token);

        Integer merchantId = user.getMerchantId();
        Integer role = user.getRole();

        //获取超级管理员才能访问的接口
        List<String> assessUris = cmsUrlInfoMapper.selectUriByRole( ADMIN_ROLE );
        if ( assessUris.contains(uri) ) {
            if ( role != ADMIN_ROLE ) {
                return false;
            }
        }

        return true;
    }

    /**
     * 清除token，将用户状态设为0/-1，退出登录（1: 登录，0: 退出，-1: 过期）
     * @param status
     * @param token
     */
    public void invalidateToken(HttpServletResponse rep, String token, Integer status) {
        Map<String, Object> request = new HashMap<>();
        request.put("token", token);
        request.put("status", status);
        try {
            request.put("updateTime", DateUtil.getCurrentDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        merchantTokenMapper.updateStatus( request );
        
        rep.setHeader("X-Token", null);
    }
}
