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

        StringBuilder stringBuilder = new StringBuilder();

        try(ServerSocket serverSocket = new ServerSocket(8080);){

            for(;;){
                Socket client = serverSocket.accept();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

                    while (true) {
                        String line = bufferedReader.readLine();
                        stringBuilder.append(line);
                        log.debug("line:{}", line);
                        if (Objects.isNull(line) || line.length() == 0) {
                            break;
                        }
                    }

                    StringBuilder responseBody = new StringBuilder();
                    responseBody.append("<html>\n");
                    responseBody.append("<body>\n");
                    responseBody.append("<h1>hello java</h1>");
                    responseBody.append("</body>\n");
                    responseBody.append("</html>");

                    //StringBuilder response = new StringBuilder();

                    String response = "HTTP/1.0 200 OK\n"
                            + "Server: HTTP server/0.1\n"
                            + "Content-type: text/html; charset=UTF-8\n"
                            + "Content-Length:28 \n\n"
                            + "<html><body>OK</body></html>\n";


                    //bufferedWriter.write("");
                    //bufferedWriter.write("Hello world\n");

                    log.debug("response:{}",response);
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
