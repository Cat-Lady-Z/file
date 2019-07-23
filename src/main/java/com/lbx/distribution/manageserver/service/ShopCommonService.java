package com.lbx.distribution.manageserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lbx.distribution.manageserver.common.EnumMenuLevelType;
import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.*;
import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntityVo;
import com.lbx.distribution.manageserver.entity.company.CompanyEntity;
import com.lbx.distribution.manageserver.entity.shop.ShopChannelEntityVo;
import com.lbx.distribution.manageserver.entity.shop.ShopConfigImport;
import com.lbx.distribution.manageserver.entity.shop.ShopEntity;
import com.lbx.distribution.manageserver.entity.shop.ShopEntityVo;
import com.lbx.distribution.manageserver.mapper.*;
import com.lbx.distribution.manageserver.util.DateUtil;
import com.lbx.distribution.manageserver.util.ManageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * @ClassName: 门店 列表 service
 * @Description:
 */
@Service
public class ShopCommonService {
    private static Logger logger = LoggerFactory.getLogger(ShopCommonService.class);

    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private ShopChannelMapper shopChannelMapper;
    @Autowired
    private CompanyCommonService companyCommonService;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private CityMatchService cityMatchService;
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private ManageCommonService manageCommonService;
    @Autowired
    private ChannelConfigCommonService channelConfigCommonService;
    @Autowired
    private BusinessTypeMapper businessTypeMapper;

    /**
     * 分页获取门店列表
     * @return
     */
    public PageResult getShopList(Map<String, Object> manageSimpleRequest) {

        List<ShopEntityVo> shopList ;
        PageResult pageResult = new PageResult();

        //判断是否需要分页
        Object isPage = manageSimpleRequest.get("isPage");

        if (isPage == null) {
            logger.warn("请求参数缺少分页标识isPage");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR , "请求参数缺少分页标识isPage！");
        }

        Integer judgePage = (Integer) isPage;
        if (judgePage == 1 ){
            Integer pageSize = manageCommonService.getPageSize(manageSimpleRequest);
            Integer pageNum = manageCommonService.getPageNum(manageSimpleRequest);

            //分页处理
            PageHelper.startPage(pageNum, pageSize);
            shopList = this.queryShopList(manageSimpleRequest);
            //取记录总条数
            PageInfo<ShopEntityVo> pageInfo = new PageInfo<>(shopList);
            pageResult.setTotal(pageInfo.getTotal());
            pageResult.setPageNum(pageInfo.getPageNum());
        } else {
            shopList = this.queryShopList(manageSimpleRequest);
            if (shopList != null){
                pageResult.setTotal(shopList.size());
            } else {
                pageResult.setTotal(0);
            }
        }

        //填充每个门店的渠道信息（录入了第三方门店编码的渠道）
        shopList = this.setShopChannel(shopList);

        //填充地址信息（省市区）
         shopList = this.setAddressName(shopList);

        pageResult.setList(shopList);

        return pageResult;
    }

    private List<ShopEntityVo> setShopChannel(List<ShopEntityVo> shopList) {
        if ( shopList == null ){
            return null;
        }

        for (ShopEntityVo vo:shopList ) {
            Integer shopId = vo.getShopId();
            List<ShopChannelEntityVo> channels = this.getShopChannelByShopId(shopId);
            vo.setShopChannels(channels);
        }

        return shopList;
    }

    /**
     * 填充地址信息（省市区）
     * @return
     */
    private List<ShopEntityVo> setAddressName(List<ShopEntityVo> shopList ) {
        for (ShopEntityVo vo:shopList ) {
            vo = this.getAddressData(vo);
        }

        return shopList;
    }

    /**
      * 填充地址信息
     */
    private ShopEntityVo getAddressData(ShopEntityVo vo) {
        //省名
        String provinceid = vo.getProvinceid();
        String provinceName = cityMatchService.queryCityNameByProvinceid(provinceid);
        vo.setProvinceName(provinceName);

        //市名
        String cityCode = vo.getCityCode();
        String cityName = cityMatchService.queryCityNameByCityCode(cityCode);
        vo.setCityName(cityName);

        //区名
        String areaid = vo.getAreaid();
        String areaName = cityMatchService.queryCityNameByAreaid(areaid);
        vo.setAreaName(areaName);

        return vo;
    }

    /**
     * 填充门店上层所有的配置渠道
     * @param shop
     * @param shop
     * @return
     */
    private List<ChannelConfigEntityVo> setSuperChannelList(ShopEntityVo shop) {
        Integer companyId = shop.getCompanyId();
        //获取所有的上层companyId
        List<Integer> companyIds =  companyCommonService.querySuperCompanyIdList(companyId);

        List<ChannelConfigEntityVo> resultList = new ArrayList<>();
        //获取所有启用的商户和组织机构
        List<Integer> availableCompanyIds = companyMapper.queryAvailableCompanyId();
        List<Integer> availableMerchantIds = merchantMapper.queryAvailableMerchantId();
        if (companyIds.size() != 1 ) {
            resultList = channelConfigCommonService.queryChannelsByCompanyIds(companyIds);
        } else {
            Integer id = companyIds.get(0);
            Map<String , Object> map = new HashMap<>();
            map.put("companyId", id );
            resultList = channelConfigCommonService.queryChannelConfigList(map, availableCompanyIds, availableMerchantIds);
        }

        //获取商户的配置渠道
        Integer merchantId = shop.getMerchantId();
        Map<String , Object> map = new HashMap<>();
        map.put("merchantId", merchantId );
        resultList.addAll( channelConfigCommonService.queryChannelConfigList(map, availableCompanyIds, availableMerchantIds) );

        return resultList;
    }

    /**
     * 获取门店列表
     * @param map
     * @return
     */
    public List<ShopEntityVo> queryShopList(Map<String, Object> map) {
        return shopMapper.queryShopList(map);
    }

    /**
     * 关联公司
     * @param companyId
     * @param shopId
     */
    public int contactCompanyByShopId(Integer companyId, Integer shopId) {
        return shopMapper.contactCompanyByShopId(companyId, shopId);
    }

    /**
     * 查询门店菜单
     * @param rq
     * @return
     */
    public List<MenuEntity> queryShopMenu(MenuRequest rq) {
        List<MenuEntity> shopMenuResults = shopMapper.getShopMenu(rq);
        if (shopMenuResults != null ) {
            for (MenuEntity me:shopMenuResults ) {
                me.setIsEnd(1);
            }
        }

        shopMenuResults = manageCommonService.setType(shopMenuResults, EnumMenuLevelType.SHOP);

        return shopMenuResults;
    }

    /**
     * 获取门店菜单
     * @param rq
     * @return
     */
    public String getShopMenu(MenuRequest rq) {
        String resp = new String();
        List<MenuEntity> menuResults = null;
        List<MenuEntity> shopMenuResults = this.queryShopMenu(rq);
        resp = manageCommonService.getRespBody(shopMenuResults);

        return resp;
    }

    /**
     * 绑定渠道
     * @param cv
     * @return
     */
    public String bindChannel(ChannelConfigEntityVo cv) {
        //填充第三方门店id
        //cv = setChannelShopId(cv);

        if (channelConfigCommonService.insertChannelConfig(cv)<= 0 ){
            logger.warn("绑定渠道失败!");
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "绑定渠道失败！");
        }

        String str = "绑定渠道成功！";
        logger.warn("绑定渠道成功!");
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }


    /**
     * 新建门店
     * @param shopEntityVo
     * @return
     */
    public ShopEntity addShop(ShopEntityVo shopEntityVo) {
        //填写商户id
        Integer companyId = shopEntityVo.getCompanyId();
        if ( companyId == null || companyId == 0 ) {
            logger.warn("请求参数错误! 无公司id！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 无公司id！");
        }
        CompanyEntity companyEntity = companyMapper.queryCompanyByCompanyId(companyId);
        if (companyEntity==null || companyEntity.getMerchantId() == null ||companyEntity.getMerchantId() == 0 ) {
            logger.warn("请求参数错误! 公司无商户信息！公司id: " + companyId);
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 公司无商户信息！公司id：" + companyId);
        }
        shopEntityVo.setMerchantId(companyEntity.getMerchantId());
        //校验请求参数
        this.checkDataLegal(shopEntityVo);

        Date updateTime = null;
        try {
            updateTime = DateUtil.getCurrentDate();
        } catch (ParseException e) {
            logger.error("日期转换异常!");
            e.printStackTrace();
        }

        shopEntityVo.setCreateTime(updateTime);
        shopEntityVo.setUpdateTime(updateTime);

        //填充省市区名称
        shopEntityVo = this.setRegionName(shopEntityVo);

        if (shopMapper.insertShop(shopEntityVo) <= 0 ){
            logger.warn("创建门店失败!");
            throw new ManageException(ManageResultCode.REQUEST_FAIL, "创建门店失败！");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("shopName", shopEntityVo.getShopName());
        map.put("companyId", companyId);
        map.put("address", shopEntityVo.getAddress());
        map.put("merchantId", shopEntityVo.getMerchantId());
        map.put("lng", shopEntityVo.getLng());
        map.put("lat", shopEntityVo.getLat());
        map.put("createTime", shopEntityVo.getCreateTime());

        ShopEntity shop = shopMapper.queryShop(map);

        shopEntityVo.setShopId(shop.getShopId());

        //存储第三方渠道门店表
        this.insertShopChannel(shopEntityVo);

        return shop;
    }

    /**
     * 填充省市区名称
     * @param shopEntityVo
     * @return
     */
    private ShopEntityVo setRegionName(ShopEntityVo shopEntityVo) {
        String provinceid = shopEntityVo.getProvinceid();
        String cityCode = shopEntityVo.getCityCode();
        String areaid = shopEntityVo.getAreaid();

        if ( (provinceid == null || "".equals(provinceid)) && (cityCode == null || "".equals(cityCode)) && (areaid == null || "".equals(areaid))) {
            logger.warn("请求参数错误! 缺少区域编码！provinceid:" + provinceid + ",cityCode: " + areaid + ", areaid: " + areaid );
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 缺少区域编码！");
        }
        String provinceName = cityMatchService.queryCityNameByProvinceid(provinceid);
        String cityName = cityMatchService.queryCityNameByCityCode(cityCode);
        String areaName = cityMatchService.queryCityNameByAreaid(areaid);

        if ( provinceName != null ) {
            shopEntityVo.setProvinceName(provinceName);
        }
        if ( cityName != null ) {
            shopEntityVo.setCityName(cityName);
        }
        if ( areaName != null ) {
            shopEntityVo.setAreaName(areaName);
        }

        return shopEntityVo;
    }

    /**
     * 校验请求参数是否齐全
     * @param shopEntityVo
     */
    private void checkDataLegal(ShopEntityVo shopEntityVo) {
        //判断门店是否存在，根据门店名称以及经纬度确认唯一性
        String shopName = shopEntityVo.getShopName();
        if ( shopName == null || shopName.replaceAll(" ", "").length() == 0 ) {
            logger.warn("请求参数错误! 请输入门店名称！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 请输入门店名称！");
        }

        String originShopId = shopEntityVo.getOriginShopId();
        if ( originShopId == null || originShopId.replaceAll(" ", "").length() == 0 ) {
            logger.warn("请求参数错误! 没有商户门店编码！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 请输入商户门店编码！");
        }

        List<ShopChannelEntityVo> addShopConfigList = shopEntityVo.getShopChannels();
        if ( addShopConfigList == null || addShopConfigList.size() == 0 ) {
            logger.warn("请求参数错误! 无第三方门店信息！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 无第三方门店信息！");
        } else {
            for (ShopChannelEntityVo shopChannel:addShopConfigList ) {
                String channelShopid = shopChannel.getChannelShopid();
                if (channelShopid == null || shopName.replaceAll(" ", "").length() == 0) {
                    logger.warn("请求参数错误! 无第三方门店编码！");
                    throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 无第三方门店编码！");
                }
                channelShopid = channelShopid.replaceAll(" ", "");
                shopChannel.setChannelShopid(channelShopid);
            }
        }

        if ( shopEntityVo.getBusinessType() == null || "".equals(shopEntityVo.getBusinessType()) ) {
            logger.warn("请求参数错误! 请选择业务类型！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 请选择业务类型！");
        }

        String contactName = shopEntityVo.getContactName();
        if ( contactName == null || contactName.replaceAll(" ", "").length() == 0 ) {
            logger.warn("请求参数错误! 请填写联系人姓名！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 请填写联系人姓名！");
        }

        String contactPhone = shopEntityVo.getContactPhone();
        if ( contactPhone == null || contactPhone.replaceAll(" ", "").length() == 0 ) {
            logger.warn("请求参数错误! 请填写联系人电话！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 请填写联系人电话！");
        }

        String address = shopEntityVo.getAddress();
        if ( address == null || address.replaceAll(" ", "").length() == 0 ) {
            logger.warn("请求参数错误! 请填写地址！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 请填写地址！");
        }

        Double lng = shopEntityVo.getLng();
        Double lat = shopEntityVo.getLat();
        if ( lat == null ||  lng == null  ) {
            logger.warn("请求参数错误! 请填写经纬度信息！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 请填写经纬度信息！");
        }

        if (shopEntityVo.getShopId() == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("shopName", shopName);
            params.put("lng", lng);
            params.put("lat", lat);
            params.put("companyId", shopEntityVo.getCompanyId());
            params.put("merchantId", shopEntityVo.getMerchantId());
            ShopEntity shopEntity = shopMapper.queryShop(params);
            if ( shopEntity != null ){
                logger.warn("门店已存在！名称:" + shopName + ", 地址:" + address);
                throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "门店已存在！名称:" + shopName + ", 地址:" + address);
            }

          /*  //判断门店配置是否存在
            List<ShopConfigImport> shopConfigList = shopChannelMapper.queryAllShopConfig();
            Set<String> shopConfigSet = new HashSet<>();
            for (ShopConfigImport shopConfigImport : shopConfigList) {
                Integer shopId = shopConfigImport.getShopId();
                Integer channelId = shopConfigImport.getChannelId();
                String channelShopId = shopConfigImport.getChannelShopId();
                shopConfigSet.add(shopId + channelId + channelShopId);
            }

            for (ShopChannelEntityVo shopConfig : addShopConfigList) {
                //判断配置是否存在
                Integer shopId = shopConfig.getShopId();
                String configShopId = shopConfig.getChannelShopid();
                Integer channelId = shopConfig.getChannelId();
                if( shopConfigSet.contains(shopId + channelId + configShopId) ){
                    logger.warn("门店配置已存在！门店编码:" + configShopId + ", " + "channelId: " + channelId);
                    throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "门店配置已存在！请检查输入是否正确。门店编码:" + configShopId );
                }
            }*/
        }

        shopName = shopName.replaceAll(" ", "");
        shopEntityVo.setShopName(shopName);
        contactName = contactName.replaceAll(" ", "");
        shopEntityVo.setContactName(contactName);
        contactPhone = contactPhone.replaceAll(" ", "");
        shopEntityVo.setContactPhone(contactPhone);
        address = address.replaceAll(" ", "");
        shopEntityVo.setAddress(address);
    }

    /**
     * 填写第三方渠道门店表格
     * @param shopEntityVo
     */
    private void insertShopChannel(ShopEntityVo shopEntityVo) {

        Integer shopId = shopEntityVo.getShopId();

        List<ShopChannelEntityVo> shopChannels = shopEntityVo.getShopChannels();

        if (shopChannels == null) {
            logger.warn("请求参数错误! 无渠道门店信息！");
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误! 无渠道门店信息！");
        }

        this.addShopChannel(shopId, shopChannels);
    }

    /**
     * 增加渠道
     * @param shopChannels
     */
    private void addShopChannel(Integer shopId, List<ShopChannelEntityVo> shopChannels) {
        for (ShopChannelEntityVo s:shopChannels ) {
            Map<String, Object> shopChannel = new HashMap<>();

            shopChannel.put("shopId", shopId);
            shopChannel.put("channelId", s.getChannelId());
            shopChannel.put("channelShopid", s.getChannelShopid());

            shopChannelMapper.delete( shopChannel );

            if (shopChannelMapper.addShopChannel( shopChannel ) <= 0 ){
                logger.warn(String.format("添加第三方渠道门店失败! shopId: %s,channelId:%s, channelShopid:%s;",
                        shopId,s.getChannelId(), s.getChannelShopid()));
                throw new ManageException(ManageResultCode.REQUEST_FAIL, "操作失败！");
            }
        }
    }

    /**
     * 根据门店id查询门店
     * @param shopId
     * @return
     */
    public ShopEntityVo queryShopByShopId(Integer shopId) {
        ShopEntityVo shopEntityVo = shopMapper.queryShopByShopId(shopId);

        List<BusinessType> businessTypes = businessTypeMapper.selectAllBusinessType();
        //业务类型
        String businessType = shopEntityVo.getBusinessType();
        if (businessType!=null && !"".equals(businessType)) {
            for (BusinessType bt:businessTypes ) {
                if (businessType.equals(bt.getId()+"")) {
                    shopEntityVo.setBusinessTypeName(bt.getName());
                }
            }
        }
        //填充门店 第三方渠道门店信息
        List<ShopChannelEntityVo> shopChannels = this.getShopChannelByShopId(shopEntityVo.getShopId());
        shopEntityVo.setShopChannels( shopChannels );

        //填充门店 配置渠道信息
        List<ChannelConfigEntityVo> channelConfigEntityVoList = this.setSuperChannelList(shopEntityVo);
        shopEntityVo.setChannelConfigList(channelConfigEntityVoList);

        //填充地址信息
        shopEntityVo = this.getAddressData(shopEntityVo);

        return shopEntityVo;
    }

    /**
     * 填充第三方门店信息
     * @param shopId
     * @return
     */
    private List<ShopChannelEntityVo> getShopChannelByShopId(Integer shopId) {
        return shopChannelMapper.queryShopChannelByShopId(shopId);
    }

    /**
     * 修改门店信息
     * @param shopEntityVo
     */
    public void updateShopByShopId(ShopEntityVo shopEntityVo) {
        this.checkDataLegal(shopEntityVo);

        //更新门店信息
        shopMapper.updateShopByShopId(shopEntityVo);

        //更新门店的第三方渠道门店信息
        this.updateShopChannel(shopEntityVo);
    }

    /**
     * 更新第三方渠道门店信息
     * @param shopEntityVo
     */
    private void updateShopChannel(ShopEntityVo shopEntityVo) {
        List<ShopChannelEntityVo> shopChannels = shopEntityVo.getShopChannels();
        //插入/更新新增的渠道
        this.addShopChannel(shopEntityVo.getShopId(), shopChannels);

        List<ShopChannelEntityVo> shopChannels_shop = shopChannelMapper.queryShopChannelByShopId(shopEntityVo.getShopId());
        if (shopChannels_shop!= null) {
            List<ShopChannelEntityVo> deleteShopChannels = new ArrayList<>();

            //传入的参数
            Set<Integer> channelIdSet = new HashSet<>();
            for (ShopChannelEntityVo shopChannel:shopChannels ) {
                channelIdSet.add(shopChannel.getChannelId());
            }

            //数据库
            Map<Integer, ShopChannelEntityVo> shopChannelMap_Shop = new HashMap<>();
            for (ShopChannelEntityVo shopChannel:shopChannels_shop ) {
                shopChannelMap_Shop.put(shopChannel.getChannelId(), shopChannel);
            }

            //现在参数中没有，数据库有的，删除
            for (ShopChannelEntityVo shopChannel_shop:shopChannels_shop ) {
                Integer channelId = shopChannel_shop.getChannelId();
                if ( !channelIdSet.contains(channelId) ) {
                    ShopChannelEntityVo shopChannelEntityVo = shopChannelMap_Shop.get(shopChannel_shop.getChannelId());
                    deleteShopChannels.add(shopChannelEntityVo);
                }
            }

            //删除渠道
            if (deleteShopChannels.size()!=0) {
                this.deleteShopChannel(shopEntityVo.getShopId(), deleteShopChannels);
            }
        }
    }

    /**
     * 删除渠道
     * @param shopId
     * @param shopChannels
     */
    private void deleteShopChannel(Integer shopId, List<ShopChannelEntityVo> shopChannels) {

        for (ShopChannelEntityVo s:shopChannels ) {
            //删除
            Map<String, Object> shopChannelByShopId = new HashMap<>();
            shopChannelByShopId.put("shopId", shopId);
            shopChannelByShopId.put("channelId", s.getChannelId());
            shopChannelByShopId.put("channelShopid", s.getChannelShopid());

            shopChannelMapper.delete(shopChannelByShopId);
        }
    }
}
