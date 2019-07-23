package com.lbx.distribution.manageserver.quartz;

import com.lbx.distribution.manageserver.entity.order.StatOrderEntity;
import com.lbx.distribution.manageserver.mapper.OrderMapper;
import com.lbx.distribution.manageserver.mapper.OrderStatusMapper;
import com.lbx.distribution.manageserver.mapper.StatOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@EnableScheduling
public class OrderStatQuartz {

    private Logger logger = LoggerFactory.getLogger(OrderStatQuartz.class);

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private StatOrderMapper statOrderMapper;

    /**
     * 定时统计订单数据：每十分钟统计一次
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    @Transactional
    public void cancelOrderTask(){
        logger.info("Order statistics Task start.");

        //从订单表读出数据
        //还没分发成功的订单不算在内
        List<StatOrderEntity> orderStat = orderStatusMapper.queryOrderStat();

        if ( orderStat != null ) {
            //记录到订单统计表中
            for (StatOrderEntity se:orderStat ) {

                //主要数据有一项为null就不插入
                if (se.getMerchantId() == null || se.getCompanyId() == null || se.getCityCode()==null || se.getShopId() == null || se.getCreateTime() == null) {
                    continue;
                }
                //从统计表中删除原有的数据
                statOrderMapper.delete(se);

                //插入新的统计数据
                statOrderMapper.insert( se );
            }
        }

        logger.info("Order statistics Task end.");
    }
}
