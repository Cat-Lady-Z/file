package com.lbx.distribution.manageserver.common;

/**
 * @ClassName: 订单状态  编码码对应显示名称
 * @Description: //
 */
public class OrderStatusEnum {
   // 显示：订单状态(下单失败=-1，已下单=0 待接单＝1 待取货＝2 骑手到店=100 配送中＝3 已完成＝4 已取消＝5 已过期＝7 指派单=8 退回中=9 退回完成=10 系统异常=1000 ; ）
    //下单失败=-1, 已过期＝7 指派单=8 退回中=9 退回完成=10 系统异常=1000：统一处理成系统异常（1000）
    //分发状态(0:未分发;1:分发渠道成功;4:订单已完成;5:订单已取消;6:订单异常;-1:失败订单)
    //待接单＝1(0-未分发)，已完成＝4，已取消＝5，系统异常=1000(-1, 6); 待取货＝2(2,100),配送中＝3
   public final static String FAIL_ORDER_MSG = "下单失败";
    public final static String PLACED_ORDER_MSG = "已下单";
    public final static String WAIT_ORDER_MSG  = "待接单" ;
    public final static String Courier_Reach  = "骑手到店";
    public final static String WAIT_DELIVERY_MSG  = "待取货";
    public final static String DISTRIBUTION_MSG = "配送中";
    public final static String FINISH_MSG  = "已完成";
    public final static String CANCEL_MSG = "已取消";
    public final static String OVERDUE_MSG  = "已过期";
    public final static String ASSIGN_ORDER_MSG  = "指派单";
    public final static String RETURNING_MSG  = "退回中";
    public final static String RETURN_FINISH_MSG  = "退回完成";
    public final static String SYS_EXCEPTION_MSG  = "系统异常";

    public final static int FAIL_ORDER = -1;
    public final static int PLACED_ORDER_CODE = 0;
    public final static int WAIT_ORDER_CODE = 1 ;
    public final static int WAIT_DELIVERY_CODE = 2;
    public final static int Courier_Reach_CODE  = 100;
    public final static int DISTRIBUTION_CODE = 3;
    public final static int FINISH_CODE = 4;
    public final static int CANCEL_CODE = 5;
    public final static int OVERDUE_CODE = 7;
    public final static int ASSIGN_ORDER_CODE = 8;
    public final static int RETURNING_CODE = 9;
    public final static int RETURN_FINISH_CODE = 10;
    public final static int SYS_EXCEPTION_CODE = 1000;

    public static String getStatusMsg(Integer status) {
        if (status == null)
            return null;

        String msg = new String();

        switch (status){
            case -1: msg = SYS_EXCEPTION_MSG;
                break;
            case 0: msg = PLACED_ORDER_MSG;
                break;
            case 1: msg = WAIT_ORDER_MSG;
                break;
            case 2: ;
            case 100:msg = WAIT_DELIVERY_MSG;
                break;
            case 3: msg = DISTRIBUTION_MSG;
                break;
            case 4: msg = FINISH_MSG;
                break;
            case 5: msg = CANCEL_MSG;
                break;
            case 6: ;
            case 7: ;
            case 8: ;
            case 9: ;
            case 10: ;
            case 1000: msg = SYS_EXCEPTION_MSG;
                break;
             default:
                 msg = "未定义步骤";
                 break;
        }

        return msg;
    }

    //待取货＝2 配送中＝3 已完成＝4
    public static Boolean isDistributionSuccessStatus(Integer statusCode) {
        if (  statusCode==WAIT_DELIVERY_CODE || statusCode ==DISTRIBUTION_CODE || statusCode ==FINISH_CODE) {
            return true;
        }
        return false;
    }

    //判断传入值是否在tb_order_status表。待接单＝1(0)，已完成＝4，已取消＝5，系统异常=1000(-1,  6)
    public static boolean isInTbOrderStatus(Integer statusCode) {
        if ( statusCode==1 || statusCode ==4 || statusCode ==5 || statusCode==1000 ) {
            return true;
        }
        return false;
    }
}
