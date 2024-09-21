package com.homsdev.account_manager_cli.repository.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.homsdev.account_manager_cli.model.Account;

@Component
public class AccountRowMapper implements RowMapper<Account> {

    @Override
    public Account mapRow(@SuppressWarnings("null") ResultSet rs, int rowNum) throws SQLException {
        return Account.builder()
                .accountId(rs.getString("account_id"))
                .balance(rs.getBigDecimal("balance"))
                .alias(rs.getString("alias"))
                .build();
    }

}
