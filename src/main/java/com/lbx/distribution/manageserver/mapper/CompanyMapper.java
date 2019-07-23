package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.MenuEntity;
import com.lbx.distribution.manageserver.entity.MenuRequest;
import com.lbx.distribution.manageserver.entity.company.CompanyEntity;
import com.lbx.distribution.manageserver.entity.company.CompanyEntityVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Interface: 单位/分支管理公司Mapper
 * @Description:
 */
@Mapper
public interface CompanyMapper {
    /**
     * 新增公司
     * @param companyEntity
     * @return
     */
    int insertCompany(CompanyEntity companyEntity);

    /**
     * 更新公司信息
     * @param companyEntity
     * @return
     */
    int updateCompany(CompanyEntity companyEntity);

    /**
     * 查询公司列表，支持分页查询，多条件查询
     * @param
     * @return
     */
    List<CompanyEntityVo> queryCompanyList(Map<String, Object> request);

    /**
     * 查询公司总数
     * @param
     * @return
     */
    int findTotalNumber();

    /**
     * 根据公司id查询公司
     * @param companyId
     * @return
     */
    CompanyEntity queryCompanyByCompanyId(@Param("companyId") int companyId);


    /**
     * 删除公司
     * @param companyId
     * @return
     */
    int deleteByCompanyId(int companyId);

    /**
     * 获取组织机构的菜单信息
     * @param rq
     * @return
     */
    List<MenuEntity> getCompanyMenu(MenuRequest rq);

    /**
     * 根据子类组织机构的Id查出所有父类组织机构id
     * @param companyId
     * @return
     */
    List<Integer> querySuperCompanyIdList(@Param("companyId")Integer companyId);

    /**
     * 获取所有可用的公司
     * @return
     */
    List<Integer> queryAvailableCompanyId();

    /**
     *  更新公司状态
     * @param map
     * @return
     */
    int updateCompanyStatus(Map<String, Object> map);

    /**
     * 根据父id获取下层所有的公司数目
     * @param parentId
     * @return
     */
    List<Integer> findCompanyIdsByParentId(@Param("parentId") Integer parentId);

    /**
     * 根据多个条件查询单个公司
     * @param map
     * @return
     */
    CompanyEntity queryCompany(Map<String, Object> map);

    /**
     * 查询商户id 下层的公司id
     * @param merchantId
     * @return
     */
    List<Integer> queryCompanyIdsByMerchantId(@Param("merchantId") Integer merchantId);
}
