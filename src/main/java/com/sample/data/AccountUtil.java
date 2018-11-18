package com.sample.data;

import com.sample.model.Account;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by RAJESH on 11/17/2018.
 */
public class AccountUtil {

    private static Map<Integer, Account> accountMap = null;

    static {
        accountMap = new HashMap<>();
        Account account1 = new Account(1);
        account1.setName("Rajesh");
        account1.setBalance(100);

        accountMap.put(account1.getAccountNumber(), account1);

        Account account2 = new Account(2);
        account2.setName("Kris");
        account2.setBalance(200);

        accountMap.put(account2.getAccountNumber(), account2);

    }

    public static Map<Integer, Account> getAccountMap() {
        if (accountMap != null && !accountMap.isEmpty()) {
            return accountMap;
        }
        return null;
    }
}
