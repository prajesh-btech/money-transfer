package com.sample.service;

import com.sample.dao.TransactionDao;
import com.sample.dao.impl.TransactionDaoImpl;
import com.sample.model.Transaction;
import com.sample.util.MoneyTransfer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by RAJESH on 11/17/2018.
 */
@Path("transactions")
public class TransactionService {

    private TransactionDao transactionDao = new TransactionDaoImpl();

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

        try {
            MoneyTransfer moneyTransfer = new MoneyTransfer();
            moneyTransfer.transferMoney(transaction);

        } catch (Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity("Transaction Failed: " + e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(transaction).build();
    }
}
