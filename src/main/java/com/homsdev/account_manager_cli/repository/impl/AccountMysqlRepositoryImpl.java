package com.homsdev.account_manager_cli.repository.impl;

import com.homsdev.account_manager_cli.model.Account;
import com.homsdev.account_manager_cli.repository.AccountRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AccountMysqlRepositoryImpl implements AccountRepository {

    @Getter
    @Value("${account.findAll}")
    private String getAllAccountsQuery;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AccountMysqlRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> getAllAccounts() {
        return null;
    }

    @Override
    public Optional<Account> saveAccount(Account account) {
        return Optional.empty();
    }

    @Override
    public Integer deleteAccount(String accountId) {
        return null;
    }

    @Override
    public Optional<Account> updateBalance(Account account) {
        return Optional.empty();
    }
}
