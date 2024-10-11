package com.nhnacademy.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpRequestHandler implements Runnable{
    private final Queue<Socket> requestQueue;
    private final int MAX_QUEUE_SIZE = 10;

    private static final String CRLF ="\r\n";

    public HttpRequestHandler() {
        requestQueue = new LinkedList<>();
    }

    public synchronized void addRequest(Socket client) {
        while(requestQueue.size()>=MAX_QUEUE_SIZE){
            try {
                wait();
            }catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        requestQueue.add(client);
        notifyAll();
    }

    public synchronized Socket getRequest() {
        while(requestQueue.isEmpty()){
            try{
                wait();
            }catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }
        notifyAll();
        return requestQueue.poll();
    }

    @Override
    public void run() {

        Socket client = getRequest();

        StringBuilder requestBuilder = new StringBuilder();
        try(
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        ){

            while(true){
                String line = br.readLine();
                requestBuilder.append(line);
                log.debug("line:{}", line);
                if(Objects.isNull(line) || line.length() == 0) {
                    break;
                }
            }

            StringBuilder responseBody = new StringBuilder();
            responseBody.append("<html/>");
            responseBody.append("<body>");
            responseBody.append("<h1>hello java</h1>");
            responseBody.append("</body>");
            responseBody.append("</html>");

            StringBuilder responseHeader = new StringBuilder();
            responseHeader.append(String.format("HTTP/1.1 200 OK%s", CRLF));
            responseHeader.append(String.format("Server: HTTP server/0.1%s", CRLF));
            responseHeader.append(String.format("Content-type: text/html; charset=%s%s", "UTF-8", CRLF));
            responseHeader.append(String.format("Connection: Closed%s", CRLF));
            responseHeader.append(String.format("Content-Length:%d %s%s",responseBody.length(), CRLF, CRLF));

            bw.write(responseHeader.toString());
            bw.write(responseBody.toString());
            bw.flush();
            client.close();
        }catch (IOException e) {
            log.error("server error:{}", e);
        }finally{
            if(Objects.nonNull(client)) {
                try {
                    client.close();
                }catch(IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        run();
    }
}
