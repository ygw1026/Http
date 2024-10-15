package com.nhnacademy.http.service;

import java.io.PrintWriter;

import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.util.ResponseUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class MethodNotAllowedService implements HttpService{
    
    @Override
    public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
        String responseBody = null;

        try{
            responseBody = ResponseUtils.tryGetBodyFromFile(ResponseUtils.DEFAULT_405);
        }catch(Exception e) {
            throw new RuntimeException(e);
        }

        String responseHeader = ResponseUtils.createResponseHeader(ResponseUtils.HttpStatus.METHOD_NOT_ALLOWED.getCode(), "UTF-8", responseBody.length(), "");

        try(PrintWriter bufferedWriter = httpResponse.getWriter();){
            bufferedWriter.write(responseHeader);
            bufferedWriter.write(responseBody);
            bufferedWriter.flush();
            log.debug("body:{}", responseBody);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
