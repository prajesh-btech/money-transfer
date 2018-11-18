package com.sample.client;

import com.sample.data.AccountUtil;
import com.sample.model.Account;
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
public class AccountTest {

    private final static int PORT = 9998;
    private final static String HOST = "http://localhost/";

    private HttpServer server;
    private WebTarget target;
    private Map<Integer, Account> accountsExpectedMap;

    @Before
    public void setUp() throws Exception {
        URI baseUri = UriBuilder.fromUri(HOST).port(PORT).build();
        ResourceConfig config = new ResourceConfig();
        config.packages("com.sample.service");
        server = JdkHttpServerFactory.createHttpServer(baseUri, config);

        Client c = ClientBuilder.newClient();
        target = c.target(baseUri).path("accounts");

        accountsExpectedMap = AccountUtil.getAccountMap();
    }

    @After
    public void tearDown() throws Exception {
        server.stop(0);
    }

    @Test
    public void testGetAccountList() {
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        List<Account> accountsInResponse = response.readEntity(List.class);
        List<Account> accountsExpected = new ArrayList<>(accountsExpectedMap.values());
        assertEquals(accountsExpected.size(), accountsInResponse.size());
    }

    @Test
    public void testGetSingleAccount() {
        Invocation.Builder invocationBuilder = target.path("2").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        Account account = response.readEntity(Account.class);
        assertTrue(accountsExpectedMap.containsKey(account.getAccountNumber()));
    }

    @Test
    public void testAddAccount() {
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Account account = new Account(3);
        account.setName("Kuba");
        account.setBalance(400);
        Response response = invocationBuilder.post(Entity.entity(account, MediaType.APPLICATION_JSON));
        Account accountInResponse = response.readEntity(Account.class);
        assertTrue(accountsExpectedMap.containsKey(accountInResponse.getAccountNumber()));
    }

    @Test
    public void testAddDuplicateAccount() {
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Account account = new Account(1);
        account.setName("Kuba");
        account.setBalance(400);
        Response response = invocationBuilder.post(Entity.entity(account, MediaType.APPLICATION_JSON));
        String responseMsg = response.readEntity(String.class);
        assertTrue(responseMsg.contains("Account exists with this AccountNumber"));
    }

    @Test
    public void testAddInvalidAccount() {
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Account account = new Account(0);
        account.setName("Kuba");
        account.setBalance(400);
        Response response = invocationBuilder.post(Entity.entity(account, MediaType.APPLICATION_JSON));
        String responseMsg = response.readEntity(String.class);
        assertTrue(responseMsg.contains("Invalid AccountNumber entered"));
    }

    @Test
    public void testUpdateAccount() {
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Account account = new Account(2);
        account.setName("Rajeshkumar");
        account.setBalance(400);
        Response response = invocationBuilder.put(Entity.entity(account, MediaType.APPLICATION_JSON));
        Account accountInResponse = response.readEntity(Account.class);
        assertEquals(account.getName(), accountInResponse.getName());
    }

    @Test
    public void testDeleteAccount() {
        Invocation.Builder invocationBuilder = target.path("1").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.delete();
        String responseMsg = response.readEntity(String.class);
        String expectedMsg = "Account deleted successfully";
        assertTrue(responseMsg.contains(expectedMsg));
    }

    @Test
    public void testDeleteAccountNotFound() {
        Invocation.Builder invocationBuilder = target.path("6").request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.delete();
        String responseMsg = response.readEntity(String.class);
        String expectedMsg = "Account not found with this AccountNumber";
        assertTrue(responseMsg.contains(expectedMsg));
    }

}
