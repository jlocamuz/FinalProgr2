package com.mycompany.myapp.service;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.mycompany.myapp.web.rest.OrdenResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class HttpRequestiesIntegrationTest {

    @Autowired
    private OrdenResource ordenResource;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setup() {
        WireMockConfiguration config = WireMockConfiguration.options().dynamicPort();
        wireMockServer = new WireMockServer(config);
        wireMockServer.start();
        WireMock.configureFor(wireMockServer.port());
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testGetRequest() {
        // Configure WireMock behavior
        stubFor(
            get(urlEqualTo("/api/data"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\"key\": \"value\"}")
                )
        );

        // Configure the mock URL
        String mockUrl = "http://localhost:" + wireMockServer.port() + "/api/data";

        // Configure the HttpRequesties class
        HttpRequesties httpRequesties = new HttpRequesties();

        // Execute the method under test
        ResponseEntity<Object> actualResponseEntity = httpRequesties.getRequest(mockUrl, "mockToken");

        // Verify that the result of the method matches the expected result
        assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
    }
}
