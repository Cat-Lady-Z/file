package com.lbx.distribution.manageserver.common;

/**
 * @Interface: 首页 - 订单统计图表 - 渠道显示颜色
 * @Description:
 */
public interface ChannelDataEnum {
    public final static String DADA__COLOR = "#f10215" ;
    public final static String HUMMER_BIRD__COLOR = "#008de1" ;
    public final static String MEITUAN_COLOR = "##FFBD27" ;
    public final static String TOTAL_COLOR = "#409EFF" ;

    public final static int DADA__CODE = 3 ;
    public final static int HUMMER_BIRD__CODE = 2 ;
    public final static int MEITUAN_CODE = 1 ;
    public final static int TOTAL__CODE = 0 ;

    public final static String DADA__TXT = "达达-快速达" ;
    public final static String HUMMER_BIRD__TXT= "蜂鸟-快速达" ;
    public final static String MEITUAN_TXT = "美团-快速达" ;
    public final static String TOTAL_TXT = "全部" ;

    public final static String DADA__NAME = "dadaStatOrders" ;
    public final static String HUMMER_BIRD__NAME = "hbirdStatOrders" ;
    public final static String MEITUAN_NAME = "meituanStatOrders" ;
    public final static String TOTAL_NAME = "totalStatOrders" ;

    public static String getChannelName(Integer channelCode) {
        if (channelCode == null)
            return null;

        String name = new String();

        switch (channelCode) {
            case 1:
                name = MEITUAN_NAME;
                break;
            case 2:
                name = HUMMER_BIRD__NAME;
                break;
            case 3:
                name = DADA__NAME;
                break;
        }

        return name;
    }

    public static String getChannelTxt(Integer channelCode) {
        if (channelCode == null)
            return null;

        String txt = new String();

        switch (channelCode) {
            case 1:
                txt = MEITUAN_TXT;
                break;
            case 2:
                txt = HUMMER_BIRD__TXT;
                break;
            case 3:
                txt = DADA__TXT;
                break;
        }

        return txt;
    }

    public static String getChannelColor(Integer channelCode) {
        if (channelCode == null)
            return null;

        String color = new String();

        switch (channelCode) {
            case 1:
                color = MEITUAN_COLOR;
                break;
            case 2:
                color = HUMMER_BIRD__COLOR;
                break;
            case 3:
                color = DADA__COLOR;
                break;
        }

        return color;
    }
}
