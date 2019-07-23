package com.lbx.distribution.manageserver.entity.company;

import java.util.List;

/**
 * @ClassName: 公司关联门店实体
 * @Description: //
 */
public class CompanyContactShop {

    //做关联的公司
    private Integer companyId;

    //被关联的门店列表
    private List<Integer> shopId;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public List<Integer> getShopId() {
        return shopId;
    }

    public void setShopId(List<Integer> shopId) {
        this.shopId = shopId;
    }
}
