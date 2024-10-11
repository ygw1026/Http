package com.nhnacademy.http;

import java.io.IOException;
import java.util.Objects;

import com.nhnacademy.http.channel.Executable;
import com.nhnacademy.http.channel.RequestChannel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpRequestHandler implements Runnable{
    private final RequestChannel requestChannel;

    public HttpRequestHandler(RequestChannel requestChannel) {
        if(Objects.isNull(requestChannel)){
            throw new IllegalArgumentException("requestChannel is null");
        }
        this.requestChannel = requestChannel;
    }

    @Override
    public void run(){
        while(!Thread.currentThread().isInterrupted()){
            try{
                Executable httpJob = requestChannel.getHttpJob();

                httpJob.execute();

            }catch(IOException e){
                if(e.getMessage().contains(InterruptedException.class.getName())){
                    Thread.currentThread().interrupt();
                }
                log.debug("RequestHandler error : {}", e.getMessage(),e);
            }
        }
    }
}
