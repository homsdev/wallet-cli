package com.homsdev.account_manager_cli.repository;

import com.homsdev.account_manager_cli.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    List<Account> getAllAccounts();
    Optional<Account> saveAccount(Account account);
    Integer deleteAccount(String accountId);
    Optional<Account> updateBalance(Account account);
}
