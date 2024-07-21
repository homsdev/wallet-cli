package com.homsdev.account_manager_cli.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {
    private String accountId;
    private BigDecimal balance;
}
