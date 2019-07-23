package com.lbx.distribution.manageserver.service;

import com.lbx.distribution.manageserver.entity.PageResult;
import com.lbx.distribution.manageserver.entity.channel.ChannelEntity;
import com.lbx.distribution.manageserver.mapper.ChannelConfigMapper;
import com.lbx.distribution.manageserver.mapper.ChannelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 渠道service
 */
@Service
public class ChannelService {

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    ManageCommonService manageCommonService;

    @Autowired
    private ChannelConfigMapper channelConfigMapper;

    /**
     * 获取渠道列表
     * @param request
     * @return
     */
    public String getChannelList(Map<String, Object> request) {
        String resp = new String();

        List<ChannelEntity> channelList ;
        PageResult pageResult = new PageResult();

        channelList = this.queryChannelList(request);
        if (channelList != null){
            pageResult.setTotal(channelList.size());
        } else {
            pageResult.setTotal(0);
        }

        pageResult.setList(channelList);

        resp = manageCommonService.getRespBody(pageResult);

        return resp;
    }

    /**
     * 根据map，去数据库查询渠道列表
     * @param request
     * @return
     */
    private List<ChannelEntity> queryChannelList(Map<String, Object> request) {
        return channelMapper.queryChannelList(request);
    }

    /**
     * 根据渠道id获取渠道名称
     * @param channelId
     * @return
     */
    public String getChannelNameByChannelId(Integer channelId) {
        return channelMapper.queryChannelNameByChannelId(channelId);
    }

    /**
     * 获取未配置渠道列表（当前机构没有在使用、被禁用的渠道）
     * @param request
     * @return
     */
    public String availableChannelList(Map<String, Object> request) {
        String resp = new String();

        List<ChannelEntity> channelList= this.getAvailableChannelList(request);

        resp = manageCommonService.getRespBody(channelList);

        return resp;
    }

    public List<ChannelEntity> getAvailableChannelList(Map<String, Object> request) {
        //已配置的渠道id
        List<Integer> channelIds = channelConfigMapper.queryChannelIds(request);

        //所有可用的渠道Id
        List<Integer> allChannelIds = channelMapper.queryChannelIds();

        //如果已经配置了所有的渠道，则不能再配置
        if ( channelIds.retainAll(allChannelIds) ) {
            return null;
        }

        if (channelIds.size() != 0 ){
            request.put("channelIds", channelIds);
        }

        return channelMapper.queryAvailableChannelList(request);
    }

}
