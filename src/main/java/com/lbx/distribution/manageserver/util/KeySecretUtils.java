package com.lbx.distribution.manageserver.util;

import java.util.Date;
import java.util.UUID;

/**
 * @ClassName: 生成appKey和secret工具类（商户）
 * @Description: //
 */
public class KeySecretUtils {

    public KeySecretUtils() {
    }

    /**
     * 生成appKey
     */
    public static String getAppKey() {
        UUID uuid = UUID.randomUUID();
        String appKey = uuid.toString();
        return appKey.trim();
    }

    /**
     * 生成密钥
     * @param appKey
     * @param createTime
     * @return
     */
    public static String getSecret(String appKey, Date createTime) {
        String mw = "lbx" + appKey + createTime;

        String secret = MD5Utils.getMD5Secret(mw);// 得到以后还要用MD5加密。

        return secret.trim();
    }
}
