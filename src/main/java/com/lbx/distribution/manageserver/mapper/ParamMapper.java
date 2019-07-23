package com.lbx.distribution.manageserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 获取公共参数
 */
@Mapper
public interface ParamMapper {

    /**
     * 获取paramValue
     * @return
     */
    String queryParamByKey(@Param("paramKey") String paramKey);
}
