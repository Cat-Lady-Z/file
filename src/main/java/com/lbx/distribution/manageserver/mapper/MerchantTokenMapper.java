package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.merchant.MerchantToken;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * tokenMapper
 */
@Mapper
public interface MerchantTokenMapper {
    int deleteByPrimaryKey(String token);

    int insert(MerchantToken record);

    MerchantToken selectByPrimaryKey(String token);

    //设置登录状态
    int updateStatus(Map<String, Object> request);
}