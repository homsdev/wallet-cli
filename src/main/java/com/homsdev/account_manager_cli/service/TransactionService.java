package com.homsdev.account_manager_cli.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.homsdev.account_manager_cli.exceptions.ResourceNotCreatedException;
import com.homsdev.account_manager_cli.exceptions.ResourceNotFoundException;
import com.homsdev.account_manager_cli.exceptions.ResourceNotUpdatedException;
import com.homsdev.account_manager_cli.model.Transaction;
import com.homsdev.account_manager_cli.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.saveTransaction(transaction)
                .orElseThrow(() -> new ResourceNotCreatedException("Transaction was not created"));
    }

    public List<Transaction> findAllByAccountId(String accountId) {
        return transactionRepository.findAllByAccountId(accountId);
    }

    public Transaction findById(String transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.updateTransaction(transaction)
                .orElseThrow(() -> new ResourceNotUpdatedException("Transaction was not updated"));
    }

    public Integer deleteTransaction(String transactionId) {
        Integer result = transactionRepository.deleteTransaction(transactionId);

        if (result == 0) {
            throw new ResourceNotFoundException("Transaction not deleted");
        }

        return result;
    }

    public List<Transaction> filterByDate(LocalDate from, LocalDate to, String accountId) {
        return transactionRepository.findByDate(from, to, accountId);
    }

    public List<Transaction> filterByCurrentDay(String accountId) {
        LocalDate today = LocalDate.now();
        return transactionRepository.findByDate(today, today, accountId);
    }

    public List<Transaction> filterByCurrentMonth(String accountId) {
        LocalDate today = LocalDate.now();
        LocalDate from = today.withDayOfMonth(1);
        LocalDate to = today.withDayOfMonth(today.lengthOfMonth());
        return transactionRepository.findByDate(from, to, accountId);
    }

}
