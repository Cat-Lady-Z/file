package com.util.file.common;

/**
 * @ClassName: 分发规则名称
 * @Description: //
 */
public class EnumMenuLevelType {
    public final static String MERCHANT_NAME = "商户" ;
    public final static String COMPANY_NAME = "组织机构";
    public final static String SHOP_NAME = "门店";

    public final static int MERCHANT = 1 ;
    public final static int COMPANY = 2;
    public final static int SHOP = 3;

    public static String getTypeName(Integer code) {
        if (code == null)
            return null;

        String name = new String();

        switch (code){
            case 1: name = MERCHANT_NAME;
                break;
            case 2: name = COMPANY_NAME;
                break;
            case 3: name = SHOP_NAME;
                break;
             default:
                 name = "未知设置";
                 break;
        }

        return name;
    }

}
