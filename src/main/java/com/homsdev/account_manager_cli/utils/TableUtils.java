package com.homsdev.account_manager_cli.utils;

import com.homsdev.account_manager_cli.model.Account;
import com.homsdev.account_manager_cli.model.Transaction;

import org.springframework.shell.table.*;

import java.util.LinkedHashMap;
import java.util.List;

public class TableUtils {

    private static <T> Table customTable(List<T> items, LinkedHashMap<String, Object> headers) {
        TableModel tableModel = new BeanListTableModel<>(items, headers);
        TableBuilder tableBuilder = new TableBuilder(tableModel);

        tableBuilder.addInnerBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_light);

        return tableBuilder.build();
    }

    public static Table accountTable(Account... accounts) {
        List<Account> accountsList = List.of(accounts);

        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("accountId", "ACCOUNT ID");
        headers.put("balance", "BALANCE");
        headers.put("alias", "ALIAS");

        return customTable(accountsList, headers);
    }

    public static Table transactionTable(Transaction... transactions) {
        List<Transaction> transactionsList = List.of(transactions);

        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("transactionId", "TRANSACTION ID");
        headers.put("alias", "ALIAS");
        headers.put("amount", "BALANCE");
        headers.put("type", "TYPE");
        headers.put("date", "DATE");

        return customTable(transactionsList, headers);
    }
}
