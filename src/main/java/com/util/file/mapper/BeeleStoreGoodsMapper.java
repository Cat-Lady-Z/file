package com.util.file.mapper;

import com.util.file.entity.BeeleStoreGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BeeleStoreGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BeeleStoreGoods record);

    int insertSelective(BeeleStoreGoods record);

    BeeleStoreGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BeeleStoreGoods record);

    int updateByPrimaryKey(BeeleStoreGoods record);

    /**
     * 批量导入
     * @param batchList
     * @return
     */
    int insertBatch(@Param("batchList") List<BeeleStoreGoods> batchList);
}