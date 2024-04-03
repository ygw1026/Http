package com.nhnacademy.http;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

@Slf4j
public class SimpleHttpServer {

    private final int port;
    private static final int DEFAULT_PORT=8080;

    public SimpleHttpServer(){
        this(DEFAULT_PORT);
    }
    public SimpleHttpServer(int port) {
        if(port<=0){
            throw new IllegalArgumentException(String.format("port range check:%d",port));
        }
        this.port = port;
    }

    public void start() throws IOException {

        StringBuilder requestBuilder = new StringBuilder();

        try(ServerSocket serverSocket = new ServerSocket(8080);){

            for(;;){
                //todo client 연결됨
                Socket client = serverSocket.accept();
                //todo 입축겨을 위해서  Reader, Writer를 선언
                try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                    ) {

                        while (true) {
                            String line = bufferedReader.readLine();
                            //todo StringBuilder에 append
                            requestBuilder.append(line);
                            log.debug("line:{}", line);
                            if (Objects.isNull(line) || line.length() == 0) {
                                //todo 종료 조건 null or size==0
                                break;
                            }
                        }
                        //todo RequestBuilder에 append된 데이터를 parcing 하여 HttpRequest가 동작할 수 있도록 구현합니다.

                        StringBuilder responseBody = new StringBuilder();
                        responseBody.append("<html>");
                        responseBody.append("<body>");
                        responseBody.append("<h1>hello java</h1>");
                        responseBody.append("</body>");
                        responseBody.append("</html>");

                        StringBuilder responseHeader = new StringBuilder();

                        responseHeader.append(String.format("HTTP/1.0 200 OK%s",System.lineSeparator()));
                        responseHeader.append(String.format("Server: HTTP server/0.1%s",System.lineSeparator()));
                        responseHeader.append(String.format("Content-type: text/html; charset=%s%s","UTF-8",System.lineSeparator()));
                        responseHeader.append(String.format("Connection: Closed%s",System.lineSeparator()));
                        responseHeader.append(String.format("Content-Length:%d %s%s",responseBody.length(),System.lineSeparator(),System.lineSeparator()));

                        bufferedWriter.write(responseHeader.toString());
                        bufferedWriter.write(responseBody.toString());
                        bufferedWriter.flush();
                        client.close();
                }catch (IOException e){
                    if (Objects.nonNull(client)){
                        client.close();;
                    }
                }
            }

        }
    }
}
