package com.nhnacademy.http.util;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ResponseUtilsTest {
    
    @Test
    @DisplayName("isExist:/404.html")
    void isExist1() {
        boolean actual = ResponseUtils.isExist(ResponseUtils.DEFAULT_404);
        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("isExist:/")
    void isExist2() {
        boolean actual = ResponseUtils.isExist("/");
        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("isExist:/favicon.ico")
    void esExist3(){
        boolean actual = ResponseUtils.isExist("/favicon.ico");
        Assertions.assertFalse(actual);
    }

    @Test
    @DisplayName("tryGetBodyFromFile : /index.html")
    void tryGetBodyFromFile() throws IOException{
        String actual = ResponseUtils.tryGetBodyFromFile("/index.html");
        Assertions.assertAll(
                ()->{
                    Assertions.assertTrue(actual.contains("<head>"));
                },
                ()->{
                    Assertions.assertTrue(actual.contains("Hello"));
                },
                ()->{
                    Assertions.assertTrue(actual.contains("Java"));
                },
                ()->{
                    Assertions.assertTrue(actual.contains("</html>"));
                }
        );
    }

    @Test
    @DisplayName("createResponseHeader:200")
    void createResponseHeader1() {
        String actual = ResponseUtils.createResponseHeader(ResponseUtils.HttpStatus.OK.getCode(), "utf-8", 100, "");
        log.debug("actual:{}", actual);

        Assertions.assertAll(
                ()->{
                    Assertions.assertTrue(actual.contains(String.valueOf(ResponseUtils.HttpStatus.OK.getCode())));
                },
                ()->{
                    Assertions.assertTrue(actual.contains(ResponseUtils.HttpStatus.getStatusFromCode(200).getDescription()));
                }
        );
    }

    @Test
    @DisplayName("createResponseHeader:404")
    void createResponseHeader2() {
        String actual = ResponseUtils.createResponseHeader(ResponseUtils.HttpStatus.NOT_FOUND.getCode(), "utf-8", 100, "");
        log.debug("actual:{}", actual);

        Assertions.assertAll(
                ()->{
                    Assertions.assertTrue(actual.contains(String.valueOf(ResponseUtils.HttpStatus.NOT_FOUND.getCode())));
                },
                ()->{
                    Assertions.assertTrue(actual.contains(ResponseUtils.HttpStatus.getStatusFromCode(404).getDescription()));
                }
        );
    }
}
