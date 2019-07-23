package com.lbx.distribution.manageserver.mapper;



import com.lbx.distribution.manageserver.entity.shop.ShopConfigImport;
import com.lbx.distribution.manageserver.entity.shop.ShopChannelEntity;
import com.lbx.distribution.manageserver.entity.shop.ShopChannelEntityVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 第三方渠道门店 mapper
 */
@Mapper
public interface ShopChannelMapper {

    int insertSelective(ShopChannelEntity record);

    int updateByPrimaryKeySelective(ShopChannelEntity record);

    /**
     * 增加第三方渠道门店信息
     * @param shopChannelEntity
     * @return
     */
    int addShopChannel( Map<String, Object> shopChannelEntity);

    /**
     * 根据门店信息查询第三方门店信息
     * @param shopId
     * @return
     */
    List<ShopChannelEntityVo> queryShopChannelByShopId(@Param("shopId") Integer shopId);

    /**
     * 删除
     * @param shopChannel
     * @return
     */
    int delete(Map<String, Object> shopChannel);

    /**
     * 查询商户的门店配置是否存在
     * @param shopConfigImport
     * @return
     */
    int ImportShopConfigIsExists(ShopConfigImport shopConfigImport);

    /**
     * 查询门店配置信息（用于导入查询是否存在）
     * @return
     */
    List<ShopConfigImport> queryAllShopConfig();

    /**
     * 保存门店配置信息
     * @param shopConfigImport
     * @return
     */
    int insertShopConfig(ShopConfigImport shopConfigImport);
}