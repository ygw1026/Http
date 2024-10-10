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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleHttpServer {
     private final int port;
     private static final int DEFAULT_PORT = 8080;
     private final ServerSocket serverSocket;

     public SimpleHttpServer() {
        this(DEFAULT_PORT);
     }

     public SimpleHttpServer(int port) {
        if(port <= 0) {
            throw new IllegalArgumentException(String.format("port range check:%d", port));
        }

        this.port = port;

        try {
            serverSocket = new ServerSocket(this.port);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
     }

     public synchronized void start() throws IOException {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                Socket client = serverSocket.accept();

                Thread thread = new Thread(new HttpRequestHandler(client));
                thread.start();
            }
        }catch (Exception e) {
            log.debug("{}", e.getMessage());
        }
     }
}
