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

import com.nhnacademy.http.channel.HttpJob;
import com.nhnacademy.http.channel.RequestChannel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleHttpServer {
     private final int port;
     private static final int DEFAULT_PORT = 8080;

     private final RequestChannel requestChannel;

     private WorkerThreadPool workerThreadPool;

     public SimpleHttpServer() {
        this(DEFAULT_PORT);
     }

     public SimpleHttpServer(int port) {
        if(port <= 0) {
            throw new IllegalArgumentException(String.format("Invalid Port:%d", port));
        }

        this.port = port;
        requestChannel = new RequestChannel();
        workerThreadPool = new WorkerThreadPool(requestChannel);
     }

     public void start(){
        workerThreadPool.start();

        try(ServerSocket serverSocket = new ServerSocket(this.port);){
            while(true) {
                Socket client = serverSocket.accept();
                requestChannel.addHttpJob(new HttpJob(client));
            }
        }catch (IOException e) {
            log.error("server error:{}", e);
        }
     }
}
