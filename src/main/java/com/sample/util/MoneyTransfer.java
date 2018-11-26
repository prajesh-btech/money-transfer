package com.sample.util;

import com.sample.dao.AccountDao;
import com.sample.dao.TransactionDao;
import com.sample.dao.impl.AccountDaoImpl;
import com.sample.dao.impl.TransactionDaoImpl;
import com.sample.model.Account;
import com.sample.model.Transaction;

/**
 * Created by RAJESH on 11/26/2018.
 */
public class MoneyTransfer {

    private TransactionDao transactionDao = new TransactionDaoImpl();
    private AccountDao accountDao = new AccountDaoImpl();

    public void transferMoney(Transaction transaction) {
        Account sourceAccount = null;
        Account destAccount = null;

        try {

            sourceAccount = accountDao.get(new Account(transaction.getSourceAcctNumber()));
            destAccount = accountDao.get(new Account(transaction.getDestAcctNumber()));

            synchronized (sourceAccount) {
                if (sourceAccount.getBalance() < transaction.getAmount()) {
                    throw new RuntimeException("Insufficient balance in Account: " + sourceAccount);
                }
                int initialFromAccountBalance = sourceAccount.getBalance();
                sourceAccount.setBalance(initialFromAccountBalance - transaction.getAmount());

                synchronized (destAccount) {

                    int initialToAccountBalance = destAccount.getBalance();
                    destAccount.setBalance(initialToAccountBalance + transaction.getAmount());

                    try {
                        accountDao.update(sourceAccount);
                        accountDao.update(destAccount);
                        transactionDao.add(transaction);
                    } catch (Exception e) {
                        sourceAccount.setBalance(initialFromAccountBalance);
                        destAccount.setBalance(initialToAccountBalance);
                        accountDao.update(sourceAccount);
                        accountDao.update(destAccount);
                        throw e;
                    }
                }
            }

        } catch (Exception e) {
            throw e;
        }

    }
}
