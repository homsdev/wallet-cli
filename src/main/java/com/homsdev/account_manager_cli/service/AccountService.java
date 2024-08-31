package com.homsdev.account_manager_cli.service;

import com.homsdev.account_manager_cli.exceptions.ResourceNotCreatedException;
import com.homsdev.account_manager_cli.exceptions.ResourceNotFoundException;
import com.homsdev.account_manager_cli.exceptions.ResourceNotUpdatedException;
import com.homsdev.account_manager_cli.model.Account;
import com.homsdev.account_manager_cli.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.getAllAccounts();
    }

    public Account saveNewAccount(Account account) {
        return accountRepository.saveAccount(account)
                .orElseThrow(() -> new ResourceNotCreatedException("Account was not created"));
    }

    public Integer deleteAccount(String accountId) {
        Integer result = accountRepository.deleteAccount(accountId);
        if (result > 0)
            return result;
        else
            throw new ResourceNotFoundException("Selected an invalid account");
    }

    public Account updateAccountBalance(Account account) {
        return accountRepository.updateBalance(account)
                .orElseThrow(() -> new ResourceNotUpdatedException("Account balance was not updated"));
    }

}
