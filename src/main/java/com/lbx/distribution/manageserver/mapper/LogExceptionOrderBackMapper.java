package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.log.LogExceptionOrderBack;
import com.lbx.distribution.manageserver.entity.log.LogExceptionOrderBackDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LogExceptionOrderBackMapper {

    LogExceptionOrderBack selectByPrimaryKey(Long id);

    /**
     * 根据订单id查询异常信息
     * @param orderId
     * @return
     */
    List<LogExceptionOrderBackDetail> selectByOrderId(@Param("orderId") String orderId);
}