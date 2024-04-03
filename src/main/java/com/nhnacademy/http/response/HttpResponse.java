package com.nhnacademy.http.response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class HttpResponse implements Response {
    private final Socket socket;
    private final DataOutputStream out;

    public HttpResponse(Socket socket) throws IOException {
        this.socket = socket;
        this.out =  new DataOutputStream (socket.getOutputStream());
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(out);
    }

    @Override
    public void setContentType(String type) {

    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public void setContentLengthLong(long len) {

    }

    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void reset() {

    }

    @Override
    public void flushBuffer() throws IOException {

    }

    public Socket getSocket(){
        return this.socket;
    }
}
