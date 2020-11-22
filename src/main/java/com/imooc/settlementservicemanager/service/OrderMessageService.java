package com.imooc.settlementservicemanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.settlementservicemanager.dao.SettlementDao;
import com.imooc.settlementservicemanager.dto.OrderMessageDTO;
import com.imooc.settlementservicemanager.enummeration.SettlementStatus;
import com.imooc.settlementservicemanager.po.SettlementPO;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 16:49
 **/
@Slf4j
@Service
public class OrderMessageService {

    @Autowired
    SettlementService settlementService;

    @Autowired
    SettlementDao settlementDao;

    @Autowired
    ObjectMapper objectMapper;

    @Async
    public void handleMessage() throws IOException, TimeoutException, InterruptedException {
        log.info("start listening message");
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("101.132.104.74");
        try(Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();) {

            channel.exchangeDeclare(
                    "exchange.settlement.order",
                    BuiltinExchangeType.FANOUT,
                    true,
                    false,
                    null
            );
            channel.queueDeclare(
                    "queue.settlement",
                    true,
                    false,
                    false,
                    null
            );
            channel.queueBind(
                    "queue.settlement",
                    "exchange.order.settlement",
                    "key.settlement"
            );

            channel.basicConsume("queue.settlement", true, deliverCallback, consumerTag -> {});
            while (true) {
                Thread.sleep(100000);
            }
        }
    }


    //具体消费逻辑
    DeliverCallback deliverCallback = ((consumerTag, message) -> {
        String messageBody = new String(message.getBody());

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("101.132.104.74");

        try{
            //将消息反序列化
            OrderMessageDTO orderMessageDTO = objectMapper.readValue(messageBody, OrderMessageDTO.class);
            SettlementPO settlementPO = new SettlementPO();
            settlementPO.setAmount(orderMessageDTO.getPrice());
            settlementPO.setDate(new Date());
            settlementPO.setOrderId(orderMessageDTO.getOrderId());

            Integer settlementId = settlementService.settlement(orderMessageDTO.getAccountId(), orderMessageDTO.getPrice());
            settlementPO.setStatus(SettlementStatus.SUCCESS);
            settlementPO.setTransactionId(settlementId);
            settlementDao.insert(settlementPO);


            try(Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();) {

                String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
                channel.basicPublish(
                        "exchange.settlement.order",
                        "key.order",
                        null,
                        messageToSend.getBytes());
            }


        }catch (Exception e) {
            log.error(e.getMessage(), e);
        }


    });
            
            
            
}
