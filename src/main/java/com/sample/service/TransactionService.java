package com.sample.service;

import com.sample.dao.AccountDao;
import com.sample.dao.impl.AccountDaoImpl;
import com.sample.dao.impl.TransactionDaoImpl;
import com.sample.model.Account;
import com.sample.model.Transaction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by RAJESH on 11/17/2018.
 */
@Path("transactions")
public class TransactionService {

    private TransactionDaoImpl transactionDao = new TransactionDaoImpl();
    private AccountDao accountDao = new AccountDaoImpl();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTransactions() {
        Transaction filteredTransaction = new Transaction();
        List<Transaction> transactionList = transactionDao.getFilteredList(filteredTransaction);

        return Response.status(Response.Status.OK).entity(transactionList).build();
    }

    @GET
    @Path("/{transactionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransaction(@PathParam("transactionId") int transactionId) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        try {
            transaction = transactionDao.get(transaction);
        } catch (Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(transaction).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response transferFunds(Transaction transaction) {
        Account sourceAccount = null;
        Account destAccount = null;

        try {
            sourceAccount = accountDao.get(new Account(transaction.getSourceAcctNumber()));
            destAccount = accountDao.get(new Account(transaction.getDestAcctNumber()));

            if (sourceAccount.getBalance() < transaction.getAmount()) {
                return Response.status(Response.Status.EXPECTATION_FAILED).entity("Insufficient balance in Account: " + sourceAccount).build();
            }

            commitOrRollbackTransaction(transaction, sourceAccount, destAccount);

        } catch (Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity("Transaction Failed: " + e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(transaction).build();
    }

    private void commitOrRollbackTransaction(Transaction transaction, Account sourceAccount, Account destAccount) {
        int initialFromAccountBalance = sourceAccount.getBalance();
        int initialToAccountBalance = destAccount.getBalance();

        sourceAccount.setBalance(initialFromAccountBalance - transaction.getAmount());
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
