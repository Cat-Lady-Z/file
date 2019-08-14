package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.merchant.MerchantToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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

    /**
     * 获取商户的登录信息
     * @param merchantName
     * @return
     */
    List<MerchantToken> selectByMerchantName(@Param("merchantName") String merchantName);
}