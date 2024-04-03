package com.nhnacademy.http.channel;

import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.response.HttpResponse;
import com.nhnacademy.http.util.HttpJobIdGenerator;

import java.net.Socket;

public class HttpJobRequest implements Job {

    private final long jobId;

    private final HttpRequest httpRequest;
    private final HttpResponse httpResponse;

    private HttpJobRequest(long jobId, HttpRequest httpRequest, HttpResponse httpResponse) {
        this.jobId = jobId;
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
    }

    public static HttpJobRequest createJob(Socket socket){
        try {
            return new HttpJobRequest(HttpJobIdGenerator.increaseAndGetId(), new HttpRequest(socket), new HttpResponse(socket) );
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public long getJobId() {
        return jobId;
    }

    public HttpRequest getHtpRequest() {
        return httpRequest;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }

    @Override
    public void execute() {
        httpRequest.initialize();
    }
}
