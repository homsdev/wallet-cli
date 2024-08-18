package com.homsdev.account_manager_cli.repository.impl;

import com.homsdev.account_manager_cli.model.TRANSACTION_TYPE;
import com.homsdev.account_manager_cli.model.Transaction;
import com.homsdev.account_manager_cli.repository.TransactionRepository;
import com.homsdev.account_manager_cli.utils.TransactionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//TODO: Create a rowMapper for transactions
@Repository
public class TransactionMySQLRepositoryImpl implements TransactionRepository {

    @Value("${transaction.save}")
    private String saveTransactionQuery;
    @Value("${transaction.findAllByAccountId}")
    private String findAllByAccountIdTransactionQuery;
    @Value("${transaction.findById}")
    private String findByIdTransactionQuery;
    @Value("${transaction.update}")
    private String updateTransactionQuery;
    @Value("${transaction.delete}")
    private String deleteTransactionQuery;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public TransactionMySQLRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Transaction> saveTransaction(Transaction transaction) {
        Map<String, Object> params = new HashMap<>();
        params.put("transactionId", transaction.getTransactionId());
        params.put("amount", transaction.getAmount());
        params.put("type", transaction.getType().toString());
        params.put("date", transaction.getDate().toString());
        params.put("accountId", transaction.getAccount().getAccountId());
        int updated = jdbcTemplate.update(saveTransactionQuery, params);
        return updated > 0 ? Optional.of(transaction) : Optional.empty();
    }

    @Override
    public List<Transaction> findAllByAccountId(String accountId) {
        Map<String, Object> params = new HashMap<>();
        params.put("accountId", accountId);
        return jdbcTemplate.query(findAllByAccountIdTransactionQuery, params, (rs, rowNum) -> {
            TRANSACTION_TYPE type = TransactionUtils
                    .transactionTypeParse(rs.getString("transaction_type"));

            LocalDate transactionDate = LocalDate
                    .parse(rs.getString("transaction_date"));

            return Transaction.builder()
                    .transactionId(rs.getString("transaction_id"))
                    .amount(rs.getBigDecimal("transaction_amount"))
                    .type(type)
                    .date(transactionDate)
                    .account(null)
                    .build();
        });
    }

    @Override
    public Optional<Transaction> findById(String transactionId) {
        Map<String, Object> params = new HashMap<>();
        params.put("transactionId", transactionId);
        List<Transaction> transaction = jdbcTemplate.query(findByIdTransactionQuery, params, (rs, rowNum) -> {
            TRANSACTION_TYPE type = TransactionUtils
                    .transactionTypeParse(rs.getString("transaction_type"));

            LocalDate transactionDate = LocalDate
                    .parse(rs.getString("transaction_date"));

            return Transaction.builder()
                    .transactionId(rs.getString("transaction_id"))
                    .amount(rs.getBigDecimal("transaction_amount"))
                    .type(type)
                    .date(transactionDate)
                    .account(null)
                    .build();
        });
        return transaction.stream().findFirst();
    }

    @Override
    public Optional<Transaction> updateTransaction(Transaction transaction) {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", transaction.getAmount());
        params.put("type", transaction.getType().toString());
        params.put("date", transaction.getDate().toString());
        params.put("transactionId", transaction.getTransactionId());

        int updated = jdbcTemplate.update(updateTransactionQuery, params);

        return updated > 0 ? Optional.of(transaction) : Optional.empty();
    }

    @Override
    public Integer deleteTransaction(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("transactionId", id);
        return jdbcTemplate.update(deleteTransactionQuery, params);
    }
}
