package com.sample.service;

import com.sample.dao.AccountDao;
import com.sample.dao.impl.AccountDaoImpl;
import com.sample.model.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by RAJESH on 11/17/2018.
 */
@Path("accounts")
public class AccountService {

    private AccountDao accountDao = new AccountDaoImpl();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAccounts() {

        Account filteredAccount = new Account();
        List<Account> accountList = accountDao.getFilteredList(filteredAccount);

        return Response.status(Response.Status.OK).entity(accountList).build();
    }

    @GET
    @Path("/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccount(@PathParam("accountNumber") int accountNumber) {
        Account accountFilter = new Account();
        accountFilter.setAccountNumber(accountNumber);
        Account account;
        try {
            account = accountDao.get(accountFilter);
        } catch (Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(account).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAccount(Account account) {
        try {
            account = accountDao.add(account);
        } catch (Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(account).build();
    }

    @DELETE
    @Path("/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAccount(@PathParam("accountNumber") int accountNumber) {
        Account account = new Account(accountNumber);
        try {
            account = accountDao.delete(account);
        } catch (Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity("Account deleted successfully : " + account).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateAccount(Account account) {
        try {
            account = accountDao.update(account);
        } catch (Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
        }
        return Response.status(Response.Status.OK).entity(account).build();
    }


}
