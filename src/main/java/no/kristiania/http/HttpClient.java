package no.kristiania.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {
    private String host;
    private int port;
    private String requestTarget;
    private HttpResponse clientResponse;
    private String body;
    private Map<String, String> headers = new HashMap<>();


    public HttpClient(String host, int port, String requestTarget) {
        this.host = host;
        this.port = port;
        this.requestTarget = requestTarget;
    }

    public HttpResponse executeRequest() throws IOException {
        Socket socket = new Socket(host, port);
        OutputStream out = socket.getOutputStream();
        out.write(("GET " + requestTarget +" HTTP/1.1\r\n").getBytes());
        out.write(("Host: " + host +"\r\n").getBytes());
        out.write(("Connection: close\r\n").getBytes());
        out.write(("\r\n").getBytes());
        out.flush();

        return clientResponse = new HttpResponse(socket);
    }

    public int getStatusCode() {
        return clientResponse.getStatusCode();
    }

    public String getResponseHeader(String key) {
        return clientResponse.getHeaderValue(key);
    }

    public String getBody() {
        return clientResponse.getBody();
    }

    public void setRequestHeader(String headerName, String headerValue) {
         this.headers.put(headerName, headerValue);
    }

    public void setBody(String body) {
        this.body = body;
    }
}
