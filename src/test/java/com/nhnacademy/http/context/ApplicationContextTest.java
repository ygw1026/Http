package com.nhnacademy.http.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.nhnacademy.http.service.IndexHttpService;

import lombok.extern.slf4j.Slf4j;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class ApplicationContextTest {
    @BeforeEach
    void setUp(){
        Context context = ContextHolder.getApplicationContext();
        context.setAttribute("indexHttpService", new IndexHttpService());
    }

    @Test
    @Order(7)
    @DisplayName("shared ContextHolder")
    void sharedContextHolder() throws InterruptedException {

        Thread thread1 = new Thread(()->{
            Context context = ContextHolder.getApplicationContext();
            context.setAttribute("counter", 10);
        });

        thread1.start();
        thread1.join();

        Thread thread2 = new Thread(()->{
            Context context = ContextHolder.getApplicationContext();
            int counter = (int) context.getAttribute("counter");
            counter = counter + 1;
            context.setAttribute("counter", counter);
        });

        thread2.start();
        thread2.join();

        Context context = ContextHolder.getApplicationContext();
        Assertions.assertEquals(11, context.getAttribute("counter"));
    }
}
