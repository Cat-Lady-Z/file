package com.lbx.distribution.manageserver.util;

import com.lbx.distribution.manageserver.common.BaseCommon;
import com.lbx.distribution.manageserver.entity.ManageResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: 商户管理 -- 统一异常处理
 * @Description:
 */
@RestControllerAdvice
public class ManageGlobalExceptionHandler implements BaseCommon {

    @ExceptionHandler(value = ManageException.class)
    public ManageResult domainExceptionHandler(HttpServletRequest req,
                                               ManageException e) throws Exception {
        e.printStackTrace();
        ManageResult result = new ManageResult(e.getErrCode(), e.getMessage());

        logger.warn( String.format("Exception message: %s;",e.getMessage() ));

        return result;
    }
}
