package com.lbx.distribution.manageserver.common;

/**
 * @ClassName: 订单配送状态列表  编码码对应显示名称
 * @Description: //
 */
public class OrderDistributionStatusEnum {
   // 订单状态(失败订单=-1，已下单=0 待接单＝1 待取货＝2 骑手到店=100 配送中＝3 已完成＝4 已取消＝5 已过期＝7 指派单=8 退回中=9 退回完成=10 系统异常=1000 ; ）
    // 对应显示：订单状态(下单失败=-1，门店发单=0 门店发单＝1 骑手接单＝2 骑手取货＝3 骑手送达＝4 已取消＝5 已过期＝7 指派单=8 退回中=9 退回完成=10 系统异常=1000 ; ）
   public final static String FAIL_ORDER_MSG = "下单失败";
    public final static String PLACED_ORDER_MSG = "门店发单";
    public final static String WAIT_ORDER_MSG  = "门店发单" ;
    public final static String WAIT_DELIVERY_MSG  = "骑手接单";
    public final static String Courier_Reach  = "骑手到店";
    public final static String DISTRIBUTION_MSG = "骑手取货";
    public final static String FINISH_MSG  = "骑手送达";
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

    public static String getStatusMsg(Integer statusCode) {
        if (statusCode == null)
            return null;

        String msg = new String();

        switch (statusCode){
            case FAIL_ORDER: msg = SYS_EXCEPTION_MSG;
                break;
            case PLACED_ORDER_CODE: msg = PLACED_ORDER_MSG;
                break;
            case WAIT_ORDER_CODE: msg = WAIT_ORDER_MSG;
                break;
            case  100:;
            case WAIT_DELIVERY_CODE: msg = WAIT_DELIVERY_MSG;
                break;
            case DISTRIBUTION_CODE: msg = DISTRIBUTION_MSG;
                break;
            case FINISH_CODE: msg = FINISH_MSG;
                break;
            case CANCEL_CODE: msg = CANCEL_MSG;
                break;
            case OVERDUE_CODE: ;
            case ASSIGN_ORDER_CODE: ;
            case RETURNING_CODE: ;
            case RETURN_FINISH_CODE: ;
            case SYS_EXCEPTION_CODE: msg = SYS_EXCEPTION_MSG;
                break;
             default:
                 msg = "未定义步骤";
                 break;
        }

        return msg;
    }

    //门店发单=0 门店发单＝1 骑手接单＝2 骑手取货＝3 骑手送达＝4
    public static Boolean isAbnormalStatus(Integer statusCode) {
        if ( statusCode == PLACED_ORDER_CODE || statusCode == WAIT_ORDER_CODE || statusCode==WAIT_DELIVERY_CODE
                || statusCode ==DISTRIBUTION_CODE || statusCode ==FINISH_CODE) {
            return false;
        }
        return true;
    }

    //判断状态码是否在tb_order_distribution
    //订单状态(已下单=0 待接单＝1 待取货＝2 配送中＝3 已完成＝4 已取消＝5 订单配送异常=6 已过期＝7 指派单=8 妥投异常之物品返回中=9 妥投异常之物品返回完成=10 系统故障订单发布失败=1000 ; ）
    public static boolean isInTbOrderDistribution(Integer statusCode) {
        if (statusCode == 0 || statusCode == 1 || statusCode==2 || statusCode ==3 || statusCode ==4 || statusCode==5 ||
        statusCode==6 || statusCode ==7 || statusCode ==8 || statusCode==9 || statusCode==10 || statusCode==1000) {
            return true;
        }
        return false;
    }
}
