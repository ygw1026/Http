package com.nhnacademy.http.channel;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Objects;

import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.request.HttpRequestImpl;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.response.HttpResponseImpl;
import com.nhnacademy.http.util.ResponseUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpJob implements Executable {

    private final Socket client;
    private final HttpRequest httpRequest;
    private final HttpResponse httpResponse;

    public HttpJob(Socket client){
        if(Objects.isNull(client)){
            throw new IllegalArgumentException("client Socket is null");
        }
        this.client = client;
        this.httpRequest = new HttpRequestImpl(client);
        this.httpResponse = new HttpResponseImpl(client);
    }

    public HttpRequest getHttpRequest(){
        return httpRequest;
    }

    @Override
    public void execute(){

        log.debug("method:{}", httpRequest.getMethod());
        log.debug("uri:{}", httpRequest.getRequestURI());
        log.debug("clinet-closed:{}", client.isClosed());

        String responseBody = null;
        String responseHeader = null;

        if(!ResponseUtils.isExist(httpRequest.getRequestURI())){
            try {
                responseBody = ResponseUtils.tryGetBodyFromFile(ResponseUtils.DEFAULT_404);
                responseHeader = ResponseUtils.createResponseHeader(404, "utf-8", responseBody.getBytes("utf-8").length);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }else{
            try{
                responseBody = ResponseUtils.tryGetBodyFromFile(httpRequest.getRequestURI());
            }catch (IOException e){
                throw new RuntimeException(e);
            }
            try {
                responseHeader = ResponseUtils.createResponseHeader(200, "UTF-8", responseBody.getBytes("utf-8").length);
            }catch (UnsupportedEncodingException e){
                throw new RuntimeException(e);
            }
        }

        try(BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {
            bufferedWriter.write(responseHeader);
            bufferedWriter.write(responseBody);
            bufferedWriter.flush();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(Objects.nonNull(client) && client.isConnected()){
                    client.close();
                }
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
