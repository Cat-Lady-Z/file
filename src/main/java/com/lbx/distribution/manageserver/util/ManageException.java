package com.lbx.distribution.manageserver.util;


import com.lbx.distribution.manageserver.common.ManageResultCode;

/**
 * @ClassName:
 * @Description: //
 */
public class ManageException extends RuntimeException {
    private int errCode = ManageResultCode.Unknown_Exception.getCode();

    public ManageException() {
        super(ManageResultCode.Unknown_Exception.getMessage());
    }

    public ManageException(ManageResultCode resultCode) {
        super(resultCode.getMessage());
        this.errCode = resultCode.getCode();
    }

    public ManageException(ManageResultCode resultCode, String msg) {
        super(msg);
        this.errCode = resultCode.getCode();
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
