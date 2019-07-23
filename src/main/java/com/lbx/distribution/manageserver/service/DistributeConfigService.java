package com.lbx.distribution.manageserver.service;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.PageResult;
import com.lbx.distribution.manageserver.entity.channel.DistributionConfigVo;
import com.lbx.distribution.manageserver.helper.CommonHelper;
import com.lbx.distribution.manageserver.mapper.DistributionConfigMapper;
import com.lbx.distribution.manageserver.util.DateUtil;
import com.lbx.distribution.manageserver.util.ManageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 分发设置service
 */
@Service
public class DistributeConfigService {

    private static Logger logger = LoggerFactory.getLogger(DistributeConfigService.class);

    @Autowired
    private DistributionConfigMapper distrConfigMapper;
    @Autowired
    ManageCommonService manageCommonService;
    @Autowired
    private DistributeConfigCommonService dcCommonService;
    @Autowired
    private CommonHelper commonHelper;

    /**
     * 获取分发规则列表（支持分页）【只展示不在禁用或删除状态的机构的】
     * @param request
     * @return
     */
    public String getDistrConfigList(Map<String, Object> request) {
        String resp = new String();

        PageResult pageResult = dcCommonService.getDistrConfigList(request);

        resp = manageCommonService.getRespBody(pageResult);

        return resp;
    }

    /**
     * 新增分发规则
     * @param distributionConfigVo
     * @return
     */
    @Transactional
    public String addDistributeConfig(DistributionConfigVo distributionConfigVo) {
        return dcCommonService.addDistributeConfig(distributionConfigVo);
    }

    /**
     * 编辑分发规则
     * @param distributionConfigVo
     * @return
     */
    @Transactional
    public String editDistributeConfig(DistributionConfigVo distributionConfigVo) {
        //转换时间类型
        distributionConfigVo = dcCommonService.parseLongByDate(distributionConfigVo);

        //判断是否已经有该时间段的分发规则，如果已经有则不能新增（比如9:00~10:00已经有规则，则9:30~11:00的规则不能新增）
        dcCommonService.isRepeat(distributionConfigVo);

        distributionConfigVo.setUpdateTime(DateUtil.getCurrentTimestamp());

        if (distrConfigMapper.updateDistrConfig(distributionConfigVo) <= 0 ){
            logger.warn(String.format("updateDistrConfig fail. merchantId: %s /companyId: %s , confId: %s, distributeRule: %s;",
                    distributionConfigVo.getMerchantId(), distributionConfigVo.getCompanyId(), distributionConfigVo.getConfId(), distributionConfigVo.getDistributeRule() ));

            throw new ManageException(ManageResultCode.REQUEST_FAIL, "分发规则编辑失败!");
        }

        String str = "分发规则编辑成功！";
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    /**
     * 更新分发规则状态状态(1.active;0:disable;-1:删除状态)
     * @param distributionConfigVo
     * @return
     */
    public String updateStatus(DistributionConfigVo distributionConfigVo) {

        if (distributionConfigVo.getStatus() == 1) {
            //判断上级机构(商户/公司)是否在启用状态，禁用则不允许启用
            commonHelper.isAvailableHighLevelStatus(distributionConfigVo.getMerchantId(), distributionConfigVo.getCompanyId());
        }

        return dcCommonService.updateStatus(distributionConfigVo);

    }

    /**
     * 删除分发规则（物理删除）
     * @param request
     * @return
     */
    public String delete(Map<String, Object> request) {
        Object confIdObject = request.get("confId");
        if (confIdObject == null) {
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数confId为空!");
        }

        Integer confId = (Integer)confIdObject;

        if (distrConfigMapper.deleteByPrimaryKey(confId) <= 0 ){
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "删除失败!");
        }

        String str = "删除成功！";
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }
}
