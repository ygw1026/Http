package com.nhnacademy.http.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.request.HttpRequestImpl;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.response.HttpResponseImpl;
import com.nhnacademy.http.util.ResponseUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class IndexHttpServiceTest {
    HttpService httpService;
    HttpRequest httpRequest;
    HttpResponse httpResponse;
    PrintWriter printWriter;
    StringWriter stringWriter;

    @BeforeEach
    void setUp() throws IOException{
        httpService = new IndexHttpService();

        httpRequest = Mockito.mock(HttpRequestImpl.class);
        Mockito.when(httpRequest.getRequestURI()).thenReturn("/index.html");

        httpResponse = Mockito.mock(HttpResponseImpl.class);

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        Mockito.when(httpResponse.getWriter()).thenReturn(printWriter);
    }

    @Test
    @DisplayName("instance of HttpService")
    void constructor(){
        Assertions.assertInstanceOf(HttpService.class, new IndexHttpService());
    }

    @Test
    @DisplayName("doGet")
    void doGet(){
        Mockito.when(httpRequest.getMethod()).thenReturn("GET");

        httpService.service(httpRequest, httpResponse);
        String response = stringWriter.toString();

        log.debug("response:{}", response);
        Assertions.assertAll(
            ()->{
                Assertions.assertTrue(response.contains(String.valueOf(ResponseUtils.HttpStatus.OK.getCode())));
            },
            ()-> {
                Assertions.assertTrue(response.contains(String.valueOf(ResponseUtils.HttpStatus.OK.getDescription())));
            }
        );
    }

    @Test
    @DisplayName("doPost : 405 method not allowed")
    void doPost(){
        Mockito.when(httpRequest.getMethod()).thenReturn("POST");
        Assertions.assertThrows(RuntimeException.class, ()->{
            httpService.service(httpRequest, httpResponse);
        });
    }
}
