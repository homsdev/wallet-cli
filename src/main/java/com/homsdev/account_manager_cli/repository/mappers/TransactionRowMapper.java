package com.homsdev.account_manager_cli.repository.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import com.homsdev.account_manager_cli.model.Account;
import com.homsdev.account_manager_cli.model.TRANSACTION_TYPE;
import com.homsdev.account_manager_cli.model.Transaction;
import com.homsdev.account_manager_cli.utils.TransactionUtils;

import java.time.LocalDate;

public class TransactionRowMapper implements RowMapper<Transaction> {

    @Override
    @Nullable
    public Transaction mapRow(@SuppressWarnings("null") ResultSet rs, int rowNum) throws SQLException {
        Account account = Account.builder()
                .accountId(rs.getString("transaction_account"))
                .build();

        TRANSACTION_TYPE type = TransactionUtils.transactionTypeParse(rs.getString("transaction_type"));

        LocalDate transaction_date = LocalDate.parse(rs.getString("transaction_date"));

        return Transaction.builder()
                .transactionId(rs.getString("transaction_id"))
                .amount(rs.getBigDecimal("transaction_amount"))
                .type(type)
                .date(transaction_date)
                .account(account).build();
    }

}
