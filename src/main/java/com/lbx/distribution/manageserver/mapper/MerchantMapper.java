package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.MenuEntity;
import com.lbx.distribution.manageserver.entity.MenuRequest;
import com.lbx.distribution.manageserver.entity.merchant.MerchantEntity;
import com.lbx.distribution.manageserver.entity.merchant.MerchantEntityVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商户Mapper
 */
@Mapper
public interface MerchantMapper {

    /**
     * 新增商户
     * @param merchantEntityVo
     * @return
     */
    int insertMerchant(MerchantEntityVo merchantEntityVo);

    /**
     * 更新商户信息
     * @param merchantEntityVo
     * @return
     */
    int updateMerchant(MerchantEntityVo merchantEntityVo);

    /**
     * 更新商户状态
     * @param merchantEntityVo
     * @return
     */
    int updateMerchantStatusByMerchantId(MerchantEntityVo merchantEntityVo);

    /**
     * 更改商户密码
     * @param merchantEntityVo
     * @return
     */
    int updatePasswordByMerchantId(MerchantEntityVo merchantEntityVo);

    /**
     * 删除商户
     * @param merchant
     * @return
     */
    int deleteByMerchantId(MerchantEntity merchant );

    /**
     * 查询商户列表(/分页)
     * @param
     * @return
     */
    List<MerchantEntity> queryMerchantList(Map<String, Object> manageSimpleRequest);

    /**
     * 查询商户总数
     * @param
     * @return
     */
    int findTotalNumber();

    /**
     * 查询商户
     * @param merchantId
     * @return
     */
    MerchantEntity queryMerchantByMerchantId(@Param("merchantId") int merchantId);

    /**
     * 确认账号是否已存在（账号即商户名称）
     * @param merchantName
     * @return
     */
    MerchantEntity isExist(@Param("merchantName") String merchantName);

    /**
     *获取商户菜单
     */
    List<MenuEntity> queryMerchantMenu(MenuRequest rq);

    /**
     * 获取所有可用的商户
     * @return
     */
    List<MenuEntity> queryMerchants();

    /**
     * 查询所有可用的商户id
     * @return
     */

    List<Integer> queryAvailableMerchantId();

    /**
     * 查询所有的商户Id(除超级管理员 role：0)
     * @return
     */
    List<Integer> queryAllMerchantIds();

    /**
     * 多条件查询单个商户
     * @param merchantEntityVo
     * @return
     */
    MerchantEntity queryMerchantMultiCond(MerchantEntityVo merchantEntityVo);

    MerchantEntity queryAllMerchantByMerchantId(Integer merchantId);
}
