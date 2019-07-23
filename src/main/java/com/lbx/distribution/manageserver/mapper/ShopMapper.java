package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.MenuEntity;
import com.lbx.distribution.manageserver.entity.MenuRequest;
import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntityVo;
import com.lbx.distribution.manageserver.entity.shop.ShopEntity;
import com.lbx.distribution.manageserver.entity.shop.ShopEntityVo;
import com.lbx.distribution.manageserver.entity.shop.ShopExport;
import com.lbx.distribution.manageserver.entity.shop.ShopImport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Interface: 门店Mapper
 * @Description:
 */
@Mapper
public interface ShopMapper {
    /**
     * 新增门店
     * @param shopEntityVo
     * @return
     */
    int insertShop(ShopEntityVo shopEntityVo);

    /**
     * 批量新增门店 ???
     * @param shopList
     * @return
     */
    int insertShopBatch(List<ShopEntityVo> shopList);

    /**
     * 更新门店信息
     * @param shopEntity
     * @return
     */
    int updateShopByShopId(ShopEntityVo shopEntity);

    /**
     * 查询门店列表(/分页 /多条件)
     * @param
     * @return
     */
    List<ShopEntityVo> queryShopList(Map<String, Object> manageSimpleRequest);

    /**
     * 查询门店总数
     * @param
     * @return
     */
    int findTotalNumber();

    /**
     * 查询门店， 根据门店Id
     * @param shopId
     * @return
     */
    ShopEntityVo queryShopByShopId(Integer shopId);

    /**
     * 删除门店
     * @param shopId
     * @return
     */
    int deleteByShopId(Integer shopId);

    /**
     * 根据公司id查询已关联的门店
     * @param companyId
     * @return
     */
    List<ShopEntityVo> queryShopsByCompanyId(@Param("companyId") Integer companyId);

    /**
     * 根据商户id查询相关联的门店
     * @param merchantId
     * @return
     */
    List<ShopEntityVo> queryShopsByMerchantId(@Param("merchantId")Integer merchantId);

    /**
     * 关联公司
     * @param companyId
     * @param shopId
     * @return
     */
    int contactCompanyByShopId(@Param("companyId") Integer companyId, @Param("shopId") Integer shopId);

    /**
     * 获取门店菜单
     * @param rq
     * @return
     */
    List<MenuEntity> getShopMenu(MenuRequest rq);


    /**
     * 禁用门店
     * @param map
     * @return
     */
    int updateShopStatus(Map<String, Object> map);

    /**
     * 多条件查询门店（单个）
     * @param map
     * @return
     */
    ShopEntity queryShop(Map<String, Object> map);

    /**
     * 查询所有门店信息（用于导入查询门店是否存在）
     * @return
     */
    List<ShopImport> queryAllShopsToImport();


    /**
     * 导入新增门店
     * @param shopImport
     * @return
     */
    Integer insertShopImport(ShopImport shopImport);

    /**
     * 查询导出门店
     * @param params
     * @return
     */
    List<ShopExport> queryShopExcelList(Map<String, Object> params);
}
