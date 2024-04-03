package com.nhnacademy.http.server;

import com.nhnacademy.http.channel.HttpJobRequest;
import com.nhnacademy.http.channel.RequestChannel;
import com.nhnacademy.http.context.Context;
import com.nhnacademy.http.context.ContextHolder;
import com.nhnacademy.http.util.HttpJobIdGenerator;
import com.nhnacademy.http.worker.HttpRequestWorker;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

@Slf4j
public class HttpServer implements Runnable {

    private static final int DEFAULT_PORT=8080;
    private static final int DEFAULT_POOL_SIZE=5;

    private final int port;
    private final int poolSize;
    private final Context context = ContextHolder.getApplicationContext();

    public HttpServer(){
        this(DEFAULT_PORT,DEFAULT_POOL_SIZE);
    }
    public HttpServer(int port,int poolSize) {
        this.port = port;
        this.poolSize = poolSize;

        //context 초기화
        applicationContextInitilize();

        //worker 초기화
        httpRequestWorkerInitilize();
    }

    @Override
    public void run() {

        RequestChannel requestChannel = (RequestChannel) context.getAttribute(RequestChannel.BEAN_NAME);


            try (ServerSocket serverSocket = new ServerSocket(this.port)) {
                while(true) {
                    Socket socket = serverSocket.accept();
                    log.debug("clinet-remote-address:{}", socket.getRemoteSocketAddress());
                    requestChannel.addHttpJob(HttpJobRequest.createJob(socket));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

    }

    private void applicationContextInitilize(){
        //http 요청을 workerThread와 공유할 수 있는 channel 생성
        RequestChannel requestChannel = new RequestChannel(this.poolSize);
        context.setAttribute(RequestChannel.BEAN_NAME, requestChannel);

        //JobIdGenerator intilize, job-id context에서 관리
        HttpJobIdGenerator.initilize();
    }

    private void httpRequestWorkerInitilize(){
        for(int i=0; i<poolSize; i++){
            Thread t = new Thread(new HttpRequestWorker(String.format("worker-%d",i)));
            t.start();
        }
    }

}
