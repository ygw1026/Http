package com.nhnacademy.http.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class  ResponseUtils {
    public static final String DEFAULT_404 = "/404.html";
    public static final String DEFAULT_405 = "/405.html";
    public static final String DEFAULT_301 = "/301.html";
    private static final String CRLF="\r\n";
    private ResponseUtils(){}

    public enum HttpStatus{
        OK(200, "OK"),
        NOT_FOUND(404, "Not Found"),
        UNKNOWN(-1, "Unknown Status"),
        METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
        MOVED_PERMANENTLY(301, "Moved Permanenetly");

        private final int code;
        private final String description;

        HttpStatus(int code, String description){
            this.code = code;
            this.description = description;
        }

        public int getCode(){
            return code;
        }

        public String getDescription(){
            return description;
        }

        public static HttpStatus getStatusFromCode(int code){
            for(HttpStatus status : HttpStatus.values()) {
                if(status.getCode() == code){
                    return status;
                }
            }
            return UNKNOWN;
        }
    }

    public static boolean isExist(String filePath){
        if(filePath.equals("/")){
            return false;
        }
        URL url = ResponseUtils.class.getResource(filePath);
        return Objects.nonNull(url);
    }

    public static String tryGetBodyFromFile(String filePath) throws IOException{
        StringBuilder responseBody = new StringBuilder();
        try(InputStream inputStream = ResponseUtils.class.getResourceAsStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))){
                while(true){
                    String line = reader.readLine();
                    if(Objects.isNull(line)){
                        break;
                    }
                    responseBody.append(line);
                }
            }
            return responseBody.toString();
    }

    public static String createResponseHeader(int httpStatusCode, String charset, int contentLength, String location){
        StringBuilder responseHeader = new StringBuilder();
        if(httpStatusCode == 301){
            responseHeader.append(String.format("HTTP/1.1 %d %s%s", httpStatusCode, HttpStatus.getStatusFromCode(httpStatusCode).getDescription(), CRLF));
            responseHeader.append(String.format("Location : %s%s", location, CRLF));
            responseHeader.append(String.format("Content-type: text/html; charset=%s%s", charset, CRLF));
            responseHeader.append(String.format("Connection: closed%s", CRLF));
            responseHeader.append(String.format("Content-Length:%d %s%s", contentLength, System.lineSeparator(), CRLF));
            return responseHeader.toString();
        }
        responseHeader.append(String.format("HTTP/1.1 %d %s%s", httpStatusCode, HttpStatus.getStatusFromCode(httpStatusCode).getDescription(), CRLF));
        responseHeader.append(String.format("Location : %s%s", location, CRLF));
        responseHeader.append(String.format("Server: HTTP server/1.1%s", CRLF));
        responseHeader.append(String.format("Content-type: text/html; charset=%s%s", charset, CRLF));
        responseHeader.append(String.format("Connection: closed%s", CRLF));
        responseHeader.append(String.format("Content-Length:%d %s%s", contentLength, System.lineSeparator(), CRLF));
        return responseHeader.toString();
    }
}
