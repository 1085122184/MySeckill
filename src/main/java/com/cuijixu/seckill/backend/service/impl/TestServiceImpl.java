package com.cuijixu.seckill.backend.service.impl;

import com.cuijixu.seckill.backend.constant.RedisKeyPrefix;
import com.cuijixu.seckill.backend.dao.SeckillMapper;
import com.cuijixu.seckill.backend.domain.SeckillExecution;
import com.cuijixu.seckill.backend.domain.SeckillMsgBody;
import com.cuijixu.seckill.backend.entity.Pay_order;
import com.cuijixu.seckill.backend.entity.Seckill;
import com.cuijixu.seckill.backend.enums.SeckillStateEnum;
import com.cuijixu.seckill.backend.exception.SeckillException;
import com.cuijixu.seckill.backend.mq.MQProducer;
import com.cuijixu.seckill.backend.service.AccessLimitService;
import com.cuijixu.seckill.backend.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private AccessLimitService accessLimitService;
    @Resource(name = "initJedisPool")
    private JedisPool jedisPool;
    @Autowired
    private MQProducer mqProducer;
    @Autowired
    SeckillMapper mapper;

    @Override
    public SeckillExecution executeSeckill(long seckillId) throws SeckillException {
        //if (accessLimitService.tryAcquireSeckill()) {
            // 如果没有被限流器限制，则执行秒杀处理
            return handleSeckillAsync(seckillId);
       // } else {    //如果被限流器限制，直接抛出访问限制的异常
        //    System.out.println("--->ACCESS_LIMITED-->seckillId="+seckillId);
        //    throw new SeckillException(SeckillStateEnum.ACCESS_LIMIT);
        //}
    }
    @Transactional
    public SeckillExecution handleSeckillAsync(long seckillId)
            throws SeckillException {
        long threadId = Thread.currentThread().getId();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(System.nanoTime());
//        Jedis jedis = jedisPool.getResource();
//        String inventoryKey = RedisKeyPrefix.SECKILL_INVENTORY + seckillId;
//        String boughtKey = RedisKeyPrefix.BOUGHT_USERS + seckillId;
        Seckill seckill = mapper.queryById(seckillId);
        //String inventoryStr = jedis.get(inventoryKey);
        Integer inventory = seckill.getInventory();
        //int inventory = Integer.valueOf(inventoryStr);
        if (inventory <= 0) {
            //jedis.close();
            System.out.println("库存不足");
            throw new SeckillException(SeckillStateEnum.SOLD_OUT);
        }
         else {
            Date nowTime = new Date();
            boolean validTime = false;
            if (seckill != null) {
                long nowStamp = nowTime.getTime();
                if (nowStamp > seckill.getStartTime().getTime() && nowStamp < seckill.getEndTime().getTime()
                        && seckill.getInventory() > 0 && seckill.getVersion() > -1) {
                    validTime = true;
                }
            }
            if (validTime){
                long oldVersion = seckill.getVersion();
                mapper.reduceInventory(seckillId, oldVersion, oldVersion + 1);
            }
            //jedis.decr(inventoryKey);
           // jedis.close();
            System.out.println("开始秒杀---success");

            // 进入待秒杀队列，进行后续串行操作
            SeckillMsgBody msgBody = new SeckillMsgBody();
            msgBody.setSeckillId(seckillId);
            msgBody.setUserPhone(1234567);
            mqProducer.send(msgBody);
            System.out.println("mq发送---success");
            Pay_order payOrder = new Pay_order();
            payOrder.setSeckillId(seckillId);

            return new SeckillExecution(seckillId, SeckillStateEnum.ENQUEUE_PRE_SECKILL, payOrder);
        }
    }
}
