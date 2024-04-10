package com.nhnacademy;

import com.nhnacademy.http.SimpleHttpServer;
import lombok.extern.slf4j.Slf4j;
import java.io.*;

@Slf4j
public class App 
{
    public static void main( String[] args ) throws IOException {
        //TODO#15 SimpleHttpServer를 시작합니다.
        SimpleHttpServer simpleHttpServer = new SimpleHttpServer(8080);
        simpleHttpServer.start();
    }
}
