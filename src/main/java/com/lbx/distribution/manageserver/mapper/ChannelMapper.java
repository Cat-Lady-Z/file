package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.channel.ChannelConfigMenu;
import com.lbx.distribution.manageserver.entity.channel.ChannelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 渠道mapper
 */
@Mapper
public interface ChannelMapper {
    /**
     * 查询渠道列表
     * @param request
     * @return
     */
    List<ChannelEntity> queryChannelList(Map<String, Object> request);

    /**
     * 新增渠道
     * @param request
     * @return
     */
    int addChannel(Map<String, Object> request);

    /**
     * 删除渠道
     * @param request
     * @return
     */
    int deleteChannel(Map<String, Object> request);

    /**
     * 根据Id获取渠道名称
     * @param channelId
     * @return
     */
    String queryChannelNameByChannelId(@Param("channelId") Integer channelId);

    /**
     * 查询所有可用渠道Id
     * @return
     */
    List<Integer> queryChannelIds();

    /**
     *  查询可用的渠道列表
     * @param request
     * @return
     */
    List<ChannelEntity> queryAvailableChannelList(Map<String, Object> request);

    /**
     * 查询所有可用渠道
     * @return
     */
    List<ChannelConfigMenu> queryAvailableChannelMenu();
}