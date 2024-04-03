package com.nhnacademy.http.util;

import com.nhnacademy.http.context.ApplicationContext;
import com.nhnacademy.http.context.Context;
import com.nhnacademy.http.context.ContextHolder;

import java.util.Objects;

public class HttpJobIdGenerator {

    private final static String KEY_JOB_ID = "http-job-id";

    private HttpJobIdGenerator(){

    }

    public synchronized static long getId(){
        Context context = ContextHolder.getApplicationContext();
        Long jobId = (Long) context.getAttribute(KEY_JOB_ID);
        return jobId.longValue();
    }

    private synchronized static void setId(long jobId){
        Context context = ContextHolder.getApplicationContext();
        context.setAttribute(KEY_JOB_ID,jobId);
    }

    public synchronized static long increaseAndGetId(){
        long jobId = getId();
        jobId++;
        setId(jobId);
        return  jobId;
    }

    public synchronized static void initilize(){
        Context context = ContextHolder.getApplicationContext();
        context.setAttribute(KEY_JOB_ID,Long.valueOf(0l));
    }
}
