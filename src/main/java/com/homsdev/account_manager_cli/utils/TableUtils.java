package com.homsdev.account_manager_cli.utils;

import com.homsdev.account_manager_cli.model.Account;
import org.springframework.shell.table.*;

import java.util.LinkedHashMap;
import java.util.List;

public class TableUtils {

    public static Table accountTable(Account... accounts) {
        List<Account> accountsList = List.of(accounts);

        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
        headers.put("accountId", "ACCOUNT ID");
        headers.put("balance", "BALANCE");
        headers.put("alias", "ALIAS");

        TableModel accountTableModel = new BeanListTableModel<>(accountsList, headers);

        TableBuilder tableBuilder = new TableBuilder(accountTableModel);

        tableBuilder.addInnerBorder(BorderStyle.fancy_light);
        tableBuilder.addHeaderBorder(BorderStyle.fancy_light);

        return tableBuilder.build();
    }
}
