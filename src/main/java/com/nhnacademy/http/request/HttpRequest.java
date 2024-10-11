package com.nhnacademy.http.request;

import java.util.Map;

public interface HttpRequest {
    String getMethod();

    String getParameter(String name);

    Map<String, String> getParameterMap();

    String getHeader(String name);

    void setAttribute(String name, Object o);

    Object getAttribute(String name);

    String getRequestURI();
}
