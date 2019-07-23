package com.lbx.distribution.manageserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lbx.distribution.manageserver.common.EnumMenuLevelType;
import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.MenuEntity;
import com.lbx.distribution.manageserver.entity.MenuRequest;
import com.lbx.distribution.manageserver.entity.PageResult;
import com.lbx.distribution.manageserver.entity.SimpleResponse;
import com.lbx.distribution.manageserver.entity.channel.ChannelConfigEntityVo;
import com.lbx.distribution.manageserver.entity.company.CompanyContactShop;
import com.lbx.distribution.manageserver.entity.company.CompanyEntity;
import com.lbx.distribution.manageserver.entity.company.CompanyEntityVo;
import com.lbx.distribution.manageserver.entity.merchant.MerchantEntity;
import com.lbx.distribution.manageserver.entity.shop.ShopEntityVo;
import com.lbx.distribution.manageserver.mapper.CompanyMapper;
import com.lbx.distribution.manageserver.mapper.MerchantMapper;
import com.lbx.distribution.manageserver.mapper.ShopMapper;
import com.lbx.distribution.manageserver.util.ManageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName:  组织机构 列表 service
 * @Description:
 */
@Service
public class CompanyCommonService {

    private static Logger logger = LoggerFactory.getLogger(CompanyCommonService.class);

    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private ManageCommonService manageCommonService;
    @Autowired
    private ShopCommonService shopCommonService;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private ChannelConfigCommonService chanCommonService;

    /**
     * 获取公司列表，支持分页查询，多条件查询
     * @return
     */
    public PageResult getCompanyList(Map<String, Object> manageSimpleRequest) {

        List<CompanyEntityVo> companyList ;

        PageResult pageResult = new PageResult();

        //判断是否需要分页
        Object isPage = manageSimpleRequest.get("isPage");

        if (isPage == null) {
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数缺少分页标识isPage！");
        }

        Integer judgePage = (Integer) isPage;
        if (judgePage == 1 ){
            Integer pageSize = manageCommonService.getPageSize(manageSimpleRequest);
            Integer pageNum = manageCommonService.getPageNum(manageSimpleRequest);

            //分页处理
            PageHelper.startPage(pageNum, pageSize);

            companyList = this.queryCompanyList(manageSimpleRequest);

            //取记录总条数
            PageInfo<CompanyEntityVo> pageInfo = new PageInfo<>(companyList);
            pageResult.setTotal(pageInfo.getTotal());
            pageResult.setPageNum(pageInfo.getPageNum());

        } else {
            companyList = this.queryCompanyList(manageSimpleRequest);
            if (companyList != null){
                pageResult.setTotal(companyList.size());
            } else {
                pageResult.setTotal(0);
            }
        }

        //设置是否是最后一级公司的标记位（下一级无公司）
        companyList = this.setIsEndCompany(companyList);

        //填充每个公司的渠道信息
        companyList = this.setChannelList(companyList);

        //填充每个公司关联门店的信息
        companyList =  this.setShopList(companyList);

        pageResult.setList(companyList);

        return pageResult;
    }

    /**
     * 根据map，获取公司列表
     * @param manageSimpleRequest
     * @return
     */
    public List<CompanyEntityVo> queryCompanyList(Map<String, Object> manageSimpleRequest) {
        return companyMapper.queryCompanyList(manageSimpleRequest);
    }

    /**
     *  设置是否是最后一级公司的标记位（isEnd: 1：最后一级，0：不是）
     * @param companyList
     * @return
     */
    private List<CompanyEntityVo> setIsEndCompany(List<CompanyEntityVo> companyList) {
        if (companyList == null)
            return null;

        for (CompanyEntityVo com:companyList  ) {
            Integer companyId = com.getCompanyId();
            List<Integer> idsNum = companyMapper.findCompanyIdsByParentId(companyId);
            if ( idsNum == null || idsNum.size() == 0) {
                com.setIsEnd(1);
            } else {
                com.setIsEnd(0);
            }
        }

        return companyList;
    }

    /**
     * 填充每个公司对应的关联门店列表
     */
    private List<CompanyEntityVo> setShopList(List<CompanyEntityVo> companyList) {
        if (companyList == null)
            return null;

        List<CompanyEntityVo> resultList = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();

        for (CompanyEntityVo com:companyList ) {

            map.put("companyId", com.getCompanyId());

            List<ShopEntityVo> shopList = shopCommonService.queryShopList(map);
            com.setShopList(shopList);

            resultList.add(com);
        }

        return resultList;
    }

    /**
     * 获取每个公司对应的渠道列表
     */
    private List<CompanyEntityVo> setChannelList(List<CompanyEntityVo> companyList) {
        if (companyList == null)
            return null;

        List<CompanyEntityVo> resultList = new ArrayList<>();

        //获取所有启用的商户和组织机构
        List<Integer> availableCompanyIds = companyMapper.queryAvailableCompanyId();
        List<Integer> availableMerchantIds = merchantMapper.queryAvailableMerchantId();

        Map<String, Object> partams = new HashMap<>();

        for (CompanyEntityVo com:companyList ) {

            partams.put("companyId", com.getCompanyId());

            List<ChannelConfigEntityVo> channelList = chanCommonService.queryChannelConfigList(partams,availableCompanyIds, availableMerchantIds);
            com.setChannelList(channelList);

            resultList.add(com);
        }

        return resultList;
    }


    /**
     * 关联门店
     * @param companyContactShop
     * @return
     */
    public String contactShops(CompanyContactShop companyContactShop) {
        Integer companyId = companyContactShop.getCompanyId();

        List<Integer> shopIdList = companyContactShop.getShopId();

        for (Integer shopId:shopIdList ) {
            shopCommonService.contactCompanyByShopId(companyId, shopId);
        }

        logger.info(String.format("contactShops success. companyId: %s ;",   companyContactShop.getCompanyId() ));

        String str = "关联门店成功！";
        String resp = manageCommonService.getRespBody(str);

        return resp;
    }

    /**
     * 获取下层菜单（组织机构 + 门店）
     * @param rq
     * @return
     */
    public List<MenuEntity> queryMenu(MenuRequest rq) {
        List<MenuEntity> totalResults = new ArrayList<>();

        List<MenuEntity> comMenuResults = this.queryCompanyMenu(rq);

        totalResults.addAll(comMenuResults);

        Integer type = rq.getType();
        if ( type == 2 ) {
            List<MenuEntity> shopMenuResults = shopCommonService.queryShopMenu(rq);
            totalResults.addAll(shopMenuResults);
        }

        return totalResults;
    }


    /**
     * 获取组织机构菜单
     */
    private List<MenuEntity> queryCompanyMenu(MenuRequest rq) {
        List<MenuEntity> comMenuResults = companyMapper.getCompanyMenu(rq);

        //设置公司是不是最后一级的标记
        comMenuResults = this.isEndCompany(comMenuResults);

        comMenuResults = manageCommonService.setType(comMenuResults, EnumMenuLevelType.COMPANY);

        return comMenuResults;
    }

    /**
     * 设置公司是不是最后一级
     * @param comMenuResults
     * @return
     */
    private List<MenuEntity> isEndCompany(List<MenuEntity> comMenuResults) {
        if (comMenuResults == null)
            return null;

        for (MenuEntity menu:comMenuResults  ) {
            Integer companyId = menu.getId();
            List<Integer> idsNum = companyMapper.findCompanyIdsByParentId(companyId);
            List<ShopEntityVo> shopEntityVos = shopMapper.queryShopsByCompanyId(companyId);
            if ( ( idsNum == null || idsNum.size() == 0) && shopEntityVos.size() == 0 ) {
                menu.setIsEnd(1);
            } else {
                menu.setIsEnd(0);
            }
        }

        return comMenuResults;
    }

    /**
     * 根据公司id获取所有父类id
     * @param companyId
     * @return
     */
    public List<Integer> querySuperCompanyIdList(Integer companyId) {
        return companyMapper.querySuperCompanyIdList(companyId);
    }

    /**
     *
     * @param companyEntity
     * @return
     */
    public CompanyEntity insertCompany(CompanyEntity companyEntity) {
        List<CompanyEntityVo> companyList = null;
        Map<String, Object> params = new HashMap<>();
        Integer level = null;

        //如果是公司在创建公司，就获取母公司的商户id
        Integer parentId = companyEntity.getParentId();
        Integer merchantId = companyEntity.getMerchantId();

        if (parentId == null && merchantId == null) {
            logger.warn(String.format("请求参数错误!缺少父公司id/商户id.merchantId: %s /parentId: %s ;", companyEntity.getMerchantId(), parentId ));
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误！缺少父公司id/商户id");
        }

        String companyName = companyEntity.getCompanyName();
        if (companyName == null || companyName.replaceAll(" ", "").length() == 0 ) {
            logger.warn(String.format("请求参数错误!公司名称为空.merchantId: %s /parentId: %s ;", companyEntity.getMerchantId(), parentId ));
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数错误！公司名称为空.");
        }
        String companyNameReplace = companyName.replaceAll(" ", "");
        companyEntity.setCompanyName(companyNameReplace);

        //情况一：商户导入
        if ( merchantId != null && parentId == null) {
            MerchantEntity merchantEntity = merchantMapper.queryMerchantByMerchantId(merchantId);
            if ( merchantEntity == null ){
                throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "未找到对应商户信息，请确认商户id是否正确!");
            }
            level = 1;
            parentId = 0;

            //判断公司是否存在
            params.put("merchantId", merchantId);
            companyList = companyMapper.queryCompanyList(params);
        }

        //情况二：公司导入
        if (parentId != null && merchantId == null) {
            CompanyEntity parentCompany = companyMapper.queryCompanyByCompanyId(parentId);
            if ( parentCompany == null || parentCompany.getMerchantId() == null){
                throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "未找到对应公司信息，请确认公司id是否正确!");
            }
            level = parentCompany.getLevel()+1;
            merchantId = parentCompany.getMerchantId();

            //判断公司是否存在
            params.put("parentId", parentId);
            companyList = companyMapper.queryCompanyList(params);
        }

        companyEntity.setMerchantId(merchantId);
        companyEntity.setParentId(parentId);
        companyEntity.setLevel(level);

        Set<String> companyNameSet = new HashSet<>();
        //判断数据库里同一个商户/父级公司下是否有重复的公司
        if ( companyList != null ) {
            for (CompanyEntityVo companyEntityVo:companyList ) {
                companyName = companyEntityVo.getCompanyName();
                companyNameSet.add(companyName);
            }
            //公司名称相同视为相同公司
            if (companyNameSet.contains(companyEntity.getCompanyName())) {
                throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "已有公司：" + companyEntity.getCompanyName() + "！");
            }
        }

        if ( (companyMapper.insertCompany(companyEntity)) <= 0 ){
            logger.warn(String.format("addCompany fail. merchantId: %s /companyId: %s , companyName: %s;",
                    companyEntity.getMerchantId(), companyEntity.getCompanyId(), companyEntity.getCompanyName() ));

            throw new ManageException(ManageResultCode.REQUEST_FAIL, "创建失败");
        }

        //返回新增的公司信息
        Map<String, Object> map = new HashMap<>();
        map.put("parentId", parentId);
        map.put("companyName", companyEntity.getCompanyName());
        map.put("merchantId", companyEntity.getMerchantId());
        map.put("level", companyEntity.getLevel());
        map.put("createTime", companyEntity.getCreateTime());

        CompanyEntity company = companyMapper.queryCompany(map);

        return company;
    }
}
