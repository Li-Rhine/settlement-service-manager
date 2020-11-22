package com.imooc.settlementservicemanager.po;

import com.imooc.settlementservicemanager.enummeration.SettlementStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description：
 * @Author： Rhine
 * @Date： 2020/11/22 22:50
 **/
@Getter
@Setter
@ToString
public class SettlementPO {

    private Integer id;
    private Integer orderId;
    private Integer transactionId;
    private SettlementStatus status;
    private BigDecimal amount;
    private Date date;

}
