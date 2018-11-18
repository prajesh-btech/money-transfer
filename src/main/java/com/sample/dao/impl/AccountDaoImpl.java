package com.sample.dao.impl;

import com.sample.dao.AccountDao;
import com.sample.data.AccountUtil;
import com.sample.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by RAJESH on 11/17/2018.
 */
public class AccountDaoImpl implements AccountDao {

    private static Logger log = Logger.getLogger(AccountDaoImpl.class.getName());

    public Account add(Account account) {
        if(account.getAccountNumber() <= 0 ) {
            throw new RuntimeException("Invalid AccountNumber entered: "+account.getAccountNumber());
        }

        Map<Integer, Account> accountMap = AccountUtil.getAccountMap();
        if(accountMap != null && accountMap.containsKey(account.getAccountNumber())) {
            throw new RuntimeException("Account exists with this AccountNumber: "+account.getAccountNumber());
        }

        if(accountMap != null) {
            accountMap.put(account.getAccountNumber(), account);
        }
        log.info("Account added successfully: "+account);
        return account;
    }

    public Account update(Account account) {
        Map<Integer, Account> accountMap = AccountUtil.getAccountMap();
        if(accountMap != null && accountMap.containsKey(account.getAccountNumber())) {
           accountMap.put(account.getAccountNumber(), account);
        }  else if (accountMap != null) {
            throw new RuntimeException("Account not found with this AccountNumber: "+account.getAccountNumber() + " to update" );
        }
        log.info("Account updated successfully: "+account);
        return account;
    }

    public Account delete(Account account) {
        Map<Integer, Account> accountMap = AccountUtil.getAccountMap();
        if(accountMap != null && accountMap.containsKey(account.getAccountNumber())) {
            account = accountMap.remove(account.getAccountNumber());
        } else if (accountMap != null) {
            throw new RuntimeException("Account not found with this AccountNumber: "+account.getAccountNumber() + " to delete" );
        }
        log.info("Account deleted successfully: "+account);
        return account;
    }

    public Account get(Account account) {
        Map<Integer, Account> accountMap = AccountUtil.getAccountMap();
        if(accountMap != null && accountMap.containsKey(account.getAccountNumber())) {
            account = accountMap.get(account.getAccountNumber());
        } else if (accountMap != null) {
            throw new RuntimeException("Account not found with this AccountNumber: "+account.getAccountNumber());
        }
        return account;
    }

    public List<Account> getFilteredList(Account filterAccount) {
        List<Account> accountList = null;
        Map<Integer, Account> accountMap = AccountUtil.getAccountMap();
        if(accountMap != null) {
            accountList = new ArrayList<>(accountMap.values());
        }
        return accountList;
    }
}
