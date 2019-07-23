package com.lbx.distribution.manageserver.service;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.BusinessType;
import com.lbx.distribution.manageserver.entity.MenuRequest;
import com.lbx.distribution.manageserver.entity.PageResult;
import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntityVo;
import com.lbx.distribution.manageserver.entity.shop.ShopEntity;
import com.lbx.distribution.manageserver.entity.shop.ShopEntityVo;
import com.lbx.distribution.manageserver.helper.CommonHelper;
import com.lbx.distribution.manageserver.mapper.BusinessTypeMapper;
import com.lbx.distribution.manageserver.mapper.ShopMapper;
import com.lbx.distribution.manageserver.util.DateUtil;
import com.lbx.distribution.manageserver.util.ManageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 门店service
 */
@Service
public class ShopService {
    private static Logger logger = LoggerFactory.getLogger(ShopService.class);

    @Autowired
    ShopMapper shopMapper;
    @Autowired
    private BusinessTypeMapper businessTypeMapper;
    @Autowired
    ManageCommonService manageCommonService;
    @Autowired
    private ShopCommonService shopCommonService;
    @Autowired
    private CommonHelper commonHelper;

    /**
     * 新增门店
     * @return
     * @Description:
     */
    @Transactional
    public String addShop(ShopEntityVo shopEntityVo) {
        ShopEntity shop = shopCommonService.addShop(shopEntityVo);

        String resp = manageCommonService.getRespBody(shop);

        logger.info(String.format("新增门店成功! shopId: %s ;", shop.getShopId() ));
        return resp;
    }

    /**
     * 修改门店信息
     * @return
     */
    @Transactional
    public String updateShop(ShopEntityVo shopEntityVo) {
        shopEntityVo.setUpdateTime(DateUtil.getCurrentTimestamp());

        ShopEntityVo shop = shopMapper.queryShopByShopId(shopEntityVo.getShopId());
        //判断当前门店是否在启用状态，禁用则不允许更新
        commonHelper.isAvailableShopStatus(shop.getShopId());
        //判断商户和上级公司是否在启用状态，禁用则不允许更新
        commonHelper.isAvailableHighLevelStatus(shop.getMerchantId(), shop.getCompanyId());

        shopCommonService.updateShopByShopId(shopEntityVo);

        ShopEntityVo updateShop = shopCommonService.queryShopByShopId(shopEntityVo.getShopId());

        String resp = manageCommonService.getRespBody(updateShop);

        logger.info(String.format("修改门店信息成功! shopId: %s ;", shopEntityVo.getShopId() ));
        return resp;
    }

    /**
     * 门店分页列表，支持多条件查询，分页查询
     * @return
     * @Description:
     */
    public String queryShopList(Map<String, Object> manageSimpleRequest) {
        PageResult pageResult = shopCommonService.getShopList(manageSimpleRequest);

        String resp = manageCommonService.getRespBody(pageResult);

        return resp;
    }

    /**
     * 根据门店名称查询门店
     * @return
     * @Description: 根据门店名称分页查询门店列表，支持模糊查询
     */
    public String queryShopByShopName(Map<String, Object> manageSimpleRequest) {

        PageResult pageResult = shopCommonService.getShopList(manageSimpleRequest);

        String resp = manageCommonService.getRespBody(pageResult);

        return resp;
    }

    /**
     * 根据门店id查询门店
     * @return
     * @Description:
     */
    public String queryShopByShopId(Integer shopId) {
        ShopEntityVo shop = shopCommonService.queryShopByShopId(shopId);

        String resp = manageCommonService.getRespBody(shop);

        return resp;
    }

    /**
     * 根据门店ID删除单个门店(非物理删除，status：-1)
     * @return
     */
    @Transactional
    public String deleteByShopId(Map<String, Object> request) {
        Object shopId = request.get("shopId");
        Object status = request.get("status");

        if (shopId == null || status == null ){
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR);
        }

        if (shopMapper.updateShopStatus(request) <= 0 ){
            logger.warn(String.format("删除门店失败! shopId: %s ;", shopId ));

            throw new ManageException(ManageResultCode.REQUEST_FAIL, "删除失败！");
        }

        String str = "删除门店成功！";
        logger.warn(String.format("删除门店成功! shopId: %s ;", shopId ));
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    /**
     * 获取门店菜单
     * @param menuRequest
     * @return
     */
    public String shopMenu(MenuRequest menuRequest) {
        return shopCommonService.getShopMenu(menuRequest);
    }

    /**
     * 绑定渠道
     * @param cv
     * @return
     */
    @Transactional
    public String bindChannel(ChannelConfigEntityVo cv) {
        return shopCommonService.bindChannel(cv);
    }

    /**
     * 更新门店状态
     * @param request
     * @return
     */
    @Transactional
    public String updateShopStatus(Map<String, Object> request) {
        Object shopIdObject = request.get("shopId");
        if (shopIdObject == null) {
            logger.warn("请求参数错误!shopId为空.");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误！门店编码为空.");
        }
        Integer shopId = (Integer) shopIdObject;
        ShopEntityVo shopEntityVo = shopMapper.queryShopByShopId(shopId);
        //判断商户和上级公司是否在启用状态，禁用则不允许更新
        commonHelper.isAvailableHighLevelStatus(shopEntityVo.getMerchantId(), shopEntityVo.getCompanyId());

        if (shopMapper.updateShopStatus(request) <= 0 ){
            logger.warn(String.format("门店状态更新失败! shopId: %s,status:%s;", request.get("shopId"), request.get("status")));

            throw new ManageException(ManageResultCode.REQUEST_FAIL, "状态更新失败！");
        }

        String str = "状态更新成功！";
        logger.info(String.format("门店状态更新成功! shopId: %s,status:%s;", request.get("shopId"), request.get("status")));
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    /**
     * 获取业务类型下拉菜单
     * @return
     */
    public String getBusinessTypeMenu() {

        List<BusinessType> businessTypes = businessTypeMapper.selectAllBusinessType();

        String resp = manageCommonService.getRespBody(businessTypes);

        return resp;
    }

    /**
     * 获取门店类型下拉菜单 (1：自有门店 ，2：第三方门店)
     * @return
     */
   /* public String getShopTypeMenu() {
        List<ShopTypeMenu> shopTypeMenuList = new ArrayList<>();

        ShopTypeMenu shopTypeMenu1 = new ShopTypeMenu();
        shopTypeMenu1.setShopType(ShopTypeEnum.LBX_SHOPTYPE);
        shopTypeMenu1.setShopTypeName(ShopTypeEnum.LBX_SHOPTYPE_NAME);
        shopTypeMenuList.add(shopTypeMenu1);

        ShopTypeMenu shopTypeMenu2 = new ShopTypeMenu();
        shopTypeMenu2.setShopType(ShopTypeEnum.ORIGIN_SHOPTYPE);
        shopTypeMenu2.setShopTypeName(ShopTypeEnum.ORIGIN_SHOPTYPE_NAME);
        shopTypeMenuList.add(shopTypeMenu2);

        String resp = manageCommonService.getRespBody(shopTypeMenuList);

        return resp;
    }*/
}
