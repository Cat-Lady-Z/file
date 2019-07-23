package com.lbx.distribution.manageserver.mapper;

import com.lbx.distribution.manageserver.entity.order.StatOrder;
import com.lbx.distribution.manageserver.entity.order.StatOrderEntity;
import com.lbx.distribution.manageserver.entity.order.StatOrderRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StatOrderMapper {
    int insert(StatOrderEntity record);

    /**
     * 插入数据
     * @param record
     * @return
     */
    int insertSelective(StatOrderEntity record);

    /**
     * 按照小时维度查询数据
     * @param params
     * @return
     */

    List<StatOrder> statOrderCountByHour(StatOrderRequest params);

    /**
     * 按照天的维度查询数据
     * @param params
     * @return
     */

    List<StatOrder> statOrderCountByDay(StatOrderRequest params);

    /**
     * 按照月份查询数据
     * @param params
     * @return
     */
    List<StatOrder> statOrderCountByMonth(StatOrderRequest params);

    /**
     * 按照年份查询数据
     * @param params
     * @return
     */
    List<StatOrder> statOrderCountByYear(StatOrderRequest params);

    /**
     * 删除数据
     * @param se
     */
    int delete(StatOrderEntity se);
}