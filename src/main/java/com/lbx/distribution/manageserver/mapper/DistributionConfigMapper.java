package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.channel.DistributionConfig;
import com.lbx.distribution.manageserver.entity.channel.DistributionConfigVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 分发设置mapper
 */
@Mapper
public interface DistributionConfigMapper {
    int deleteByPrimaryKey(Integer confId);

    /**
     * 查询分发设置列表
     * @param requset
     * @return
     */
    List<DistributionConfigVo> queryDistrConfigList(Map<String, Object> requset);

    /**
     * 新增分发设置
     * @param distributionConfigVo
     * @return
     */
    int addDistributeConfig(DistributionConfigVo distributionConfigVo);

    /**
     * 编辑分发规则
     * @param distributionConfigVo
     * @return
     */
    int updateDistrConfig(DistributionConfigVo distributionConfigVo);

    /**
     * 根据组织机构id获取所有的分发规则
     * @param companyIds
     * @return
     */
    List<DistributionConfigVo> queryDistrConfigsByCompanyIds(@Param("companyIds") List<Integer> companyIds);
}