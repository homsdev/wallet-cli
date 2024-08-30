package com.homsdev.account_manager_cli.command;

import com.homsdev.account_manager_cli.model.Account;
import com.homsdev.account_manager_cli.service.AccountService;
import com.homsdev.account_manager_cli.shell.ShellTerminalComponent;
import com.homsdev.account_manager_cli.utils.TableUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.table.*;

import java.math.BigDecimal;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@ShellComponent
@ShellCommandGroup("Account commands")
@RequiredArgsConstructor
public class AccountCommands {

    private static final Integer SCREEN_SIZE = 200;
    private final AccountService accountService;
    private final ShellTerminalComponent terminalComponent;

    /**
     * Retrieves and list all accounts if present in a table
     */
    @ShellMethod(key = "get-all", value = "Gets all available accounts for the current user")
    public void getAllAccounts() {
        List<Account> allAccounts = accountService.getAllAccounts();

        if (allAccounts.isEmpty()) {
            terminalComponent.printSuccessMessage("There are no accounts registered yet(Please capture an account).");
        }

        Table accountTable = TableUtils.accountTable(allAccounts.toArray(new Account[0]));

        terminalComponent.printInfoMessage(accountTable.render(SCREEN_SIZE));
    }

    /**
     * 
     */
    @ShellMethod(key = "save-account", value = "Saves a new account")
    public void saveNewAccount() {
        terminalComponent.printInfoMessage("Creating new account...");

        String inputBalance = terminalComponent.readSimpleTextInput("Capture account balance", "0.00");
        String inputAlias = terminalComponent.readSimpleTextInput("Capture account alias", "defaultAlias");

        BigDecimal balance = new BigDecimal(inputBalance);

        UUID accountId = UUID.randomUUID();

        Account newAccount = Account.builder()
                .accountId(accountId.toString())
                .balance(balance)
                .alias(inputAlias)
                .build();

        terminalComponent.printSuccessMessage("Account created");
        String table = TableUtils.accountTable(accountService.saveNewAccount(newAccount)).render(SCREEN_SIZE);
        terminalComponent.printInfoMessage(table);
    }

    /**
     * 
     */
    @ShellMethod(key = "delete-acc", value = "Deletes an account")
    public void deleteAccount() {

        LinkedHashMap<String, String> accounts = new LinkedHashMap<>();

        List<Account> availableAccounts = accountService.getAllAccounts();

        availableAccounts.forEach(account -> {
            accounts.put(account.getAccountId(), account.getAlias());
        });

        String accountId = terminalComponent.readMultipleSelectionInput("Select account to remove", accounts);

        if (accountId.trim().isEmpty()) {
            terminalComponent.printErrorMessage("No valid account selected");
            return;
        }

        terminalComponent.printInfoMessage("Deleting account" + accountId);

        accountService.deleteAccount(accountId);
        terminalComponent.printSuccessMessage("Account deleted");
    }

    /**
     * 
     */
    @ShellMethod(key = "update-acc", value = "Updates selected account balance")
    public void updateAccountBalance() {
        List<Account> allAccounts = accountService.getAllAccounts();
        LinkedHashMap<String, String> accountsMap = new LinkedHashMap<>();

        allAccounts.forEach(account -> accountsMap.put(account.getAccountId(), account.getAlias()));
        String selectedAccount = terminalComponent.readMultipleSelectionInput("Select account to update balance",
                accountsMap);
        if (selectedAccount.trim().isEmpty()) {
            terminalComponent.printErrorMessage("No valid account selected");
            return;
        }

        String updatedBalance = terminalComponent.readSimpleTextInput("Type new account balance", "0.00");

        Account updatedAccount = Account.builder()
                .accountId(selectedAccount)
                .balance(new BigDecimal(updatedBalance))
                .build();

        Account result = accountService.updateAccountBalance(updatedAccount);
        
        terminalComponent.printSuccessMessage("Account balance updated");
        terminalComponent.printInfoMessage(TableUtils.accountTable(result).render(SCREEN_SIZE));
    }

}
