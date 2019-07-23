package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.merchant.MerchantConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MerchantConfigMapper {
    /**
     * 新建商户配置
     * @param record
     * @return
     */
    int insert(MerchantConfig record);

    int deleteByPrimaryKey(Integer merchantId);

    int insertSelective(MerchantConfig record);

    MerchantConfig selectByPrimaryKey(Integer merchantId);

    int updateByPrimaryKeySelective(MerchantConfig record);

    int updateByPrimaryKey(MerchantConfig record);
}