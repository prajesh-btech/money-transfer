package com.sample.dao;

import com.sample.model.Transaction;

import java.util.List;

/**
 * Created by RAJESH on 11/17/2018.
 */
public interface TransactionDao {

    Transaction add(Transaction transaction);
    Transaction get(Transaction transaction);
    List<Transaction> getFilteredList(Transaction transaction);

}
