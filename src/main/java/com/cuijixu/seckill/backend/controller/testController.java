package com.cuijixu.seckill.backend.controller;

import com.cuijixu.seckill.backend.domain.SeckillExecution;
import com.cuijixu.seckill.backend.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

@RestController
@RequestMapping("/seckill")
public class testController {
@Autowired
    TestService service;
    @RequestMapping(value = "/execution/{seckillId}",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    public String test(@PathVariable("seckillId") Long seckillId){
            SeckillExecution result = service.executeSeckill(seckillId);
        System.out.println(result.toString());
        return "ok";
    }
}
