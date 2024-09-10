package com.homsdev.account_manager_cli.command;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.Table;

import com.homsdev.account_manager_cli.model.Account;
import com.homsdev.account_manager_cli.model.TRANSACTION_TYPE;
import com.homsdev.account_manager_cli.model.Transaction;
import com.homsdev.account_manager_cli.service.AccountService;
import com.homsdev.account_manager_cli.service.TransactionService;
import com.homsdev.account_manager_cli.shell.ShellTerminalComponent;
import com.homsdev.account_manager_cli.utils.TableUtils;

import lombok.RequiredArgsConstructor;

@ShellComponent
@ShellCommandGroup("Transaction commands")
@RequiredArgsConstructor
public class TransactionCommands {

    private static final Integer SCREEN_SIZE = 200;

    private final TransactionService transactionService;
    private final AccountService accountService;

    private final ShellTerminalComponent terminalComponent;

    @ShellMethod(key = "save-t", value = "Register a new transaction")
    public void registerTransaction() {
        // Selecting account
        List<Account> userAccounts = accountService.getAllAccounts();

        LinkedHashMap<String, String> options = new LinkedHashMap<>();

        for (Account account : userAccounts) {
            options.put(account.getAccountId(), account.getAlias());
        }

        String selectedAccount = terminalComponent.readMultipleSelectionInput("Select account", options);

        // Capture transaction alias
        String description = terminalComponent.readSimpleTextInput("Capture description", "<<empty>>");

        // Capture transaction amount
        String transactionAmount = terminalComponent.readSimpleTextInput("Capture transaction amount", "0.00");

        // Select transaction type
        LinkedHashMap<String, String> transactionTypeItems = new LinkedHashMap<>();
        transactionTypeItems.put(TRANSACTION_TYPE.EXPENSE.toString(), "Expense");
        transactionTypeItems.put(TRANSACTION_TYPE.INCOME.toString(), "Income");

        String selectedType = terminalComponent.readMultipleSelectionInput("Select transaction type",
                transactionTypeItems);

        // Capture transaction date
        LocalDate transactionDate = null;
        while (transactionDate == null) {
            transactionDate = terminalComponent.readDateTypeInput("Capture date").orElse(null);
        }

        // Generate UUID
        String transactionId = UUID.randomUUID().toString();

        Account account = Account.builder().accountId(selectedAccount).build();

        Transaction newTransaction = Transaction.builder()
                .transactionId(transactionId)
                .account(account)
                .amount(new BigDecimal(transactionAmount))
                .date(transactionDate)
                .type(TRANSACTION_TYPE.valueOf(selectedType))
                .alias(description)
                .build();

        Transaction savedTransaction = transactionService.saveTransaction(newTransaction);

        Table transactionTable = TableUtils.transactionTable(savedTransaction);

        terminalComponent.printSuccessMessage("transaction created");
        terminalComponent.printInfoMessage(transactionTable.render(SCREEN_SIZE));
    }

    @ShellMethod(key = "find-t", value = "Gets all transactions by selected Account")
    public void getAllTransactionsByAccount() {
        List<Account> allAccounts = accountService.getAllAccounts();

        LinkedHashMap<String, String> options = new LinkedHashMap<>();

        for (Account account : allAccounts) {
            options.put(account.getAccountId(), account.getAlias());
        }

        String selectedAccount = terminalComponent.readMultipleSelectionInput("Select account", options);

        List<Transaction> transactions = transactionService.findAllByAccountId(selectedAccount);

        Table transactionsTable = TableUtils.transactionTable(transactions.toArray(new Transaction[0]));

        terminalComponent.printInfoMessage(transactionsTable.render(SCREEN_SIZE));
    }

    @ShellMethod(key = "navigate", value = "Helps to navigate registered transactions for the given account")
    public void filterTransactions(
            @ShellOption(defaultValue = "false", value = { "-t",
                    "--today" }, help = "Displays transactions of current day") Boolean today,
            @ShellOption(defaultValue = "false", value = { "-m",
                    "--month" }, help = "Displays transactions of current month") Boolean month,
            @ShellOption(defaultValue = "0", value = { "-f",
                    "--from" }, help = "Displays transactions of selected month") Integer fromMonth) {
        List<Account> userAccounts = accountService.getAllAccounts();

        LinkedHashMap<String, String> options = new LinkedHashMap<>();

        for (Account account : userAccounts) {
            options.put(account.getAccountId(), account.getAlias());
        }

        String selectedAccount = terminalComponent.readMultipleSelectionInput("select account", options);

        List<Transaction> filteredTransactions = null;
        LocalDate todayDate = LocalDate.now();
        
        if (fromMonth == 0) {
            if (today) {
                filteredTransactions = transactionService.filterByCurrentDay(selectedAccount);
            } else if (month) {
                filteredTransactions = transactionService.filterByCurrentMonth(selectedAccount);
            } else {
                System.out.println("Invalid Date");
                return;
            }
        } else {
            int year = todayDate.getYear();
            int firstDay = 1;

            LocalDate from = LocalDate.of(year, fromMonth, firstDay);

            int lastDay = from.lengthOfMonth();

            LocalDate to = LocalDate.of(year, fromMonth, lastDay);

            filteredTransactions = transactionService.filterByDate(from, to, selectedAccount);
            System.out.printf("Searching from %s to %s\n", from, to);
        }

        Table filteredTransactionsTable = TableUtils.transactionTable(filteredTransactions.toArray(new Transaction[0]));

        terminalComponent.printInfoMessage(filteredTransactionsTable.render(SCREEN_SIZE));
    }
}
