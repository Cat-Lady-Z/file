package com.lbx.distribution.manageserver.entity.order;

/**
 * @ClassName: 商户订单来源实体（）
 * @Description: //
 */
public class OrderSourceEntity {
    //自增id
    private Integer id;
    //商户id
    private Integer merchantId;
    //商户订单来源类型
    private Integer sourceType;
    //商户订单来源类型描述
    private String sourceDesc;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }


    //商户订单来源类型
    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }


    //商户订单来源类型描述
    public String getSourceDesc() {
        return sourceDesc;
    }

    public void setSourceDesc(String sourceDesc) {
        this.sourceDesc = sourceDesc;
    }
}
