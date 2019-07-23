package com.lbx.distribution.manageserver.common;

/**
 * @ClassName: 异常处理状态码
 * @Description:
 */

public enum ManageResultCode {

    SUCCESS(0, "请求成功"),
    Unknown_Exception(-1, "系统异常"),

    TOKEN_Exception(10000, "token异常"),
    NO_AUTHORITY_Exception(11000, "没有权限"),

    DATATYPE_Exception(21000, "数据类型错误"),

    NULL_Exception(31000, "必填字段为空"),

    DATA_REQUEST_ERROR(41000, "请求参数错误"),

    REQUEST_FAIL(51000, "操作失败");

    /**
     * 商户部分-异常处理状态码
     *//*
    MERCHANT_INSERT_FAIL(10000, "商户创建失败！"),

    MERCHANT_NOT_FOUND(10001, "没有找到此商户！"),
    NO_MERCHANT(10013, "没有商户信息！"),
    MERCHANT_NOT_BLANK(10002, "商户名称不能为空！"),

    PHONE_WROND(10003, "手机号不正确！"),
    PHONE_EXIST(10004, "手机号已经存在！"),

    EMAIL_WROND(10005, "邮箱不正确！"),
    EMAIL_EXIST(10006, "邮箱已经存在！"),

    PASSWORD_EMPTY(10007, "密码不能为空！"),
    PASSWORD_WROND(10008, "密码格式不正确！"),
    PASSWORD_CHECK_EXCEPTION(10009, "两次密码输入不一致！"),

    MERCHANT_EXIST(10010, "该账号已经存在！"),

    MERCHANT_EMPTY_EXCEPTION(10011, "商户名、手机号或者邮箱为空！"),

    MERCHANT_UPDATE_FAIL(10012, "商户信息更新失败！"),
    *//**
     * 单位/公司
     *//*
    COMPANY_INSERT_FAIL(20001, "公司创建失败！"),
    COMPANY_UPDATE_FAIL(20002, "公司信息更新失败！"),

    NO_COMPANY(20003, "没有公司信息！"),
    COMPANY__NOT_FOUND(20004, "没有找到此公司！"),
    COMPANY__NOT_FOUND_MUTICOND(20005, "根据条件没有找到公司！"),

    COMPANY__DELETE_FAIL(20006, "公司删除失败！"),

    *//**
     * 门店
     *//*
    SHOP_INSERT_FAIL(30001, "门店创建失败！"),
    SHOP_UPDATE_FAIL(30002, "门店信息更新失败！"),

    NO_SHOP(30003, "没有门店信息！"),
    SHOP_NOT_FOUND(30004, "没有找到此门店！"),
    SHOP_NOT_FOUND_MUTICOND(30005, "根据条件没有找到门店！"),

    SHOP_DELETE_FAIL(30006, "门店删除失败！"),

    SHOP_EXCEL_NULL(30007, "导入的表格为空！"),

    *//**
     * 其他
     *//*
    DATATYPE_ERROR(40001, "数据类型不正确！"),
    ACTION_MONGODB_ERROR(40002, "操作mysql数据库出错！"),
    OPERATION_TOO_FREQUENT(40003, "请求过于频繁，请稍候再试！");*/

    private int code;
    private String message;

    ManageResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
