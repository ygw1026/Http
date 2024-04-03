package com.nhnacademy.http.context;

public class ContextHolder {
    private static final Context context = new ApplicationContext();
    private ContextHolder(){}
    public static synchronized ApplicationContext getApplicationContext() {
        return (ApplicationContext) context;
    }
}
