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
import com.nhnacademy.http.context.Context;
import com.nhnacademy.http.context.ContextHolder;
import com.nhnacademy.http.service.IndexHttpService;
import com.nhnacademy.http.service.InfoHttpService;
import com.nhnacademy.http.service.MethodNotAllowedService;
import com.nhnacademy.http.service.NotFoundHttpService;
import com.nhnacademy.http.util.CounterUtils;

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

        Context context = ContextHolder.getApplicationContext();
        context.setAttribute("/index.html", new IndexHttpService());
        context.setAttribute("/info.html", new InfoHttpService());
        context.setAttribute("/404.html", new NotFoundHttpService());
        context.setAttribute("/405.html", new MethodNotAllowedService());
        context.setAttribute(CounterUtils.CONTEXT_COUNTER_NAME, 0l);
     }

     public void start(){
        workerThreadPool.start();

        //오류 발생시 "this.port" <=> "8080" 으로 변경 필요
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
