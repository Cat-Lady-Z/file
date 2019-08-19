package com.util.file.entity;


import com.util.file.common.ManageResultCode;

/**
 * 接口返回参数
 */
public class SimpleResponse {

    private Integer code;

    private String msg;

    private Object data;

    public SimpleResponse(){
        this.code = ManageResultCode.SUCCESS.getCode();
        this.msg = "请求成功！";
        this.data = null;
    }

    public SimpleResponse(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
