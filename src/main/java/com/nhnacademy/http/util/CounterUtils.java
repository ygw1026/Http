package com.nhnacademy.http.util;

import com.nhnacademy.http.context.Context;
import com.nhnacademy.http.context.ContextHolder;

public class CounterUtils {
    public final static String CONTEXT_COUNTER_NAME = "Global-Couter";
    private CounterUtils(){}

    public static synchronized long increaseAndGet(){
        Context context = ContextHolder.getApplicationContext();
        long count = (long) context.getAttribute(CONTEXT_COUNTER_NAME);
        context.setAttribute(CONTEXT_COUNTER_NAME, ++count);
        return count;
    }
}
