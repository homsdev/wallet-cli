package com.homsdev.account_manager_cli.model;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Transaction {
    private String transactionId;
    private BigDecimal balance;
    private TRANSACTION_TYPE type;
    private Timestamp date;
    private String accountId;
}
