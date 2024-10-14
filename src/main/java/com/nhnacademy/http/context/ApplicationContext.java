package com.nhnacademy.http.context;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.nhnacademy.http.context.exception.ObjectNotFoundException;

public class ApplicationContext implements Context{
    ConcurrentMap<String, Object> objectMap;

    public ApplicationContext() {
        this.objectMap = new ConcurrentHashMap<>();
    }

    @Override
    public void setAttribute(String name, Object object){
        objectNameCheck(name);
        objectValueCheck(object);
        objectMap.put(name,object);
    }

    @Override
    public void removeAttribute(String name){
        objectNameCheck(name);
        objectMap.remove(name);
    }

    @Override
    public Object getAttribute(String name){
        objectNameCheck(name);
        Object object = objectMap.get(name);
        if(Objects.isNull(object)){
            throw new ObjectNotFoundException(name);
        }
        return object;
    }

    private void objectNameCheck(String name){
        if(Objects.isNull(name) || name.length()==0){
            throw new IllegalArgumentException(name);
        }
    }

    private void objectValueCheck(Object o){
        if(Objects.isNull(o)){
            throw new IllegalArgumentException("value is null");
        }
    }
}
