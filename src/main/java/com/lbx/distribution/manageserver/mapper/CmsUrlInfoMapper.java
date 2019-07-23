package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.CmsUrlInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 接口uri
 */
@Mapper
public interface CmsUrlInfoMapper {

    /**
     * 根据角色权限查询可访问的uri
     * @param role
     * @return
     */
    List<String> selectUriByRole(@Param("role") Integer role);
}