package com.nhnacademy.http.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.util.CounterUtils;
import com.nhnacademy.http.util.ResponseUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InfoHttpService implements HttpService{

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponse httpResponse){
        String responseBody = null;

        try{
            responseBody = ResponseUtils.tryGetBodyFromFile(httpRequest.getRequestURI());
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        String id = httpRequest.getParameter("id");
        String name = httpRequest.getParameter("name");
        name = URLDecoder.decode(name, StandardCharsets.UTF_8);
        String age = httpRequest.getParameter("age");

        log.debug("id:{}", id);
        log.debug("name:{}", name);
        log.debug("age:{}", age);

        responseBody = responseBody.replace("${id}", id);
        responseBody = responseBody.replace("${name}", name);
        responseBody = responseBody.replace("${age}", age);

        responseBody = responseBody.replace("${count}", String.valueOf(CounterUtils.increaseAndGet()));

        String responseHeader = ResponseUtils.createResponseHeader(200, "UTF-8", responseBody.getBytes().length);

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
