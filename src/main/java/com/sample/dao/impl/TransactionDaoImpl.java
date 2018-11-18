package com.sample.dao.impl;

import com.sample.dao.TransactionDao;
import com.sample.data.TransactionUtil;
import com.sample.model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by RAJESH on 11/17/2018.
 */
public class TransactionDaoImpl implements TransactionDao {

    private static Logger log = Logger.getLogger(TransactionDaoImpl.class.getName());

    public Transaction add(Transaction transaction) {
        if (transaction.getTransactionId() <= 0) {
            throw new RuntimeException("Invalid TransactionId entered: " + transaction.getTransactionId());
        }
        Map<Integer, Transaction> transactionMap = TransactionUtil.getTransactionMap();
        if (transactionMap != null) {
            transactionMap.put(transaction.getTransactionId(), transaction);
        }

        log.info("Transaction added successfully: " + transaction);

        return transaction;
    }

    public Transaction get(Transaction transaction) {
        Map<Integer, Transaction> transactionMap = TransactionUtil.getTransactionMap();
        if (transactionMap != null && transactionMap.containsKey(transaction.getTransactionId())) {
            transaction = transactionMap.get(transaction.getTransactionId());
        } else if (transactionMap != null) {
            throw new RuntimeException("Transaction not found with this transactionId: " + transaction.getTransactionId());
        }
        return transaction;
    }

    public List<Transaction> getFilteredList(Transaction transaction) {
        Map<Integer, Transaction> transactionMap = TransactionUtil.getTransactionMap();
        List<Transaction> transactionList = null;
        if (transactionMap != null) {
            transactionList = new ArrayList<>(transactionMap.values());
        }
        return transactionList;
    }
}
