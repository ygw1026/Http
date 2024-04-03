package com.nhnacademy.http.context;

public interface Context {
    void setAttribute(String name, Object object);
    void removeAttribute(String name);
    Object getAttribute(String name);
}