package com.cuijixu.seckill.backend.service;

import com.cuijixu.seckill.backend.domain.SeckillExecution;
import com.cuijixu.seckill.backend.exception.SeckillException;

public interface TestService {
    SeckillExecution executeSeckill(long seckillId)
            throws SeckillException;
}
