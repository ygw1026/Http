package com.nhnacademy;

import com.nhnacademy.http.server.HttpServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        HttpServer httpServer = new HttpServer();
        Thread thread = new Thread(httpServer);
        thread.start();

    }
}
