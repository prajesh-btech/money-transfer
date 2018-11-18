package com.sample.data;

import com.sample.model.Transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by RAJESH on 11/18/2018.
 */
public class TransactionUtil {

    private static Map<Integer, Transaction> transactionMap = null;

    static {
        transactionMap = new HashMap<>();
        Transaction transaction = new Transaction(101);
        transaction.setAmount(50);
        transaction.setSourceAcctNumber(1);
        transaction.setDestAcctNumber(2);

        transactionMap.put(transaction.getTransactionId(), transaction);
    }

    public static Map<Integer, Transaction> getTransactionMap() {
        if (transactionMap != null && !transactionMap.isEmpty()) {
            return transactionMap;
        }
        return null;
    }
}
