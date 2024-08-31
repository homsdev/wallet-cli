package com.homsdev.account_manager_cli.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Account {
    private String accountId;
    private BigDecimal balance;
    private String alias;
}
