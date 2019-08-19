package com.util.file.common;

/**
 * @ClassName: 分发规则名称
 * @Description: //
 */
public class EnumDistributeRuleName {
    public final static String Default_NAME = "系统自动分发" ;
    public final static String PRICE_PRIORITY_NAME = "价格优先";
    public final static String TIME_PRIORITY_NAME = "时间优先";
    public final static String SPECIAL_NAME = "指定渠道";

    public final static int Default = 0 ;
    public final static int PRICE_PRIORITY = 1;
    public final static int TIME_PRIORITY = 2;
    public final static int SPECIAL = 3;

    public static String getRuleName(Integer code) {
        if (code == null)
            return null;

        String name = new String();

        switch (code){
            case 0: name = Default_NAME;
                    break;
            case 1: name = PRICE_PRIORITY_NAME;
                break;
            case 2: name = TIME_PRIORITY_NAME;
                break;
            case 3: name = SPECIAL_NAME;
                break;
             default:
                 name = "未知设置";
                 break;
        }

        return name;
    }

}
