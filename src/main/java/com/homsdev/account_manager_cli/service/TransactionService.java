package com.homsdev.account_manager_cli.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.homsdev.account_manager_cli.exceptions.ResourceNotCreatedException;
import com.homsdev.account_manager_cli.exceptions.ResourceNotFoundException;
import com.homsdev.account_manager_cli.exceptions.ResourceNotUpdatedException;
import com.homsdev.account_manager_cli.model.Account;
import com.homsdev.account_manager_cli.model.TRANSACTION_TYPE;
import com.homsdev.account_manager_cli.model.Transaction;
import com.homsdev.account_manager_cli.repository.AccountRepository;
import com.homsdev.account_manager_cli.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    /**
     * Saves new transaction to database and updates vinculated account balance
     * according to newly transaction amount and type
     * 
     * @param transaction transaction POJO with info to be persisted
     * @return transaction POJO if info persisted OK
     */
    public Transaction saveTransaction(Transaction transaction) {
        Transaction savedTransaction = transactionRepository.saveTransaction(transaction)
                .orElseThrow(() -> new ResourceNotCreatedException("Transaction was not created"));
        Account vinculatedAccount = accountRepository
                .findById(savedTransaction.getAccount().getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Vinculated account was not found"));

        savedTransaction.setAccount(vinculatedAccount);
        BigDecimal vinculatedAccountBalance = vinculatedAccount.getBalance();

        if (savedTransaction.getType() == TRANSACTION_TYPE.EXPENSE) {
            vinculatedAccountBalance = vinculatedAccountBalance
                    .subtract(savedTransaction.getAmount());
        } else if (savedTransaction.getType() == TRANSACTION_TYPE.INCOME) {
            vinculatedAccountBalance = vinculatedAccountBalance
                    .add(savedTransaction.getAmount());
        }

        vinculatedAccount.setBalance(vinculatedAccountBalance);

        accountRepository.updateBalance(vinculatedAccount)
                .orElseThrow(() -> new ResourceNotUpdatedException("Unable to update account balance"));

        return savedTransaction;
    }

    /**
     * Finds all transactions vinculated to given account id
     * 
     * @param accountId
     * @return List of the account related transactions
     */
    public List<Transaction> findAllByAccountId(String accountId) {
        return transactionRepository.findAllByAccountId(accountId);
    }

    /**
     * Find an specific transaction by provided id
     * 
     * @param transactionId
     * @return Finded transaction if exists
     * @throws ResourceNotFoundException if transaction does not exists
     */
    public Transaction findById(String transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
    }

    /**
     * Updates transaction info and updates vinculated account balance
     * according to transaction amount and type
     * @param transaction
     * @return POJO of updated transaction
     */
    public Transaction updateTransaction(Transaction transaction) {

        Transaction originalTransaction = transactionRepository.findById(transaction.getTransactionId())
                .orElseThrow(() -> new ResourceNotFoundException("Given transaction does not exists"));

        Account vinculatedAccount = accountRepository.findById(transaction.getAccount().getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Vinculated account not found"));

        BigDecimal vinculatedAccountBalance = vinculatedAccount.getBalance();

        if (originalTransaction.getType() == TRANSACTION_TYPE.EXPENSE) {
            vinculatedAccountBalance = vinculatedAccountBalance.add(originalTransaction.getAmount());
        } else if (originalTransaction.getType() == TRANSACTION_TYPE.INCOME) {
            vinculatedAccountBalance = vinculatedAccountBalance.subtract(originalTransaction.getAmount());
        }

        Transaction updatedTransaction = transactionRepository.updateTransaction(transaction)
                .orElseThrow(() -> new ResourceNotUpdatedException("Transaction was not updated"));

        if (updatedTransaction.getType() == TRANSACTION_TYPE.EXPENSE) {
            vinculatedAccountBalance = vinculatedAccountBalance.subtract(updatedTransaction.getAmount());
        } else if (updatedTransaction.getType() == TRANSACTION_TYPE.INCOME) {
            vinculatedAccountBalance = vinculatedAccountBalance.add(updatedTransaction.getAmount());
        }

        vinculatedAccount.setBalance(vinculatedAccountBalance);

        accountRepository.updateBalance(vinculatedAccount)
                .orElseThrow(() -> new ResourceNotUpdatedException("Problem updating account balance"));

        return updatedTransaction;
    }

    /**
     * Deletes specific transaction by provided id
     * @param transactionId
     * @return 1 if operation was Ok
     */
    public Integer deleteTransaction(String transactionId) {
        Integer result = transactionRepository.deleteTransaction(transactionId);

        if (result == 0) {
            throw new ResourceNotFoundException("Transaction not deleted");
        }

        return result;
    }

    /**
     * Filters transactions by providing two dates
     * @param from
     * @param to
     * @param accountId
     * @return
     */
    public List<Transaction> filterByDate(LocalDate from, LocalDate to, String accountId) {
        return transactionRepository.findByDate(from, to, accountId);
    }

    /**
     * Filters transactions vinculated to provided account id by current day
     * @param accountId
     * @return
     */
    public List<Transaction> filterByCurrentDay(String accountId) {
        LocalDate today = LocalDate.now();
        return transactionRepository.findByDate(today, today, accountId);
    }

    /**
     * Filters transactions vinculated to provided account id by current month
     * @param accountId
     * @return
     */
    public List<Transaction> filterByCurrentMonth(String accountId) {
        LocalDate today = LocalDate.now();
        LocalDate from = today.withDayOfMonth(1);
        LocalDate to = today.withDayOfMonth(today.lengthOfMonth());
        return transactionRepository.findByDate(from, to, accountId);
    }

}
