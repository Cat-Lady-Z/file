package com.lbx.distribution.manageserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lbx.distribution.manageserver.common.EnumDistributeRuleName;
import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.PageResult;
import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntityVo;
import com.lbx.distribution.manageserver.entity.channel.DistributionConfigVo;
import com.lbx.distribution.manageserver.entity.company.CompanyEntity;
import com.lbx.distribution.manageserver.entity.merchant.MerchantEntity;
import com.lbx.distribution.manageserver.entity.shop.ShopEntityVo;
import com.lbx.distribution.manageserver.mapper.CompanyMapper;
import com.lbx.distribution.manageserver.mapper.DistributionConfigMapper;
import com.lbx.distribution.manageserver.mapper.MerchantMapper;
import com.lbx.distribution.manageserver.mapper.ShopMapper;
import com.lbx.distribution.manageserver.util.DateUtil;
import com.lbx.distribution.manageserver.util.ManageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:  分发设置公共 service
 * @Description:
 */
@Service
public class DistributeConfigCommonService {

    private static Logger logger = LoggerFactory.getLogger(DistributeConfigCommonService.class);

    @Autowired
    private ManageCommonService manageCommonService;

    @Autowired
    private CompanyCommonService companyCommonService;

    @Autowired
    private DistributionConfigMapper distributionConfigMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private CompanyMapper companyMapper;

    /**
     * 获取分发规则列表（支持分页）
     * @param reuest
     * @return
     */
    public PageResult getDistrConfigList(Map<String, Object> reuest) {

        List<DistributionConfigVo> distrConfigList ;

        PageResult pageResult = new PageResult();

        //判断是否需要分页
        Object isPage = reuest.get("isPage");

        if (isPage == null) {
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR);
        }

        //获取所有启用的商户和组织机构
        List<Integer> availableCompanyIds = companyMapper.queryAvailableCompanyId();
        List<Integer> availableMerchantIds = merchantMapper.queryAvailableMerchantId();

        Integer judgePage = (Integer) isPage;
        if (judgePage == 1 ){
            Integer pageSize = manageCommonService.getPageSize(reuest);
            Integer pageNum = manageCommonService.getPageNum(reuest);

            //分页处理
            PageHelper.startPage(pageNum, pageSize);

            distrConfigList = this.queryDistrConfigList(reuest, availableCompanyIds, availableMerchantIds);

            //取记录总条数
            PageInfo<DistributionConfigVo> pageInfo = new PageInfo<>(distrConfigList);
            pageResult.setTotal(pageInfo.getTotal());
            pageResult.setPageNum(pageInfo.getPageNum());
        } else {
            distrConfigList = this.queryDistrConfigList(reuest, availableCompanyIds, availableMerchantIds);
            if (distrConfigList != null){
                pageResult.setTotal(distrConfigList.size());
            } else {
                pageResult.setTotal(0);
            }
        }

        //填充分发规则名称
        distrConfigList = this.setDistrConfigName(distrConfigList);

        //填充分发规则所属机构（商户/组织机构/门店）
        distrConfigList = this.setOrganName(distrConfigList);

        pageResult.setList(distrConfigList);

        return pageResult;
    }

    /**
     * 填充分发规则所属机构
     * @param distrConfigList
     * @return
     */
    private List<DistributionConfigVo> setOrganName(List<DistributionConfigVo> distrConfigList) {

        //填充所属机构名称
        for (DistributionConfigVo dis:distrConfigList  ) {

            //如果是商户，就填写商户名称
            Integer merchantId = dis.getMerchantId();
            if (merchantId != null && merchantId != 0) {
                MerchantEntity merchantEntity = merchantMapper.queryMerchantByMerchantId(merchantId);
                if (merchantEntity != null){
                    dis.setName(merchantEntity.getEnterpriseName());
                }
            }

            //如果是公司，就填公司名称
            Integer companyId = dis.getCompanyId();
            if (companyId != null && companyId != 0) {
                CompanyEntity company = companyMapper.queryCompanyByCompanyId(companyId);
                if (company != null){
                    dis.setName(company.getCompanyName());
                }
            }
        }

        return distrConfigList;
    }

    /**
     * 填充分发规则名称
     * @param distrConfigList
     * @return
     */
    private List<DistributionConfigVo> setDistrConfigName(List<DistributionConfigVo> distrConfigList) {
        List<DistributionConfigVo> resultList = new ArrayList<>();
        if (distrConfigList == null)
            return null;

        for (DistributionConfigVo dis:distrConfigList  ) {
            Integer distributeRule = dis.getDistributeRule();
            dis.setDistributeRuleName(EnumDistributeRuleName.getRuleName(distributeRule));

            resultList.add(dis);
        }

        return resultList;
    }

    /**
     * 从数据库查询分发规则列表【只展示不在禁用或删除状态的机构的】
     * @param params
     * @return
     */
    public List<DistributionConfigVo> queryDistrConfigList(Map<String, Object> params, List<Integer> availableCompanyIds,  List<Integer> availableMerchantIds) {
        Object merchantIdObject = params.get("merchantId");
        Object companyIdObject = params.get("companyId");

        List<DistributionConfigVo> distrConfigList = null;
        if (merchantIdObject == null && companyIdObject == null) {
            if (availableMerchantIds != null && availableMerchantIds.size() > 0) {
                if (availableMerchantIds.size() > 1) {
                    params.put("merchantIds", availableMerchantIds);
                } else {
                    params.put("merchantId", availableMerchantIds.get(0));
                }
                distrConfigList = distributionConfigMapper.queryDistrConfigList(params);
            }
        } else {
            distrConfigList = distributionConfigMapper.queryDistrConfigList(params);
        }

        if (distrConfigList == null){
            return null;
        }

        List<DistributionConfigVo> resultList = new ArrayList<>();
        //转换起始时间类型
        for (DistributionConfigVo dis:distrConfigList ) {
            Integer companyId = dis.getCompanyId();
            Integer merchantId = dis.getMerchantId();

            if ( availableCompanyIds.contains(companyId) || availableMerchantIds.contains(merchantId)) {
                Long timeStart = dis.getTimeStart();
                Long timeEnd = dis.getTimeEnd();
                try {
                    String startTime = DateUtil.parseDateStrByLong(timeStart);
                    String endTime = DateUtil.parseDateStrByLong(timeEnd);
                    dis.setStartTime(startTime);
                    dis.setEndTime(endTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                resultList.add(dis);
            }
        }

        return resultList;
    }

    /**
     * 新增分发规则(目前只有价格优先规则，暂时写死)
     * @param distributionConfigVo
     * @return
     */
    @Transactional
    public String addDistributeConfig(DistributionConfigVo distributionConfigVo) {
        //转换时间类型
        distributionConfigVo = this.parseLongByDate(distributionConfigVo);

        //判断是否已经有该时间段的分发规则，如果已经有则不能新增（比如9:00~10:00已经有规则，则9:30~11:00的规则不能新增）
        this.isRepeat(distributionConfigVo);

        if (distributionConfigMapper.addDistributeConfig(distributionConfigVo) <= 0 ){
            logger.warn(String.format("addDistributeConfig success. merchantId: %s /companyId: %s , distributeRule: %s;",
                    distributionConfigVo.getMerchantId(), distributionConfigVo.getCompanyId(), distributionConfigVo.getDistributeRule() ));

            throw new ManageException(ManageResultCode.REQUEST_FAIL, "分发设置新增失败!");
        }

        logger.info(String.format("addDistributeConfig success. merchantId: %s /companyId: %s , distributeRule: %s;",
                distributionConfigVo.getMerchantId(), distributionConfigVo.getCompanyId(), distributionConfigVo.getDistributeRule() ));
        String str = "分发设置新增成功！";
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    /**
     * 判断是否已经有该时间段的分发规则，如果已经有则不能新增（比如9:00~10:00已经有规则，则9:30~11:00的规则不能新增，包括首位结点9点10点）
     * @param distributionConfigVo
     */
    public void isRepeat(DistributionConfigVo distributionConfigVo) {
        Map<String, Object> map = new HashMap<>();
        Long timeStart = distributionConfigVo.getTimeStart();
        Long timeEnd = distributionConfigVo.getTimeEnd();
        Integer merchantId = distributionConfigVo.getMerchantId();
        Integer companyId = distributionConfigVo.getCompanyId();
        Integer shopId = distributionConfigVo.getShopId();
        Integer confId = distributionConfigVo.getConfId();//针对更改分发规则的

        map.put("merchantId", merchantId);
        map.put("companyId", companyId);
        map.put("shopId", shopId);
        map.put("confId", confId);

        //只判断本级自定义的时间段是否有冲突
        List<DistributionConfigVo> dcList = distributionConfigMapper.queryDistrConfigList(map);

        for (DistributionConfigVo dc:dcList  ) {
            Long start = dc.getTimeStart();
            Long end = dc.getTimeEnd();

            if ( ((timeStart >= start) && (timeStart <= end)) || ((timeEnd >= start) && (timeEnd <= end)) ){
                logger.warn(String.format("该时间段内已有在使用的分发规则! merchantId: %s/companyId: %s , companyName: %s, " +
                                "timeStart: %s, timeEnd: %s;", distributionConfigVo.getMerchantId(),
                        distributionConfigVo.getCompanyId(), timeStart, timeEnd));
                throw new ManageException(ManageResultCode.REQUEST_FAIL, "该时间段内已有在使用的分发规则!");
            }
        }

    }

    /**
     * 将日期的String类型转换为long类型
     * @param distributionConfigVo
     * @return
     */
    public DistributionConfigVo parseLongByDate(DistributionConfigVo distributionConfigVo) {
        try {
            Long timeStart = DateUtil.parseLongByDate(distributionConfigVo.getStartTime());
            Long timeEnd = DateUtil.parseLongByDate(distributionConfigVo.getEndTime());

            if (timeEnd < timeStart){
               logger.warn(String.format("起始时间选择错误! merchantId: %s/companyId: %s , companyName: %s, " +
                                "timeStart: %s, timeEnd: %s;", distributionConfigVo.getMerchantId(),
                        distributionConfigVo.getCompanyId(), timeStart, timeEnd));
                throw new ManageException(ManageResultCode.REQUEST_FAIL, "起始时间选择错误!");
            }

            distributionConfigVo.setTimeStart(timeStart);
            distributionConfigVo.setTimeEnd(timeEnd);
        } catch (ParseException e) {
            logger.warn(String.format("起始时间格式转换错误! merchantId: %s/companyId: %s , companyName: %s, " +
                            "timeStart: %s, timeEnd: %s;", distributionConfigVo.getMerchantId(),
                    distributionConfigVo.getCompanyId(), distributionConfigVo.getStartTime(), distributionConfigVo.getEndTime()));
            e.printStackTrace();
        }

        return distributionConfigVo;
    }

    /**
     * 更新分发规则状态
     * @param dc
     * @return
     */
    @Transactional
    public String updateStatus(DistributionConfigVo dc) {
        dc.setUpdateTime(DateUtil.getCurrentTimestamp());
        if (distributionConfigMapper.updateDistrConfig(dc) <= 0 ){
            logger.warn(String.format("状态更新失败! merchantId: %s/companyId: %s , confId: %s;" , dc.getMerchantId(),
                    dc.getCompanyId(), dc.getConfId()));
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "操作失败!");
        }
        String str = "操作成功！";
        logger.warn(String.format("状态更新成功! merchantId: %s/companyId: %s , confId: %s;" , dc.getMerchantId(),
                dc.getCompanyId(), dc.getConfId()));
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }
}
