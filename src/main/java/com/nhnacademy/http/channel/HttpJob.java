package com.nhnacademy.http.channel;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

import com.nhnacademy.http.context.Context;
import com.nhnacademy.http.context.ContextHolder;
import com.nhnacademy.http.context.exception.ObjectNotFoundException;
import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.request.HttpRequestImpl;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.response.HttpResponseImpl;
import com.nhnacademy.http.service.HttpService;
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
        Context context = ContextHolder.getApplicationContext();

        if(!ResponseUtils.isExist(httpRequest.getRequestURI())){
            httpService = (HttpService) context.getAttribute(ResponseUtils.DEFAULT_404);
        }else {
            try {
                httpService = (HttpService) context.getAttribute(httpRequest.getRequestURI());
            }catch (ObjectNotFoundException e){
                httpService = (HttpService) context.getAttribute(ResponseUtils.DEFAULT_404);
            }
        }

        try{
            httpService.service(httpRequest, httpResponse);
        }catch (RuntimeException e) {
            httpService = (HttpService) context.getAttribute(ResponseUtils.DEFAULT_405);
            httpService.service(httpRequest, httpResponse);
        }

        try{
            httpService.service(httpRequest, httpResponse);
        }catch (RuntimeException e){
            httpService = (HttpService) context.getAttribute(ResponseUtils.DEFAULT_301);
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
