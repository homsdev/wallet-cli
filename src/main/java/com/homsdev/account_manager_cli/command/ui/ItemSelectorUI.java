package com.homsdev.account_manager_cli.command.ui;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.homsdev.account_manager_cli.enums.MenuOperation;
import com.homsdev.account_manager_cli.exceptions.InvalidInputException;
import com.homsdev.account_manager_cli.model.Account;
import com.homsdev.account_manager_cli.model.Transaction;
import com.homsdev.account_manager_cli.shell.ShellTerminalComponent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ItemSelectorUI {

    private final ShellTerminalComponent terminalComponent;

    public Transaction transactionItemSelector(List<Transaction> transactionsList) {
        LinkedHashMap<String, String> items = new LinkedHashMap<>();

        for (Transaction transaction : transactionsList) {
            items.put(transaction.getTransactionId(), transaction.getAlias());
        }

        String selectedItem = terminalComponent.readMultipleSelectionInput("Select transaction to operate", items);

        return transactionsList.stream()
                .filter(transaction -> transaction.getTransactionId().equals(selectedItem))
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("Invalid input"));
    }

    public Account accountItemSelector(List<Account> accountsList) {
        LinkedHashMap<String, String> items = new LinkedHashMap<>();
        for (Account account : accountsList) {
            items.put(account.getAccountId(), account.getAlias());
        }

        String selectedItem = terminalComponent.readMultipleSelectionInput("Select account to operate", items);

        return accountsList.stream()
                .filter(account -> account.getAccountId().equals(selectedItem))
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("Invalid account selected"));
    }

    public MenuOperation operationItemSelector() {
        LinkedHashMap<String, String> items = new LinkedHashMap<>();

        for (MenuOperation operation : MenuOperation.values()) {
            items.put(operation.name(), operation.getName());
        }

        String selectedOperation = terminalComponent.readMultipleSelectionInput("Which operation want to execute",
                items);

        return MenuOperation.fromStr(selectedOperation);
    }
}
