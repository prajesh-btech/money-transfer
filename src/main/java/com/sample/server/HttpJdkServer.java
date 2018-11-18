package com.sample.server;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by RAJESH on 11/17/2018.
 */
public class HttpJdkServer {
    private final static int PORT = 9998;
    private final static String HOST = "http://localhost/";

    public static void main(String[] args) {
        URI baseUri = UriBuilder.fromUri(HOST).port(PORT).build();
        ResourceConfig config = new ResourceConfig();
        config.packages("com.sample.service");
        JdkHttpServerFactory.createHttpServer(baseUri, config);

    }
}
