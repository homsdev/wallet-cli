package com.homsdev.account_manager_cli.commands;

import com.homsdev.account_manager_cli.repository.AccountRepository;
import com.homsdev.account_manager_cli.repository.impl.AccountMysqlRepositoryImpl;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class AccountCommands {

    private final AccountRepository accountRepository;

    public AccountCommands(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @ShellMethod(key = "account:all")
    public String getAll(){
        System.out.println("Message");
        accountRepository.getAllAccounts();
        return "";
    }

}
