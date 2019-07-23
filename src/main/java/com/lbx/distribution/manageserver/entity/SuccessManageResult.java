package com.lbx.distribution.manageserver.entity;


import com.lbx.distribution.manageserver.common.ManageResultCode;

/**
 * @ClassName:
 * @Description: //
 */
public class SuccessManageResult<T> extends ManageResult<T> {
    public SuccessManageResult(){
    }

    public SuccessManageResult(T data) {
        super(ManageResultCode.SUCCESS.getCode(), ManageResultCode.SUCCESS.getMessage(), data);
    }
}
