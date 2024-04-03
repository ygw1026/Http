package com.nhnacademy.http.response;

import java.io.IOException;
import java.io.PrintWriter;

public interface Response {
    public PrintWriter getWriter() throws IOException;

    public void setContentType(String type);

    public String getContentType();

    //len a long specifying the length of the content being returned to the client;
    public void setContentLengthLong(long len);

    public void setCharacterEncoding(String charset);

    public String getCharacterEncoding();

    public void reset();

    public void flushBuffer() throws IOException;

}
