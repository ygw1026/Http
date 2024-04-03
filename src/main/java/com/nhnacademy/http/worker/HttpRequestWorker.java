package com.nhnacademy.http.worker;

import com.nhnacademy.http.channel.HttpJobRequest;
import com.nhnacademy.http.channel.RequestChannel;
import com.nhnacademy.http.context.Context;
import com.nhnacademy.http.context.ContextHolder;
import com.nhnacademy.http.request.HttpRequest;
import com.nhnacademy.http.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

@Slf4j
public class HttpRequestWorker implements Runnable {
    private final String workerName;
    private final Context context = ContextHolder.getApplicationContext();

    public HttpRequestWorker(String workerName) {
        this.workerName = workerName;
    }

    @Override
    public void run() {
       // while (true){

            RequestChannel requestChannel = (RequestChannel) context.getAttribute(RequestChannel.BEAN_NAME);

            try {

                HttpJobRequest httpJobRequest = requestChannel.getHttpJob();

//
                HttpRequest httpRequest = httpJobRequest.getHtpRequest();
                HttpResponse httpResponse = httpJobRequest.getHttpResponse();

                System.out.println("---execute---");
                log.debug("worker-name:{} - jobId:{}",this.workerName,  httpJobRequest.getJobId());
//                log.debug("method:{}", httpRequest.getMethod());
//                log.debug("id:{}",httpRequest.getParameter("id"));
//                log.debug("name:{}", httpRequest.getParameter("name"));
                log.debug("request-path:{}",httpRequest.getRequestURI());

                PrintWriter printWriter = httpResponse.getWriter();

                SimpleDateFormat format = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:Ss z");
                String res = "HTTP/1.0 200 OK\n"
                        + "Server: HTTP server/0.1\n"
                        + "Date: "+format.format(new java.util.Date())+"\n"
                        + "Content-type: text/html; charset=UTF-8\n"
                        + "Content-Length:28 \n\n"
                        + "<html><body>OK</body></html>";

                printWriter.write(res);
                printWriter.flush();

                httpResponse.getSocket().close();

            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (Exception e){
                e.printStackTrace();
            }

            this.run();
       // }
    }

}
