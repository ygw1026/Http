package com.nhnacademy.http.request;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class HttpRequest implements Request{

    private final Socket socket;
    private final DataInputStream in;

    private final StringBuilder stringBuilder = new StringBuilder();
    private final Map<String,Object> headerMap = new HashMap<>();
    private final Map<String,Object> attributeMap = new HashMap<>();
    private final static String KEY_HTTP_METHOD = "HTTP-METHOD";
    private final static String KEY_QUERY_PARAM_MAP = "HTTP-QUERY-PARAM-MAP";
    private final static String KEY_REQUEST_PATH="HTTP-REQUEST-PATH";
    private final static String HEADER_DELIMER=":";


    public HttpRequest(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        initialize();
    }

    public  void initialize() {
        try {
            appendData();

            String arr[] = stringBuilder.toString().split(System.lineSeparator());

            for (int i=0 ; i<arr.length; i++){
                if(i==0){
                    //method
                    //parameterMap
                    parseHttpRequestInfo(arr[i]);
                }else{
                    String[] hStr = arr[i].split(HEADER_DELIMER);
                    String key = hStr[0].trim();
                    if(key.equals("") ){
                        break;
                    }
                    String value = hStr[1].trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private  void appendData() throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;

        while((line=br.readLine())!=null){
            stringBuilder.append(line);
            System.out.println("line:"+line);
        }

        /*
        String tmp;
        while ((tmp = in.readLine()) != null) {
            stringBuilder.append(tmp);
            System.out.println(tmp);
        }*/

        /*
        BufferedInputStream bis = new BufferedInputStream(in);

        int len = 0;

        while (true) {
            byte[] buf = new byte[1024];
            len = bis.read(buf, 0, buf.length);
            if(len<=0){
                break;
            }
            String s = new String(buf, 0, buf.length);
            String ds = URLDecoder.decode(s, StandardCharsets.UTF_8);
            stringBuilder.append(ds);
            log.debug("len:{}",len);
        }
        bis.close();
         */
        log.debug("input:{}", stringBuilder.toString());
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
    public Map<String, String[]> getParameterMap() {
        return (Map<String, String[]>) headerMap.get(KEY_QUERY_PARAM_MAP);
    }

    @Override
    public String getHeader(String name) {
        return String.valueOf(headerMap.get(name));
    }

    @Override
    public void setAttribute(String name, Object o) {
        attributeMap.put(name,o);
    }

    @Override
    public Object getAttribute(String name) {
        return attributeMap.get(name);
    }

    @Override
    public String getRequestURI() {
        return String.valueOf(headerMap.get(KEY_REQUEST_PATH));
    }

    private void parseHttpRequestInfo(String s) {
        String arr[] = s.split(" ");
        //http method parse
        if (arr.length > 0) {
            headerMap.put(KEY_HTTP_METHOD, s.split(" ")[0]);
        }
        //query parameter parse
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

            if (Objects.nonNull(queryString) && !httpRequestPath.equals(queryString) ) {
                String qarr[] = queryString.split("&");
                for (String q : qarr) {
                    String key = q.split("=")[0];
                    String value = q.split("=")[1];
                    log.debug("key:{},value={}", key, value);
                    queryMap.put(key.trim(), value.trim());
                }
            }

            //path 설정
            headerMap.put(KEY_REQUEST_PATH, httpRequestPath);

            //queryMap 설정
            headerMap.put(KEY_QUERY_PARAM_MAP, queryMap);
        }
    }


}
