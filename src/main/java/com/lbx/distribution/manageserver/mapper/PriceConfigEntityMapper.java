package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.channel.PriceConfigEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PriceConfigEntityMapper {
    int deleteByPrimaryKey(Integer confId);

    int insert(PriceConfigEntity record);

    int insertSelective(PriceConfigEntity record);

    PriceConfigEntity selectByPrimaryKey(Integer confId);

    int updateByPrimaryKeySelective(PriceConfigEntity record);

    int updateByPrimaryKey(PriceConfigEntity record);
}