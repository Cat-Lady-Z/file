package com.lbx.distribution.manageserver.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 接口请求参数转换实体
 */
public class SimpleRequest implements Serializable {

    private final static long serialVersionUID = 1L;

    @NotBlank(message = "data不能为空")
    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
