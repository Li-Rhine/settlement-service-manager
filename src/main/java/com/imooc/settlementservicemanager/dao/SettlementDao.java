package com.imooc.settlementservicemanager.dao;

import com.imooc.settlementservicemanager.po.SettlementPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 17:23
 **/
@Repository
@Mapper
public interface SettlementDao {

    @Insert("INSERT INTO settlement (order_id, transaction_id, status, amount, date) VALUES (#{orderId}, #{transactionId}, #{status}, #{amount}, #{date})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(SettlementPO settlementPO);
}
