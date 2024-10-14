package com.nhnacademy.http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpRequestImpl implements HttpRequest {
    private final Socket client;

    private final static String HEADER_DELIMER = ":";
    private final Map<String,Object> headerMap = new HashMap<>();
    private final static String KEY_REQUEST_PATH = "HTTP-REQUEST-PATH";
    private final static String KEY_QUERY_PARAM_MAP = "HTTP-QUERY-PARAM-MAP";
    private final static String KEY_HTTP_METHOD = "HTTP-METHOD";
    private final Map<String,Object> attributeMap = new HashMap<>();

    public HttpRequestImpl(Socket socket){
        this.client = socket;
        initialize();
    }

    private void initialize() {
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while(true){
                String line = bufferedReader.readLine();
                if(Objects.isNull(line)){
                    break;
                }

                log.debug("line:{}", line);

                if(isFirstLine(line)){
                    parseHttpRequestInfo(line);
                }else if(isEndLine(line)){
                    break;
                }else{
                    parseHeader(line);
                }
            }
        }catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Object getAttribute(String name) {
        return attributeMap.get(name);
    }

    @Override
    public String getHeader(String name) {
        return String.valueOf(headerMap.get(name));
    }

    @Override
    public String getMethod() {
        return String.valueOf(headerMap.get(KEY_HTTP_METHOD));
    }

    @Override
    public String getParameter(String name) {
        return String.valueOf(getParameterMap().get(name));
    }

    @Override
    public Map<String, String> getParameterMap() {
        return (Map<String, String>) headerMap.get(KEY_QUERY_PARAM_MAP);
    }

    @Override
    public String getRequestURI() {
        return String.valueOf(headerMap.get(KEY_REQUEST_PATH));
    }

    @Override
    public void setAttribute(String name, Object o) {
        attributeMap.put(name, o);
    }

    private boolean isFirstLine(String line){
        if(Objects.isNull(line)){
            return false;
        }
        if(line.toUpperCase().indexOf("GET") > -1 || line.toUpperCase().indexOf("POST") > -1){
            return true;
        }
        return false;
    }

    private boolean isEndLine(String s) {
        return Objects.isNull(s) || s.equals("") ? true : false;
    }

    private void parseHeader(String s){
        String[] hStr = s.split(HEADER_DELIMER);
        String key = hStr[0].trim();
        String value = hStr[1].trim();

        if(Objects.nonNull(key) && key.length()>0) {
            headerMap.put(key, value);
        }
    }

    private void parseHttpRequestInfo(String s){
        String arr[] = s.split(" ");

        if(arr.length > 0){
            headerMap.put(KEY_HTTP_METHOD, s.split(" ")[0]);
        }
        if (arr.length > 2) {
            Map<String, String> queryMap = new HashMap<>();
            int questionIndex = arr[1].indexOf("?");
            String httpRequestPath;

            if(questionIndex>0){
                httpRequestPath = arr[1].substring(0, questionIndex);
            }else{
                httpRequestPath = arr[1];
            }

            String queryString = arr[1].substring(questionIndex + 1, arr[1].length());

            if(Objects.nonNull(queryString) && !httpRequestPath.equals(queryString)){
                String qarr[] = queryString.split("&");
                for(String q : qarr){
                    String key = q.split("=")[0];
                    String value = q.split("=")[1];
                    log.debug("key:{}, value={}", key, value);
                    queryMap.put(key.trim(), value.trim());
                }
            }
            headerMap.put(KEY_REQUEST_PATH, httpRequestPath);
            headerMap.put(KEY_QUERY_PARAM_MAP, queryMap);
        }
    }
}
