package com.cuijixu.seckill.backend.domain;


import com.cuijixu.seckill.backend.entity.Pay_order;
import com.cuijixu.seckill.backend.enums.SeckillStateEnum;

/**
 * 封装秒杀执行后结果
 * Created by liushaoming on 2019-01-14.
 */
public class SeckillExecution {

    private long seckillId;

    //秒杀执行结果状态
    private int state;

    //状态表示
    private String stateInfo;

    //秒杀成功对象
    private Pay_order payOrder;

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", payOrder=" + payOrder +
                '}';
    }

    public SeckillExecution(long seckillId, SeckillStateEnum stateEnum, Pay_order payOrder) {
        this.seckillId = seckillId;
        this.state = stateEnum.getState();
        this.stateInfo = stateEnum.getStateInfo();
        this.payOrder = payOrder;
    }

    public SeckillExecution(long seckillId, SeckillStateEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public Pay_order getPayOrder() {
        return payOrder;
    }

    public void setPayOrder(Pay_order payOrder) {
        this.payOrder = payOrder;
    }
}
