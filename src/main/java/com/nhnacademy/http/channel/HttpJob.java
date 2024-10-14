package com.nhnacademy.http.channel;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.request.HttpRequestImpl;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.response.HttpResponseImpl;
import com.nhnacademy.http.service.HttpService;
import com.nhnacademy.http.service.IndexHttpService;
import com.nhnacademy.http.service.InfoHttpService;
import com.nhnacademy.http.service.MethodNotAllowedService;
import com.nhnacademy.http.service.NotFoundHttpService;
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

        HttpService httpService = null;

        if(!ResponseUtils.isExist(httpRequest.getRequestURI())){
            httpService = new NotFoundHttpService();
        }else if(httpRequest.getRequestURI().equals("/index.html")){
            httpService = new IndexHttpService();
        }else if(httpRequest.getRequestURI().equals("/info.html")){
            httpService = new InfoHttpService();
        }else {
            httpService = new NotFoundHttpService();
        }

        try{
            httpService.service(httpRequest, httpResponse);
        }catch (RuntimeException e) {
            httpService = new MethodNotAllowedService();
            httpService.service(httpRequest, httpResponse);
        }

        try {
            if(Objects.nonNull(client) && client.isConnected()) {
                client.close();
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
