package com.nhnacademy.http.request;

import java.util.Map;

public interface Request {
    String getMethod();
    String getParameter(String name);
    Map<String, String[]> getParameterMap();
    String getHeader(String name);
    void setAttribute(String name, Object o);
    Object getAttribute(String name);

    /*
    * GET http://foo.bar/a.html HTTP/1.0  return ->  /a.html
    * */
    String getRequestURI();

}
