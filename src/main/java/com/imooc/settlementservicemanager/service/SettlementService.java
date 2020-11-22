package com.imooc.settlementservicemanager.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 23:00
 **/
@Service
public class SettlementService {

    Random random = new Random(25);

    public Integer settlement(Integer accountId, BigDecimal amount) {
        // 这里实际要实现真正的结算业务代码
        //根据业务需要
        return random.nextInt(10000000);
    }
}
