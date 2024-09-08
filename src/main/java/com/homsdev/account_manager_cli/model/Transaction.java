package com.homsdev.account_manager_cli.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class Transaction {
    private String transactionId;
    private BigDecimal amount;
    private TRANSACTION_TYPE type;
    private LocalDate date;
    private Account account;
    private String alias;
}
