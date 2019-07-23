package com.lbx.distribution.manageserver.entity;

/**
 * @ClassName: 商户管理部分返回消息设置。
 * @Description:  具体内容包括消息码、消息文本、消息数据
 */
public class ManageResult<T> {
    private int code;
    private String msg;
    private T data;

    public ManageResult(){
    }

    public ManageResult(int code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    public ManageResult(int code, String message) {
        this.code = code;
        this.msg = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
