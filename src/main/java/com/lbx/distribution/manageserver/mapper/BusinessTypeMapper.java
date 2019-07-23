package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.BusinessType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BusinessTypeMapper {
    /**
     * 查询所有的业务类型
     * @return
     */
    List<BusinessType> selectAllBusinessType();
}