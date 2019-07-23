package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntity;
import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntityVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 配置渠道mapper
 */
@Mapper
public interface ChannelConfigMapper {
    /**
     * 删除配置渠道
     * @param confId
     * @return
     */
    int deleteByPrimaryKey(@Param("confId") Integer confId);

    /**
     * 获取商户配置的渠道列表（/根据商户id）
     * @param request
     * @return
     */
    List<ChannelConfigEntityVo> queryChannelConfigList(Map<String, Object> request);

    /**
     * 新增渠道
     * @param vo
     * @return
     */
    int addChannelConfig(ChannelConfigEntityVo vo);

    /**
     * 编辑渠道
     * @param channelConfigEntity
     * @return
     */
    int updateChannelConfig(ChannelConfigEntity channelConfigEntity);

    /**
     * 更新状态（状态(1.active;0:disable)）
     * @param channelConfigEntity
     * @return
     */
    int updateStatus(ChannelConfigEntity channelConfigEntity);

    /**
     * 根据公司Id获取所有配置的渠道列表
     * @param companyIds
     * @return
     */
    List<ChannelConfigEntityVo> queryChannelsByCompanyIds(@Param("companyIds") List<Integer> companyIds);

    /**
     * 查询已配置渠道id（当前机构在使用、被禁用的渠道）
     * @return
     */
    List<Integer> queryChannelIds(Map<String, Object> request);

    /**
     *
     * @param confId
     * @return
     */
    ChannelConfigEntity queryChannelConfigByConfId(@Param("confId") Integer confId);
}