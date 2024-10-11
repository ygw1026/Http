package com.nhnacademy.http.response;

import java.io.IOException;
import java.io.PrintWriter;

public interface HttpResponse {
    PrintWriter getWriter() throws IOException;

    void setCharacterEncoding(String charset);

    String getCharacterEncoding();
}
