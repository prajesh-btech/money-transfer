package com.sample.client;

import com.sample.dao.AccountDao;
import com.sample.dao.impl.AccountDaoImpl;
import com.sample.data.TransactionUtil;
import com.sample.model.Account;
import com.sample.model.Transaction;
import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by RAJESH on 11/18/2018.
 */
public class TransactionTest {

    private final static int PORT = 9998;
    private final static String HOST = "http://localhost/";

    private HttpServer server;
    private WebTarget target;
    private Map<Integer, Transaction> transactionsExpectedMap;
    private AccountDao accountDao = null;

    @Before
    public void setUp() throws Exception {
        URI baseUri = UriBuilder.fromUri(HOST).port(PORT).build();
        ResourceConfig config = new ResourceConfig();
        config.packages("com.sample.service");
        server = JdkHttpServerFactory.createHttpServer(baseUri, config);

        Client c = ClientBuilder.newClient();
        target = c.target(baseUri).path("transactions");

        transactionsExpectedMap = TransactionUtil.getTransactionMap();
        accountDao = new AccountDaoImpl();
    }

    @After
    public void tearDown() throws Exception {
        server.stop(0);
    }

    @Test
    public void testGetTransactionsList() {
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        List<Transaction> transactionsInResponse = response.readEntity(List.class);
        List<Transaction> transactionsExpected = new ArrayList<>(transactionsExpectedMap.values());
        assertEquals(transactionsExpected.size(), transactionsInResponse.size());
    }

    @Test
    public void testGetSingleTransaction() {
        Invocation.Builder invocationBuilder = target.path("101").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        Transaction transaction = response.readEntity(Transaction.class);
        assertTrue(transactionsExpectedMap.containsKey(transaction.getTransactionId()));
    }

    @Test
    public void testTransferFunds() {
        int TRANSFER_AMOUNT = 50;
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Transaction transaction = new Transaction(102);
        transaction.setSourceAcctNumber(1);
        transaction.setAmount(TRANSFER_AMOUNT);
        transaction.setDestAcctNumber(2);

        Account sourceAccount = accountDao.get(new Account(transaction.getSourceAcctNumber()));
        Account destAccount = accountDao.get(new Account(transaction.getDestAcctNumber()));

        int initialBalanceOfSourceAccount = sourceAccount.getBalance();
        int initialBalanceOfDestAccount = destAccount.getBalance();

        Response response = invocationBuilder.post(Entity.entity(transaction, MediaType.APPLICATION_JSON));

        int expectedSourceAccountBalance = initialBalanceOfSourceAccount - TRANSFER_AMOUNT;
        int expectedDestAccountBalance =  initialBalanceOfDestAccount + TRANSFER_AMOUNT;

        assertEquals(response.getStatus(),Response.Status.OK.getStatusCode());
        assertEquals(expectedSourceAccountBalance,sourceAccount.getBalance());
        assertEquals(expectedDestAccountBalance,destAccount.getBalance());

    }

    @Test
    public void testAddTransferWithInsufficientFunds() {
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Transaction transaction = new Transaction(102);
        transaction.setSourceAcctNumber(1);
        transaction.setAmount(500);
        transaction.setDestAcctNumber(2);

        Account sourceAccount = accountDao.get(new Account(transaction.getSourceAcctNumber()));
        Account destAccount = accountDao.get(new Account(transaction.getDestAcctNumber()));

        int initialBalanceOfSourceAccount = sourceAccount.getBalance();
        int initialBalanceOfDestAccount = destAccount.getBalance();

        Response response = invocationBuilder.post(Entity.entity(transaction, MediaType.APPLICATION_JSON));
        String responseMsg = response.readEntity(String.class);

        assertTrue(responseMsg.contains("Insufficient balance in Account"));
        assertEquals(initialBalanceOfSourceAccount,sourceAccount.getBalance());
        assertEquals(initialBalanceOfDestAccount,destAccount.getBalance());
    }

    @Test
    public void testTransferFundsRollback() {
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Transaction transaction = new Transaction(0);
        transaction.setSourceAcctNumber(1);
        transaction.setAmount(50);
        transaction.setDestAcctNumber(2);

        Account sourceAccount = accountDao.get(new Account(transaction.getSourceAcctNumber()));
        Account destAccount = accountDao.get(new Account(transaction.getDestAcctNumber()));

        int initialBalanceOfSourceAccount = sourceAccount.getBalance();
        int initialBalanceOfDestAccount = destAccount.getBalance();

        Response response = invocationBuilder.post(Entity.entity(transaction, MediaType.APPLICATION_JSON));
        String responseMsg = response.readEntity(String.class);

        assertTrue(responseMsg.contains("Invalid TransactionId entered"));
        assertEquals(initialBalanceOfSourceAccount,sourceAccount.getBalance());
        assertEquals(initialBalanceOfDestAccount,destAccount.getBalance());
    }
}
