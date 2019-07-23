package com.lbx.distribution.manageserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lbx.distribution.manageserver.common.BaseCommon;
import com.lbx.distribution.manageserver.common.ChannelDataEnum;
import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.MenuEntity;
import com.lbx.distribution.manageserver.entity.PageResult;
import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntityVo;
import com.lbx.distribution.manageserver.entity.channel.ChannelEntity;
import com.lbx.distribution.manageserver.entity.company.CompanyEntity;
import com.lbx.distribution.manageserver.entity.merchant.MerchantEntity;
import com.lbx.distribution.manageserver.mapper.ChannelConfigMapper;
import com.lbx.distribution.manageserver.mapper.ChannelMapper;
import com.lbx.distribution.manageserver.mapper.CompanyMapper;
import com.lbx.distribution.manageserver.mapper.MerchantMapper;
import com.lbx.distribution.manageserver.util.DateUtil;
import com.lbx.distribution.manageserver.util.ManageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: 组织机构 列表 service
 * @Description:
 */
@Service
public class ChannelConfigCommonService {

    private static Logger logger = LoggerFactory.getLogger(ChannelConfigService.class);

    @Autowired
    private ManageCommonService manageCommonService;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private ChannelConfigMapper channelConfigMapper;
    @Autowired
    private ChannelMapper channelMapper;

    /**
     * 根据map，去数据库查询可用机构的配置渠道列表
     *
     * @param params
     * @return
     */
    public List<ChannelConfigEntityVo> queryChannelConfigList(Map<String, Object> params, List<Integer> availableCompanyIds, List<Integer> availableMerchantIds) {
        Object merchantIdObject = params.get("merchantId");
        Object companyIdObject = params.get("companyId");

        //确认是否是超级管理员。超级管理员查看所有商户信息
        List<ChannelConfigEntityVo> channelConfigList = new ArrayList<>();

        if (merchantIdObject == null && companyIdObject == null) {
            if (availableMerchantIds != null && availableMerchantIds.size() > 0) {
                Map<String, Object> merchantParams = params;
                if (availableMerchantIds.size() > 1) {
                    merchantParams.put("merchantIds", availableMerchantIds);
                } else {
                    merchantParams.put("merchantId", availableMerchantIds.get(0));
                }
                List<ChannelConfigEntityVo> merchantChannelConfigList = channelConfigMapper.queryChannelConfigList(merchantParams);
                if (merchantChannelConfigList != null) {
                    channelConfigList.addAll(merchantChannelConfigList);
                }
            }
        } else {
            channelConfigList = channelConfigMapper.queryChannelConfigList(params);
        }

        return channelConfigList;
    }

    /**
     * 获取渠道配置列表（支持分页）【禁用/删除的商户/组织机构不出现】
     *
     * @param request
     * @return
     */
    public PageResult getChannelConfigList(Map<String, Object> request) {

        List<ChannelConfigEntityVo> channelConfigList;

        PageResult pageResult = new PageResult();

        //判断是否需要分页
        Object isPage = request.get("isPage");

        if (isPage == null) {
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR);
        }

        //获取所有启用的商户和组织机构
        List<Integer> availableCompanyIds = companyMapper.queryAvailableCompanyId();
        List<Integer> availableMerchantIds = merchantMapper.queryAvailableMerchantId();

        Integer judgePage = (Integer) isPage;
        if (judgePage == 1) {
            Integer pageSize = manageCommonService.getPageSize(request);
            Integer pageNum = manageCommonService.getPageNum(request);

            //分页处理
            PageHelper.startPage(pageNum, pageSize);
            channelConfigList = this.queryChannelConfigList(request, availableCompanyIds, availableMerchantIds);
            //取记录总条数
            PageInfo<ChannelConfigEntityVo> pageInfo = new PageInfo<>(channelConfigList);
            pageResult.setTotal(pageInfo.getTotal());
            pageResult.setPageNum(pageInfo.getPageNum());

        } else {
            channelConfigList = this.queryChannelConfigList(request, availableCompanyIds, availableMerchantIds);
            if (channelConfigList != null) {
                pageResult.setTotal(channelConfigList.size());
            } else {
                pageResult.setTotal(0);
            }
        }

        //填充商户/组织机构名称
        this.setName(channelConfigList, availableCompanyIds, availableMerchantIds);

        pageResult.setList(channelConfigList);

        return pageResult;
    }

    /**
     *
     */
    private void setName(List<ChannelConfigEntityVo> channelConfigList, List<Integer> availableCompanyIds, List<Integer> availableMerchantIds) {
        if (channelConfigList != null && channelConfigList.size() != 0) {
            List<ChannelConfigEntityVo> resultList = new ArrayList<>();
            //填充所属机构名称
            for (ChannelConfigEntityVo chan : channelConfigList) {
                //如果是商户，就填写商户名称
                Integer merchantId = chan.getMerchantId();
                //判断商户是否在可用列表里
                if (merchantId != 0 && merchantId != null) {
                    if (availableMerchantIds.contains(merchantId)) {
                        if (merchantId != null && merchantId != 0) {
                            MerchantEntity merchantEntity = merchantMapper.queryMerchantByMerchantId(merchantId);
                            if (merchantEntity != null) {
                                chan.setName(merchantEntity.getEnterpriseName());
                            }
                        }
                        resultList.add(chan);
                    }
                    continue;
                }

                //如果是公司，就填公司名称
                Integer companyId = chan.getCompanyId();
                if (availableCompanyIds.contains(companyId)) {
                    if (companyId != null && companyId != 0) {
                        CompanyEntity company = companyMapper.queryCompanyByCompanyId(companyId);
                        if (company != null) {
                            chan.setName(company.getCompanyName());
                        }
                    }
                    resultList.add(chan);
                }
            }
        }
    }

    /**
     * 新增渠道
     *
     * @param vo
     * @return
     */
    public void addChannelConfig(ChannelConfigEntityVo vo) {
        //校验参数
        this.checkDataLegal(vo);

        this.insertChannelConfig(vo);

        logger.info(String.format("addChannelConfig success. merchantId: %s/companyId: %s, channelId: %s;",
                vo.getMerchantId(), vo.getCompanyId(), vo.getChannelId()));
    }

    //校验参数
    private void checkDataLegal(ChannelConfigEntityVo vo) {
        Integer companyId = vo.getCompanyId();
        Integer merchantId = vo.getMerchantId();
        if (companyId == null && merchantId == null) {
            logger.warn("请求参数错误! 无机构id！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 无机构id！");
        }

        MerchantEntity merchantEntity = null;
        if (merchantId != null) {
            merchantEntity = merchantMapper.queryMerchantByMerchantId(merchantId);
        }
        CompanyEntity companyEntity = null;
        if (companyId != null) {
            companyEntity = companyMapper.queryCompanyByCompanyId(companyId);
        }

        if (merchantEntity == null && companyEntity == null) {
            logger.warn("请求参数错误! 无对应商户/公司信息！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 无对应商户/公司信息！");
        }

        Integer channelId = vo.getChannelId();
        if (channelId == null || channelId == 0) {
            logger.warn("请求参数错误! 无渠道id！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 无渠道id！");
        }

        if (channelId == ChannelDataEnum.DADA__CODE) {
            String channelSourceId = vo.getChannelSourceId();
            if (channelSourceId == null || channelSourceId.replaceAll(" ", "").length() == 0) {
                logger.warn("请求参数错误! 缺少对应渠道 商户/公司id！");
                throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 对应渠道 商户/公司id！");
            }
            String channelSourceIdReplace = channelSourceId.replaceAll(" ", "");
            vo.setChannelSourceId(channelSourceIdReplace);
        }


        //判断商户/公司是否已配置该渠道
        Map<String, Object> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("companyId", companyId);
        List<Integer> channelIds = channelConfigMapper.queryChannelIds(params);
        if (channelIds.contains(channelId)) {
            logger.warn("商户/公司已配置该渠道！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "商户/公司已配置该渠道！");
        }

        String appkey = vo.getAppkey();
        if (appkey == null || appkey.replaceAll(" ", "").length() == 0) {
            logger.warn("请求参数错误! 请输入AppKey！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 请输入AppKey！");
        }
        String appkeyReplace = appkey.replaceAll(" ", "");
        vo.setAppkey(appkeyReplace);

        String secret = vo.getSecret();
        if (secret == null || secret.replaceAll(" ", "").length() == 0) {
            logger.warn("请求参数错误! 请输入secret！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 请输入secret！");
        }
        String secretReplace = secret.replaceAll(" ", "");
        vo.setSecret(secretReplace);
    }

    public int insertChannelConfig(ChannelConfigEntityVo vo) {
        //状态(1.active;0:disable)
        //默认新建后处于禁用状态
        vo.setStatus(0);
        vo.setCreateTime(DateUtil.getCurrentTimestamp());
        int i = channelConfigMapper.addChannelConfig(vo);
        if (i <= 0) {
            logger.warn(String.format("addChannelConfig fail. merchantId: %s/companyId: %s, channelId: %s;",
                    vo.getMerchantId(), vo.getCompanyId(), vo.getChannelId()));

            throw new ManageException(ManageResultCode.REQUEST_FAIL, "新增渠道配置失败!");
        }
        return i;
    }

    /**
     * 根据公司Id获取所有配置的渠道列表
     *
     * @param companyIds
     * @return
     */
    public List<ChannelConfigEntityVo> queryChannelsByCompanyIds(List<Integer> companyIds) {
        if (companyIds == null)
            return null;

        return channelConfigMapper.queryChannelsByCompanyIds(companyIds);

    }
}
