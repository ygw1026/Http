/*
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * + Copyright 2024. NHN Academy Corp. All rights reserved.
 * + * While every precaution has been taken in the preparation of this resource,  assumes no
 * + responsibility for errors or omissions, or for damages resulting from the use of the information
 * + contained herein
 * + No part of this resource may be reproduced, stored in a retrieval system, or transmitted, in any
 * + form or by any means, electronic, mechanical, photocopying, recording, or otherwise, without the
 * + prior written permission.
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */

package com.nhnacademy.http;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Slf4j
class SimpleHttpServerTest {
    static Thread thread;

    @BeforeAll
    static void beforeAllSetUp(){
        thread = new Thread(()->{
            SimpleHttpServer simpleHttpServer = new SimpleHttpServer(8080);
            try {
                simpleHttpServer.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }catch (Exception e){
                log.debug("exit!!!");
            }
        });
        thread.start();
    }

    @Test
    @DisplayName("status code : 200 ok")
    void request1() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080"))
                .build();

        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        log.debug("response:{}",response.body());
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("response: hello java")
    void request2() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080"))
                .build();

        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());

        Assertions.assertAll(
                ()->{
                    Assertions.assertTrue(response.body().contains("hello"));
                },
                ()->{
                    Assertions.assertTrue(response.body().contains("java"));
                }
        );
    }

    @Test
    @DisplayName("content-type")
    void request3() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080"))
                .build();

        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        Optional<String> contentTypeOptional = response.headers().firstValue("Content-Type");
        String actual = contentTypeOptional.get().toLowerCase();
        log.debug("contentType:{}",actual);

        Assertions.assertTrue(actual.contains("text/html"));

    }

    @Test
    @DisplayName("charset utf-8")
    void request4() throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080"))
                .build();

        HttpResponse<String> response = httpClient.send(request,HttpResponse.BodyHandlers.ofString());
        Optional<String> contentTypeOptional = response.headers().firstValue("Content-Type");
        String actual = contentTypeOptional.get().toLowerCase();
        log.debug("contentType:{}",actual);

        Assertions.assertTrue(actual.contains("utf-8"));

    }

    @AfterAll
    static void tearDown() throws InterruptedException {
       Thread.sleep(1000);
    }

}