package com.sample.dao;

import com.sample.model.Account;

import java.util.List;

/**
 * Created by RAJESH on 11/17/2018.
 */
public interface AccountDao {

    Account add(Account account);
    Account update(Account account);
    Account delete(Account account);
    Account get(Account account);
    List<Account> getFilteredList(Account filterAccount);
}
