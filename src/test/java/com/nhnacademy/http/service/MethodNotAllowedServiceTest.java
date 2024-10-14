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
class MethodNotAllowedServiceTest {
    HttpService httpService;
    HttpRequest httpRequest;
    HttpResponse httpResponse;
    PrintWriter printWriter;
    StringWriter stringWriter;

    @BeforeEach
    void setUp() throws IOException {
        httpService = new MethodNotAllowedService();

        httpRequest = Mockito.mock(HttpRequestImpl.class);
        Mockito.when(httpRequest.getRequestURI()).thenReturn("/index.html");

        httpResponse = Mockito.mock(HttpResponseImpl.class);

        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        Mockito.when(httpResponse.getWriter()).thenReturn(printWriter);
    }

    @Test
    @DisplayName("doGet : 405 method not allowed")
    void doGet() {
        Mockito.when(httpRequest.getMethod()).thenReturn("GET");

        httpService.service(httpRequest, httpResponse);
        String response = stringWriter.toString();

        log.debug("response:{}", response);

        Assertions.assertAll(
            ()->{
                Assertions.assertTrue(response.contains(String.valueOf(ResponseUtils.HttpStatus.METHOD_NOT_ALLOWED.getCode())));
            },
            ()->{
                Assertions.assertTrue(response.contains(String.valueOf(ResponseUtils.HttpStatus.METHOD_NOT_ALLOWED.getDescription())));
            }
        );
    }
}
