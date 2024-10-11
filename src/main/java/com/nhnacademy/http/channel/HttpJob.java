package com.nhnacademy.http.channel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpJob implements Executable {
    private final Socket client;
    private static final String CRLF = "\r\n";

    public HttpJob(Socket client){
        if(Objects.isNull(client)){
            throw new IllegalArgumentException("client Socket is null");
        }
        this.client = client;
    }

    public Socket getClient(){
        return client;
    }

    @Override
    public void execute(){
        StringBuilder requestBuilder = new StringBuilder();
        try(
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        ){
            while(true) {
                String line = br.readLine();
                requestBuilder.append(line);
                log.debug("line:{}", line);
                if(Objects.isNull(line) || line.length() == 0) {
                    break;
                }
            }

            StringBuilder responseBody = new StringBuilder();
            responseBody.append("<html>");
                responseBody.append("<body>");
                    responseBody.append(String.format("<h1>{%s}hello java</h1>", Thread.currentThread().getName()));
                responseBody.append("</body>");
            responseBody.append("</html>");

            StringBuilder responseHeader = new StringBuilder();

            responseHeader.append(String.format("HTTP/1.0 200 OK%s", CRLF));
            responseHeader.append(String.format("Server: HTTP server/0.1%s", CRLF));
            responseHeader.append(String.format("Content-type: text/html; charset=%s%s", "UTF-8", CRLF));
            responseHeader.append(String.format("Connection: Closed%s", CRLF));
            responseHeader.append(String.format("Content-Length:%d %s%s", responseBody.length(), CRLF, CRLF));

            bw.write(responseHeader.toString());
            bw.write(responseBody.toString());
            bw.flush();
            client.close();
        }catch(IOException e){
            log.error("server error:{}", e);
        }finally {
            try{
                client.close();
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
