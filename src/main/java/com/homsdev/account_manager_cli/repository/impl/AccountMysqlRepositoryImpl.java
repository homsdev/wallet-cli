package com.homsdev.account_manager_cli.repository.impl;

import com.homsdev.account_manager_cli.model.Account;
import com.homsdev.account_manager_cli.repository.AccountRepository;
import com.homsdev.account_manager_cli.repository.mappers.AccountRowMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountMysqlRepositoryImpl implements AccountRepository {

    @Value("${account.findAll}")
    private String getAllAccountsQuery;
    @Value("${account.save}")
    private String saveAccountQuery;
    @Value("${account.delete}")
    private String deleteAccountQuery;
    @Value("${account.update}")
    private String updateAccountBalanceQuery;
    @Value("${account.findById}")
    private String findAccountByIdQuery;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final AccountRowMapper accountRowMapper;

    @Override
    public Optional<Account> findById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", id);
        return jdbcTemplate
                .query(findAccountByIdQuery, params, new AccountRowMapper())
                .stream()
                .findFirst();
    }

    @Override
    public List<Account> getAllAccounts() {
        return jdbcTemplate.query(getAllAccountsQuery, accountRowMapper);
    }

    @Override
    public Optional<Account> saveAccount(Account account) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", account.getAccountId());
        params.put("balance", account.getBalance());
        params.put("alias", account.getAlias());
        int updated = jdbcTemplate.update(saveAccountQuery, params);
        return updated > 0 ? Optional.of(account) : Optional.empty();
    }

    @Override
    public Integer deleteAccount(String accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", accountId);
        return jdbcTemplate.update(deleteAccountQuery, params);
    }

    @Override
    public Optional<Account> updateBalance(Account account) {
        Map<String, Object> params = new HashMap<>();
        params.put("updatedBalance", account.getBalance());
        params.put("accountId", account.getAccountId());
        int updated = jdbcTemplate.update(updateAccountBalanceQuery, params);
        return updated > 0 ? Optional.of(account) : Optional.empty();
    }
}
