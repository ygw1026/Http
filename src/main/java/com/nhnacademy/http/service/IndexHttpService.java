package com.nhnacademy.http.service;

import java.io.IOException;
import java.io.PrintWriter;

import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.util.CounterUtils;
import com.nhnacademy.http.util.ResponseUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IndexHttpService implements HttpService{
    
    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse){
        String responseBody = null;

        try{
            responseBody = ResponseUtils.tryGetBodyFromFile(httpRequest.getRequestURI());
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        String userId = httpRequest.getParameter("userId");

        log.debug("userId:{}", userId);

        responseBody = responseBody.replace("${userId}", userId);
        responseBody = responseBody.replace("${count}", String.valueOf(CounterUtils.increaseAndGet()));
        
        String responseHeader = ResponseUtils.createResponseHeader(200, "OK", responseBody.getBytes().length, "");

        try(PrintWriter bufferedWriter = httpResponse.getWriter();){
            bufferedWriter.write(responseHeader);
            bufferedWriter.write(responseBody);
            bufferedWriter.write("\n");
            bufferedWriter.flush();
            log.debug("body:{}", responseBody);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
