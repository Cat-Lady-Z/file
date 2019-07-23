package com.lbx.distribution.manageserver.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.PageResult;
import com.lbx.distribution.manageserver.entity.SimpleRequest;
import com.lbx.distribution.manageserver.entity.channel.*;

import com.lbx.distribution.manageserver.entity.company.CompanyEntity;
import com.lbx.distribution.manageserver.entity.merchant.MerchantToken;
import com.lbx.distribution.manageserver.helper.CommonHelper;
import com.lbx.distribution.manageserver.mapper.*;
import com.lbx.distribution.manageserver.util.DateUtil;
import com.lbx.distribution.manageserver.util.ManageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 渠道service
 */
@Service
public class ChannelConfigService {
    private static Logger logger = LoggerFactory.getLogger(ChannelConfigService.class);
    private static final Integer UPDATE_PriceConfig = 2;
    private static final Integer ADD_PriceConfig = 1;

    @Autowired
    private ChannelConfigMapper channelConfigMapper;
    @Autowired
    ManageCommonService manageCommonService;
    @Autowired
    private ChannelConfigCommonService commonService;
    @Autowired
    private RegionPriceMapper regionPriceMapper;
    @Autowired
    private OrderDeliveryMarkupMapper orderDeliveryMarkupMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private CommonHelper commonHelper;
    @Autowired
    private MerchantMapper merchantMapper;

    /**
     * 获取渠道列表（支持分页）
     * @param request
     * @return
     */
    public String getChannelConfigList(Map<String, Object> request) {
        String resp = new String();

        PageResult pageResult = commonService.getChannelConfigList(request);

        resp = manageCommonService.getRespBody(pageResult);

        return resp;
    }

    /**
     * 新增渠道，商户/组织机构配置新渠道
     * @param vo
     * @return
     */
    @Transactional
    public String addChannelConfig(ChannelConfigEntityVo vo) {
        commonService.addChannelConfig(vo);

        String str = "新增渠道配置成功！";
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    /**
     * 编辑配置渠道信息
     * @param channelConfigEntity
     * @return
     */
    @Transactional
    public String updateChannelConfig(ChannelConfigEntity channelConfigEntity) {
        //获取本条渠道配置信息
        ChannelConfigEntity temp = channelConfigMapper.queryChannelConfigByConfId(channelConfigEntity.getConfId());
        if (temp == null) {
            logger.error(String.format("无渠道配置信息！confId: %s;" + channelConfigEntity.getConfId()));
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "无渠道配置信息!");
        }

        channelConfigEntity.setUpdateTime(DateUtil.getCurrentTimestamp());
       //key和密钥校验
        String secret = channelConfigEntity.getSecret();
        if ( secret == null || secret.replaceAll(" ", "").length() == 0 ) {
            logger.warn(String.format("请求参数错误! 请输入secret！"));
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 请输入secret！");
        }
        String secretReplace = secret.replaceAll(" ", "");
        channelConfigEntity.setSecret(secretReplace);

        String appkey = channelConfigEntity.getAppkey();
        if ( appkey == null || appkey.replaceAll(" ", "").length() == 0 ) {
            logger.warn(String.format("请求参数错误! 请输入AppKey！"));
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 请输入AppKey！");
        }
        String appkeyReplace = appkey.replaceAll(" ", "");
        channelConfigEntity.setAppkey(appkeyReplace);

        if (channelConfigMapper.updateChannelConfig(channelConfigEntity) <= 0 ){
            logger.error(String.format("编辑配置渠道信息失败！"));
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "编辑失败!");
        }

        String str = "编辑成功！";
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    /**
     * 更新状态（状态(1.active;0:disable)）
     * @param channelConfigEntity
     * @return
     */
    @Transactional
    public String updateStatus(ChannelConfigEntity channelConfigEntity) {
        //获取本条渠道配置信息
        ChannelConfigEntity temp = channelConfigMapper.queryChannelConfigByConfId(channelConfigEntity.getConfId());
        //查询对应的价格配置信息
        List<RegionPriceEntity> regionPrices = regionPriceMapper.queryRegionEntityByChannelConfig(temp);
        List<OrderDeliveryMarkupEntity> orderDeliveryMarkups = orderDeliveryMarkupMapper.updateOrderDeliveryMarkupByChannelConfig(temp);

        if ( channelConfigEntity.getStatus() == 1 ) {
            //判断上级机构(商户/公司)是否在启用状态，禁用则不允许启用
            commonHelper.isAvailableHighLevelStatus(channelConfigEntity.getMerchantId(), channelConfigEntity.getCompanyId());

            //如果是其启用配置渠道，需要先检查是否有价格配置。如果有，才允许启用
            if ( regionPrices == null || regionPrices.size()== 0 || orderDeliveryMarkups == null || orderDeliveryMarkups.size() == 0 ) {
                logger.warn(String.format("没有价格配置，不能启用！confId: %s;", channelConfigEntity.getConfId()));
                throw new ManageException(ManageResultCode.REQUEST_FAIL, "请先进行价格配置!");
            }
        }

        channelConfigEntity.setUpdateTime(DateUtil.getCurrentTimestamp());

        if (channelConfigMapper.updateStatus(channelConfigEntity) <= 0 ){
            logger.error(String.format("状态更新失败!"));
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "状态更新失败!");
        }

        String str = "状态更新成功！";
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    /**
     * 删除已配置的渠道（物理删除)）
     * @param request
     * @return
     */
    @Transactional
    public String deleteChannelConfig( Map<String, Object> request, HttpServletRequest httpServletRequest){
        Object confIdObject = request.get("confId");
        if (confIdObject == null) {
            logger.error(String.format("无配置渠道id！"));
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "无配置渠道id!");
        }

        Integer confId = (Integer)confIdObject;
        ChannelConfigEntity temp = channelConfigMapper.queryChannelConfigByConfId(confId);

        //日志记录操作
        Object userObject = httpServletRequest.getAttribute("user");
        if (userObject != null) {
            MerchantToken user = (MerchantToken)userObject;
            Integer merchantId = user.getMerchantId();
            commonHelper.isAvailableMerchantStatus(merchantId);
            logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起删除配置渠道操作，confId: %s, channelId: %s, merchantId: %s, companyId: %s,;",
                    httpServletRequest.getRemoteAddr(),merchantId, user.getMerchantName(), temp.getConfId(),temp.getChannelId(), temp.getMerchantId(), temp.getCompanyId()));
        } else {
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
        }
        //删除配置渠道
        int deleteSuccess = channelConfigMapper.deleteByPrimaryKey(confId);

        //删除对应的价格配置
        if (deleteSuccess > 0) {
            List<RegionPriceEntity> regionPrices = regionPriceMapper.queryRegionEntityByChannelConfig(temp);
            List<OrderDeliveryMarkupEntity> orderDeliveryMarkups = orderDeliveryMarkupMapper.updateOrderDeliveryMarkupByChannelConfig(temp);
            if (regionPrices != null && regionPrices.size() != 0 ) {
                for (RegionPriceEntity regionPriceEntity:regionPrices ) {
                    regionPriceMapper.deleteRegionPrice(regionPriceEntity.getId());
                }
            }
            if (orderDeliveryMarkups != null && orderDeliveryMarkups.size() != 0) {
                for (OrderDeliveryMarkupEntity orderDeliveryMarkupEntity:orderDeliveryMarkups ) {
                    orderDeliveryMarkupMapper.deleteOrderDeliveryMarkup(orderDeliveryMarkupEntity.getId());
                }
            }
        } else {
            logger.error(String.format("配置渠道删除失败！"));
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "配置渠道删除失败!");
        }

        String str = "删除成功！";
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }


    /**
     * 新增价格配置
     * @param request
     * @return
     */
    @Transactional
    public String addPriceConfig(SimpleRequest request, HttpServletRequest httpServletRequest, Integer flag){
        JSONObject params = (JSONObject)JSONArray.toJSON(request.getData());
        JSONArray cityPriceArray = params.getJSONArray("cityPrice");
        JSONArray markupConditionArray = params.getJSONArray("markupCondition");
        for (Object obj : cityPriceArray) {
            JSONObject param = (JSONObject)JSONObject.toJSON(obj);
            RegionPriceEntity cityBasePriceEntity = JSONObject.toJavaObject(param, RegionPriceEntity.class);

            //判断上级机构(商户/公司)是否在启用状态，禁用则不允许新增
            commonHelper.isAvailableHighLevelStatus(cityBasePriceEntity.getMerchantId(), cityBasePriceEntity.getCompanyId());

            //日志记录操作
            int index = 0;
            if (index==0) {
                Object userObject = httpServletRequest.getAttribute("user");
                if (userObject != null) {
                    MerchantToken user = (MerchantToken)userObject;
                    Integer merchantId = user.getMerchantId();
                    commonHelper.isAvailableMerchantStatus(merchantId);
                    if (flag == UPDATE_PriceConfig) {
                        logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起更新价格配置操作，配置所属机构--merchantId: %s / companyId: %s;",
                                httpServletRequest.getRemoteAddr(),merchantId, user.getMerchantName(),cityBasePriceEntity.getMerchantId(), cityBasePriceEntity.getCompanyId()));
                    } else {
                        logger.info(String.format("IP:%s, merchantId:%s ,merchantName:%s 发起新增价格配置操作，配置所属机构--merchantId: %s / companyId: %s;",
                                httpServletRequest.getRemoteAddr(),merchantId, user.getMerchantName(),cityBasePriceEntity.getMerchantId(), cityBasePriceEntity.getCompanyId()));
                    }
                } else {
                    throw new ManageException(ManageResultCode.REQUEST_FAIL, "未登录/登录过期，请重新登录！");
                }
            }
            index++;

            //插入数据
            regionPriceMapper.insertRegionPrice(cityBasePriceEntity);

        }
        for (Object obj : markupConditionArray) {
            JSONObject param = (JSONObject)JSONObject.toJSON(obj);
            OrderDeliveryMarkupEntity markupEntity = JSONObject.toJavaObject(param, OrderDeliveryMarkupEntity.class);
            //判断上级机构(商户/公司)是否在启用状态，禁用则不允许新增
            commonHelper.isAvailableHighLevelStatus(markupEntity.getMerchantId(), markupEntity.getCompanyId());

            //插入数据
            orderDeliveryMarkupMapper.insertOrderDeliveryMarkup(markupEntity);
        }
        String str = "新增价格配置成功！";
        String resp = manageCommonService.getRespBody(str);
        return  resp;
    }

    /**
     * 更新价格配置
     * @param request
     * @return
     */
    @Transactional
    public String updatePriceConfig(SimpleRequest request, HttpServletRequest httpServletRequest){
        deletePriceConfig(request);
        addPriceConfig(request, httpServletRequest, UPDATE_PriceConfig);
        String str = "更新价格配置成功！";
        String resp = manageCommonService.getRespBody(str);
        return  resp;
    }

    /**
     * 删除价格配置
     * @param request
     * @return
     */
    @Transactional
    public void deletePriceConfig(SimpleRequest request){
        JSONObject requestJson = (JSONObject)JSONObject.toJSON(request.getData());
        JSONArray basePriceArray = requestJson.getJSONArray("cityPrice");
        JSONArray markupConditionArray = requestJson.getJSONArray("markupCondition");
        Map<String,Integer> params = new HashMap<>();
        if(basePriceArray != null && basePriceArray.size() > 0){
            JSONObject basePriceJson = (JSONObject)JSONObject.toJSON(basePriceArray.get(0));
            Integer channelId = basePriceJson.getInteger("channel_id");
            Integer merchantId = basePriceJson.getInteger("merchant_id");
            Integer companyId = basePriceJson.getInteger("company_id");
            params.put("channelId",channelId);
            params.put("merchantId",merchantId);
            params.put("companyId",companyId);
            //删除城市基础价格
            regionPriceMapper.deleteRegionPriceByMerchantIdOrCompanyId(params);
        }
        if(markupConditionArray != null && markupConditionArray.size() > 0){
            JSONObject markupJson = (JSONObject)JSONObject.toJSON(markupConditionArray.get(0));
            Integer channelId = markupJson.getInteger("channel_id");
            Integer companyId = markupJson.getInteger("company_id");
            Integer merchantId = markupJson.getInteger("merchant_id");
            params.put("channelId",channelId);
            params.put("merchantId",merchantId);
            params.put("companyId",companyId);
            //删除价格配置
            orderDeliveryMarkupMapper.deleteOrderDeliveryMarkupByMerchantIdOrCompanyId(params);
        }
    }


    /**
     * 查询价格配置
     * @return
     */
    public String queryPriceConfig(SimpleRequest request){
        //查询城市基础价格
        List<RegionPriceEntity> regionPriceList = queryCityBasePrice(request);
        List<OrderDeliveryMarkupEntity> orderDeliveryMarkupList = queryMarkupCondition(request);
        JSONObject resultJson = new JSONObject();
        resultJson.put("regionPrice",regionPriceList);
        resultJson.put("deliveryMarkup",orderDeliveryMarkupList);
        String resp = manageCommonService.getRespBody(resultJson);
        return resp;
    }

    /**
     * 查询城市基础价格
     * @param request
     * @return
     */
    public List<RegionPriceEntity> queryCityBasePrice(SimpleRequest request){
        JSONObject params = (JSONObject)JSONObject.toJSON(request.getData());
        Integer companyId = params.getInteger("company_id");
        Integer merchantId = params.getInteger("merchant_id");
        Integer channelId = params.getInteger("channel_id");
        List<RegionPriceEntity> regionPriceList;
        //商户id不为空查询商户配置
        if(merchantId != null && Integer.valueOf(0).compareTo(merchantId) != 0){
            regionPriceList = regionPriceMapper.selectByMerchantId(merchantId,channelId);
            return regionPriceList;
        }
        if((merchantId == null || Integer.valueOf(0).compareTo(merchantId) == 0) && companyId == null ){
            throw new ManageException(ManageResultCode.NULL_Exception, "公司id为空！");
        }
        //根据公司id获取城市基础价格
        regionPriceList = regionPriceMapper.selectByCompanyId(companyId,channelId);
        //根据商户id获取城市基础价格
        if(StringUtils.isEmpty(regionPriceList) || regionPriceList.size() <= 0 ){
            //轮询父公司配置信息
            regionPriceList = queryCompanyConfig(regionPriceList,companyId,channelId);
            if(StringUtils.isEmpty(regionPriceList) || regionPriceList.size() <= 0 ){
                //获取此公司的商户id
                CompanyEntity company = companyMapper.queryCompanyByCompanyId(companyId);
                if(!StringUtils.isEmpty(company)){
                    merchantId = company.getMerchantId();
                    //查询商户配置信息
                    regionPriceList = regionPriceMapper.selectByMerchantId(merchantId,channelId);
                }
            }
        }
        return regionPriceList;
    }

    /**
     * 因公司使用无限级继承，轮询公司信息
     * @return
     */
    public List<RegionPriceEntity> queryCompanyConfig(List<RegionPriceEntity> regionPriceList,Integer companyId,Integer channelId){
        //查询父公司
        CompanyEntity company = companyMapper.queryCompanyByCompanyId(companyId);
        //有父公司则获取配置信息
        if(!StringUtils.isEmpty(company) && Integer.valueOf(0).compareTo(company.getParentId()) != 0){
            Integer parentId = company.getParentId();
            regionPriceList = regionPriceMapper.selectByCompanyId(companyId,channelId);
            //父公司没找到配置
            if(StringUtils.isEmpty(regionPriceList) || regionPriceList.size() <= 0){
                //递归遍历
                regionPriceList = this.queryCompanyConfig(regionPriceList,parentId,channelId);
            }else{
                return regionPriceList;
            }
        }
        return regionPriceList;
    }

    /**
     * 查询加价条件
     * @param request
     * @return
     */
    public List<OrderDeliveryMarkupEntity> queryMarkupCondition(SimpleRequest request){
        JSONObject params = (JSONObject)JSONObject.toJSON(request.getData());
        Integer companyId = params.getInteger("company_id");
        Integer merchantId = params.getInteger("merchant_id");
        Integer channelId = params.getInteger("channel_id");
        //根据公司id获取加价条件
        List<OrderDeliveryMarkupEntity> orderDeliveryMarkupList = orderDeliveryMarkupMapper.selectByCompanyId(companyId, channelId);
        //根据商户id获取加价条件
        if(orderDeliveryMarkupList.size() <= 0 ){
            orderDeliveryMarkupList = orderDeliveryMarkupMapper.selectByMerchantId(merchantId, channelId);
        }
        return orderDeliveryMarkupList;
    }

    /**
     * 获取门店可用的渠道配置下拉菜单
     * @param request
     * @return
     */
    public String getChannelConfigMenu(Map<String, Object> request) {
        String resp = null;

        Object companyId_Object = request.get("companyId");
        if ( companyId_Object == null) {
            throw new ManageException( ManageResultCode.DATA_REQUEST_ERROR );
        }

        Integer companyId = (Integer) companyId_Object;
        CompanyEntity company = companyMapper.queryCompanyByCompanyId(companyId);

        List<ChannelConfigEntityVo> channelConfigList = new ArrayList<>();
        if ( company == null ) {
            return manageCommonService.getRespBody(null);
        }

        Integer merchantId = company.getMerchantId();
        Map<String, Object> map = new HashMap<>();

        List<ChannelConfigEntityVo> merchantChannelConfigList = this.getChannelConfigListByMerchantId(merchantId);

        //自下到上的查询
        List<ChannelConfigEntityVo> companyChannelConfigList = this.getChannelConfigListByCompanyIdFromDown(companyId);

        if (merchantChannelConfigList != null) {
            channelConfigList.addAll(merchantChannelConfigList);
        }

        if ( companyChannelConfigList != null) {
            channelConfigList.addAll(companyChannelConfigList);
        }

        if ( channelConfigList == null ) {
            return manageCommonService.getRespBody(null);
        }

        //封装成menu形式，并去重
        List<ChannelConfigMenu> channelConfigMenus = this.duplicateRemoval(channelConfigList);

        resp = manageCommonService.getRespBody(channelConfigMenus);

        return resp;
    }

    /**
     * 封装成menu形式并去重
     * @param channelConfigList
     * @return
     */
    public List<ChannelConfigMenu> duplicateRemoval(List<ChannelConfigEntityVo> channelConfigList) {
        List<ChannelConfigMenu> channelConfigMenus = new ArrayList<>();

        Set<String> channelNameSet = new HashSet<>();
        for (ChannelConfigEntityVo vo:channelConfigList ) {
            if ( !channelNameSet.contains(vo.getChannelName()) ) {
                channelNameSet.add( vo.getChannelName() );

                ChannelConfigMenu me = new ChannelConfigMenu();
                me.setChannelId(vo.getChannelId());
                me.setChannelName(vo.getChannelName());
                channelConfigMenus.add(me);
            }
        }
        return channelConfigMenus;
    }

    /**
     * 由公司id自下到上的查询配置渠道
     * @param companyId
     * @return
     */
    public List<ChannelConfigEntityVo> getChannelConfigListByCompanyIdFromDown(Integer companyId) {
        Map<String, Object> map = new HashMap<>();
        List<ChannelConfigEntityVo> result = null;

        List<Integer> companyIds = companyMapper.querySuperCompanyIdList(companyId);
        //获取所有启用的商户和组织机构
        List<Integer> availableCompanyIds = companyMapper.queryAvailableCompanyId();
        List<Integer> availableMerchantIds = merchantMapper.queryAvailableMerchantId();
        if ( companyIds != null ) {
            if ( companyIds.size() > 1 ) {
                map.put("companyIds", companyIds);
            } else {
                map.put("companyId", companyIds.get(0) );
            }
            result = commonService.queryChannelConfigList(map, availableCompanyIds, availableMerchantIds);
        }
        return result;
    }

    /**
     *
     * @param merchantId
     */
    public List<ChannelConfigEntityVo> getChannelConfigListByMerchantId(Integer merchantId) {
        Map<String, Object> map = new HashMap<>();
        map.put("merchantId",merchantId);
        //获取所有启用的商户和组织机构
        List<Integer> availableCompanyIds = companyMapper.queryAvailableCompanyId();
        List<Integer> availableMerchantIds = merchantMapper.queryAvailableMerchantId();
        return commonService.queryChannelConfigList(map,availableCompanyIds,availableMerchantIds);
    }
}
