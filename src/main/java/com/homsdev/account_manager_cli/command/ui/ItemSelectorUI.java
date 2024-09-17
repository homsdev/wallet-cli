package com.homsdev.account_manager_cli.command.ui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.jline.terminal.Terminal;
import org.springframework.shell.component.SingleItemSelector;
import org.springframework.shell.component.SingleItemSelector.SingleItemSelectorContext;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.standard.AbstractShellComponent;
import org.springframework.stereotype.Component;

import com.homsdev.account_manager_cli.enums.MenuOperation;
import com.homsdev.account_manager_cli.exceptions.InvalidInputException;
import com.homsdev.account_manager_cli.model.Account;
import com.homsdev.account_manager_cli.model.TRANSACTION_TYPE;
import com.homsdev.account_manager_cli.model.Transaction;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ItemSelectorUI extends AbstractShellComponent {

    private final Terminal terminal;

    public Transaction transactionItemSelector(List<Transaction> transactionsList) {
        LinkedHashMap<String, String> items = new LinkedHashMap<>();

        for (Transaction transaction : transactionsList) {
            items.put(transaction.getTransactionId(), transaction.getAlias());
        }

        String selectedItem = multipleSelectorHelper("Select transaction to operate", items);

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

        String selectedItem = multipleSelectorHelper("Select account to operate", items);

        return accountsList.stream()
                .filter(account -> account.getAccountId().equals(selectedItem))
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("Invalid account selected"));
    }

    public TRANSACTION_TYPE transactionTypeSelector() {
        LinkedHashMap<String, String> items = new LinkedHashMap<>();
        for (TRANSACTION_TYPE type : TRANSACTION_TYPE.values()) {
            items.put(type.name(), type.toString());
        }
        String result = multipleSelectorHelper("Select transaction type", items);

        return TRANSACTION_TYPE.valueOf(result);
    }

    public MenuOperation operationItemSelector() {
        LinkedHashMap<String, String> items = new LinkedHashMap<>();

        for (MenuOperation operation : MenuOperation.values()) {
            items.put(operation.name(), operation.getName());
        }

        String selectedOperation = multipleSelectorHelper("Which operation want to execute",
                items);

        return MenuOperation.fromStr(selectedOperation);
    }

    private String multipleSelectorHelper(String instruction, LinkedHashMap<String, String> items) {

        List<SelectorItem<String>> selectionItems = new ArrayList<>();

        items.entrySet().stream().forEach(item -> {
            selectionItems.add(SelectorItem.of(item.getValue(), item.getKey()));
        });

        SingleItemSelector<String, SelectorItem<String>> singleItemSelectorComponent = new SingleItemSelector<>(
                terminal, selectionItems, instruction, null);

        singleItemSelectorComponent.setResourceLoader(getResourceLoader());
        singleItemSelectorComponent.setTemplateExecutor(getTemplateExecutor());

        SingleItemSelectorContext<String, SelectorItem<String>> context = singleItemSelectorComponent
                .run(SingleItemSelectorContext.empty());

        String selectedValue = context.getResultItem()
                .map(SelectorItem::getItem)
                .orElseGet(() -> "");

        return selectedValue;
    }
}