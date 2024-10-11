package com.nhnacademy.http;

import java.util.Objects;

import com.nhnacademy.http.channel.RequestChannel;

public class WorkerThreadPool {
    private final int poolSize;

    private final static int DEFAULT_POOLPSIZE=5;

    private final Thread[] workerThreads;
    private final RequestChannel requestChannel;

    public WorkerThreadPool(RequestChannel requestChannel){
        this(DEFAULT_POOLPSIZE, requestChannel);
    }

    public WorkerThreadPool(int poolSize, RequestChannel requestChannel){
        if(poolSize <1){
            throw new IllegalArgumentException("poolSize: > 0");
        }
        if(Objects.isNull(requestChannel)){
            throw new IllegalArgumentException("requestChannel is null");
        }

        this.poolSize = poolSize;
        this.requestChannel = requestChannel;

        HttpRequestHandler httpRequestHandler = new HttpRequestHandler(requestChannel);

        workerThreads = new Thread[poolSize];

        for(int i=0; i<poolSize; i++){
            workerThreads[i] = new Thread(httpRequestHandler);
            workerThreads[i].setName(String.format("thread-%d",i));
        }
    }

    public synchronized void start(){
        for(Thread thread : workerThreads){
            thread.start();
        }
    }

    public synchronized void stop(){
        for(Thread thread : workerThreads){
            if(Objects.nonNull(thread) && thread.isAlive()){
                thread.interrupt();
            }
        }

        for(Thread thread : workerThreads){
            try {
                thread.join();
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
