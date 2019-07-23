package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntity;
import com.lbx.distribution.manageserver.entity.channel.RegionPriceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 城市价格配置
 */
@Mapper
public interface RegionPriceMapper {

    //新增城市价格
    Integer insertRegionPrice(RegionPriceEntity regionPrice);

    //更新城市价格
    Integer updateRegionPrice(RegionPriceEntity regionPrice);

    //删除城市价格
    Integer deleteRegionPrice(Integer id);

    //删除城市价格
    Integer deleteRegionPriceByMerchantIdOrCompanyId(Map<String,Integer> params);

    //根据公司id查询价格配置
    List<RegionPriceEntity> selectByCompanyId(@Param(value="companyId")Integer companyId, @Param(value="channelId")Integer channelId);

    //根据商户id查询价格配置
    List<RegionPriceEntity> selectByMerchantId(@Param(value="merchantId")Integer merchantId,@Param(value="channelId")Integer channelId);

    /**
     * 根据配置渠道信息查询价格配置
     * @param channelConfig
     * @return
     */
    List<RegionPriceEntity> queryRegionEntityByChannelConfig(ChannelConfigEntity channelConfig);
}