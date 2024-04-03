package com.nhnacademy.http.channel;

import com.nhnacademy.http.request.HttpRequest;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestChannel {
    public static final String BEAN_NAME="REQUEST-CHANNEL";
    private static final int DEFAULT_MAX_QUEUE_SIZE=10;
    private final LinkedBlockingQueue<HttpJobRequest> queue;
    private final int maxQueueSize;

    public RequestChannel(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
        this.queue = new LinkedBlockingQueue<>(maxQueueSize);
    }

    public RequestChannel(){
        this(DEFAULT_MAX_QUEUE_SIZE);
    }

    public void addHttpJob(HttpJobRequest httpJobRequest) throws InterruptedException {
        //Queue가 가득차 있다면 요청이 소비될 때까지 대기 합니다.
//        while (this.queue.size()>=maxQueueSize){
//            wait();
//        }

        //queue에 요청을 추가하고 대기하고 있는 스레드를 깨웁니다
        queue.add(httpJobRequest);
        //notifyAll();
    }

    public HttpJobRequest getHttpJob() throws InterruptedException {
        //Queue가 비어있다면 대기 합니다.
//        while(queue.isEmpty()){
//            wait();
//        }

        // queue에서 HttpJobRequest 반환합니다. 대기하고 있는 스레드를 깨웁니다
        HttpJobRequest httpJobRequest = queue.take();

        //    notifyAll();
        return httpJobRequest;
    }

}
