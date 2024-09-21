package com.homsdev.account_manager_cli.repository;

import com.homsdev.account_manager_cli.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Optional<Transaction> saveTransaction(Transaction transaction);

    List<Transaction> findAllByAccountId(String accountId);

    Optional<Transaction> findById(String transactionId);

    Optional<Transaction> updateTransaction(Transaction transaction);

    Integer deleteTransaction(String id);

    List<Transaction> findByDate(LocalDate from, LocalDate to, String accountId);
}
