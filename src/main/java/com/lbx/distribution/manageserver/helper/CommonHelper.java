package com.lbx.distribution.manageserver.helper;

import com.lbx.distribution.manageserver.common.ManageResultCode;
import com.lbx.distribution.manageserver.entity.company.CompanyEntity;
import com.lbx.distribution.manageserver.entity.merchant.MerchantEntity;
import com.lbx.distribution.manageserver.entity.shop.ShopEntityVo;
import com.lbx.distribution.manageserver.mapper.CompanyMapper;
import com.lbx.distribution.manageserver.mapper.MerchantMapper;
import com.lbx.distribution.manageserver.mapper.ShopMapper;
import com.lbx.distribution.manageserver.service.CompanyService;
import com.lbx.distribution.manageserver.service.ManageCommonService;
import com.lbx.distribution.manageserver.util.ManageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: 通用工具
 * @Description: //
 */
@Component
public class CommonHelper {
    private static Logger logger = LoggerFactory.getLogger(CommonHelper.class);

    @Autowired
    protected MerchantMapper merchantMapper;
    @Autowired
    private ShopMapper shopMapper;
    @Autowired
    private CompanyMapper comapnyMapper;


    /**
     * 判断上级机构（商户/公司）是否在启用状态（状态(1.active;0:disable, 删除: -1））
     * @param merchantId
     * @param parentId
     * @return
     */
    public  Boolean isAvailableHighLevelStatus(Integer merchantId, Integer parentId) {
        Boolean flag = false;
        Boolean availableMerchantStatus = this.isAvailableMerchantStatus(merchantId);
        if (parentId!=null && parentId!=0) {
            Boolean availableCompanyStatus = this.isAvailableCompanyStatus(parentId);
            if (availableCompanyStatus && availableMerchantStatus) {
                flag = true;
            }
        } else {
            if ( availableMerchantStatus) {
                flag = true;
            }
        }

        return flag;
    }

    /**
     * 判断门店是否在启用状态（状态(1.active;0:disable, 删除: -1））
     * @param shopId
     * @return
     */
    public Boolean isAvailableShopStatus(Integer shopId) {
        Boolean flag = true;
        if (shopId != null) {
            ShopEntityVo shopEntityVo = shopMapper.queryShopByShopId(shopId);
            Integer status = shopEntityVo.getStatus();
            if (status==0 || status==-1) {
                flag = false;
                logger.info(String.format("门店已被禁用，无操作权限！ shopId: %s;", shopId));
                throw new ManageException(ManageResultCode.REQUEST_FAIL, "门店已被禁用，无操作权限！请与管理员联系。");

            }
        }
        return flag;
    }

    /**
     * 判断当前公司是否在启用状态（状态(1.active;0:disable, 删除: -1））
     * @return
     */
    public Boolean isAvailableCompanyStatus(Integer companyId) {
        Boolean flag = true;
        if (companyId != null ) {
            CompanyEntity companyEntity = comapnyMapper.queryCompanyByCompanyId(companyId);
            Integer status = companyEntity.getStatus();
            if (status==0 || status==-1) {
                flag = false;
                logger.info(String.format("公司已被禁用，无操作权限！companyId: %s;", companyId));
                throw new ManageException(ManageResultCode.REQUEST_FAIL, "公司已被禁用，无操作权限！请与管理员联系。");
            }
        }

        return flag;
    }

    /**
     * 判断当前公司是否在启用状态（状态(1.active;0:disable, 删除: -1））
     * @return
     */
    public Boolean isAvailableMerchantStatus(Integer merchantId) {
        Boolean flag = true;
        if (merchantId != null) {
            MerchantEntity merchantEntity = merchantMapper.queryAllMerchantByMerchantId(merchantId);
            Integer status = merchantEntity.getStatus();
            if (status==0 || status==-1) {
                flag = false;
                logger.info(String.format("商户已被禁用，无操作权限！merchantId: %s;", merchantId));
                throw new ManageException(ManageResultCode.REQUEST_FAIL, "商户已被禁用，无操作权限！请与管理员联系。");
            }
        }

        return flag;
    }
}
