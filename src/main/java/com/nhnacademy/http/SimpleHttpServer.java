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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleHttpServer {
     private final int port;
     private static final int DEFAULT_PORT = 8080;

     private final AtomicLong atomicCounter;

     public SimpleHttpServer() {
        this(DEFAULT_PORT);
     }

     public SimpleHttpServer(int port) {
        if(port <= 0) {
            throw new IllegalArgumentException(String.format("Invalid Port:%d", port));
        }

        this.port = port;
        atomicCounter = new AtomicLong();
     }

     public void start(){
        try(ServerSocket serverSocket = new ServerSocket(port);){

            HttpRequestHandler httpRequestHandlerA = new HttpRequestHandler();
            HttpRequestHandler httpRequestHandlerB = new HttpRequestHandler();

            Thread threadA = new Thread(httpRequestHandlerA);
            threadA.setName("threadA");
            threadA.start();
            Thread threadB = new Thread(httpRequestHandlerB);
            threadB.setName("threadB");
            threadB.start();

            while(true){
                Socket client = serverSocket.accept();
                long count = atomicCounter.incrementAndGet();
                log.debug("count:{}", atomicCounter);
                
                if(count%2==0){
                    httpRequestHandlerA.addRequest(client);
                }else{
                    httpRequestHandlerB.addRequest(client);
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
     }
}
