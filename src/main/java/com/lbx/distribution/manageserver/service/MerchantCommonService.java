package com.lbx.distribution.manageserver.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lbx.distribution.manageserver.common.EnumMenuLevelType;
import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.MenuEntity;
import com.lbx.distribution.manageserver.entity.MenuRequest;
import com.lbx.distribution.manageserver.entity.merchant.MerchantConfig;
import com.lbx.distribution.manageserver.entity.PageResult;
import com.lbx.distribution.manageserver.entity.merchant.MerchantEntity;
import com.lbx.distribution.manageserver.entity.merchant.MerchantEntityVo;
import com.lbx.distribution.manageserver.mapper.*;
import com.lbx.distribution.manageserver.util.DateUtil;
import com.lbx.distribution.manageserver.util.KeySecretUtils;
import com.lbx.distribution.manageserver.util.ManageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:  商户List service
 * @Description:
 */
@Service
public class MerchantCommonService {

    private static Logger logger = LoggerFactory.getLogger(MerchantCommonService.class);

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private MerchantConfigMapper merchantConfigMapper;

    @Autowired
    private ManageCommonService manageCommonService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private ShopMapper shopMapper;

    /**
     * 获取商户列表(/分页)
     * @return
     */
    public PageResult getMerchantList(Map<String, Object> manageSimpleRequest) {

        List<MerchantEntity> merchantList = null;

        PageResult pageResult = new PageResult();

        //判断是否需要分页
        Object isPage = manageSimpleRequest.get("isPage");

        if (isPage == null) {
            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR);
        }

        Integer judgePage = (Integer) isPage;
        if (judgePage == 1 ){
            Integer pageSize = manageCommonService.getPageSize(manageSimpleRequest);
            Integer pageNum = manageCommonService.getPageNum(manageSimpleRequest);

            //分页处理
            PageHelper.startPage(pageNum, pageSize);

            merchantList = queryMerchantList(manageSimpleRequest);

            //取记录总条数
            PageInfo<MerchantEntity> pageInfo = new PageInfo<>(merchantList);
            pageResult.setTotal(pageInfo.getTotal());
            pageResult.setPageNum(pageInfo.getPageNum());

        } else {
            merchantList = queryMerchantList(manageSimpleRequest);
            if (merchantList != null){
                pageResult.setTotal(merchantList.size());
            } else {
                pageResult.setTotal(0);
            }
        }

        pageResult.setList(merchantList);

        return pageResult;
    }

    /**
     * 查询商户列表
     * @param manageSimpleRequest
     * @return
     */
    private List<MerchantEntity> queryMerchantList(Map<String, Object> manageSimpleRequest) {
        return merchantMapper.queryMerchantList(manageSimpleRequest);
    }

    /**
     * 获取商户列表（根据角色(0.管理员;1:普通商户)区分返回内容）
     * @param rq
     * @return
     */
    public String getMerchantMenu(MenuRequest rq) {

        String resp = new String();

        List<MenuEntity> menuResults = null;

        //超级管理员（查询商户菜单）,普通商户（查询本商户信息）
        menuResults = merchantMapper.queryMerchantMenu(rq);

        //填充菜单层级类型type
        menuResults = manageCommonService.setType(menuResults, EnumMenuLevelType.MERCHANT);

        resp = manageCommonService.getRespBody(menuResults);

        return resp;


    }

    /**
     *
     * @param merchantEntityVo
     * @return
     */
    public MerchantConfig insertMerchant(MerchantEntityVo merchantEntityVo) {
        merchantEntityVo.setCreateTime(DateUtil.getCurrentTimestamp());

        //生成appKey
        String appKey = KeySecretUtils.getAppKey();

        //获取商户secret
        String secret = KeySecretUtils.getSecret(appKey, merchantEntityVo.getCreateTime());

        MerchantConfig merchantConfig = new MerchantConfig();
        merchantConfig.setAppKey(appKey);
        merchantConfig.setSecret(secret);

        if (merchantMapper.insertMerchant(merchantEntityVo) <= 0 ){
            logger.warn(String.format("insertMerchant fail.  merchantName: %s;", merchantEntityVo.getMerchantName() ));

            throw new ManageException(ManageResultCode.REQUEST_FAIL, "商户创建失败！");
        }

        MerchantEntity temp = merchantMapper.queryMerchantMultiCond( merchantEntityVo );

        merchantConfig.setMerchantId( temp.getMerchantId() );

        //存储
        if (merchantConfigMapper.insert(merchantConfig) <= 0){
            logger.warn(String.format("merchantConfigMapper insertMerchant fail. merchantId: %s , merchantName: %s;",
                    merchantEntityVo.getMerchantId(), merchantEntityVo.getMerchantName() ));

            throw new ManageException(ManageResultCode.REQUEST_FAIL, "商户创建失败！");
        }

        return merchantConfig;
    }

    /**
     * 获取没有被禁用、删除，渠道配置没有满配的商户
     * @param request
     * @return
     */
    public List<MenuEntity> getAvailableMerchantList(Map<String, Object> request) {
        List<MenuEntity> result = new ArrayList<>();

        //获取所有可用的商户
        List<MenuEntity> merchants = merchantMapper.queryMerchants();

        if ( merchants == null )
            return null;

        Map<String, Object> map = new HashMap<>();

        for (MenuEntity m:merchants ) {
            //判断单个商户是否满配
            map.put("merchantId", m.getId());

            if (channelService.getAvailableChannelList(request) != null)

                //判断是否是最后一级
                this.isEnd( m );

                result.add(m);
        }


        return result;
    }

    private void isEnd(MenuEntity m) {
        Integer id = m.getId();

        List<Integer> ids = companyMapper.queryCompanyIdsByMerchantId(id);
        if ( ids == null || ids.size() == 0 ) {
            m.setIsEnd(1);
        } else {
            m.setIsEnd( 0 );
        }
    }

    /**
     * 更新商户状态(下层所有公司和门店都禁用或启用)
     * @param merchantEntityVo
     * @return
     */
    public int updateMerchantStatus(MerchantEntityVo merchantEntityVo) {

        Integer merchantId = merchantEntityVo.getMerchantId();

        if ( merchantId == null || merchantId == 0 ) {

            throw new ManageException(ManageResultCode.DATA_REQUEST_ERROR, "请求参数不包含商户id");

        }

        int merchant = merchantMapper.updateMerchantStatusByMerchantId(merchantEntityVo);

        //商户禁用/启用成功就把下层所有公司和门店都禁用/启用
        if (merchant > 0 ) {
            Map<String, Object> map = new HashMap<>();
            map.put("merchantId" , merchantEntityVo.getMerchantId());
            Timestamp updateTime = DateUtil.getCurrentTimestamp();
            map.put("updateTime", updateTime);

            companyMapper.updateCompanyStatus(map);

            shopMapper.updateShopStatus(map);
        }

        return merchant ;
    }
}
