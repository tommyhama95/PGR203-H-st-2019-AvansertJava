package no.kristiania.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpClient {
    private final String host;
    private final int port;
    private final String requestTarget;
    private HttpResponse clientResponse;
    private String body;
    private final Map<String, String> headers = new HashMap<>();


    public HttpClient(String host, int port, String requestTarget) {
        this.host = host;
        this.port = port;
        this.requestTarget = requestTarget;
        setRequestHeader("Host", host);
        setRequestHeader("Connection", "close");
    }

    //Default to GET methods
    public HttpResponse executeRequest() throws IOException {
        return executeRequest("GET");
    }

    //Option to start a server with a different method (POST)
    public HttpResponse executeRequest(final String httpMethod) throws IOException {
        Socket socket = new Socket(host, port);

        if(body != null){
            setRequestHeader("Content-Length", String.valueOf(body.length()));
        }

        String headerString = headers.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\r\n"));

        //Send the request
        OutputStream out = socket.getOutputStream();
        out.write((httpMethod + " " + requestTarget +" HTTP/1.1\r\n").getBytes());
        out.write((headerString + "\r\n").getBytes());
        out.write(("\r\n").getBytes());
        if(httpMethod.equals("POST")){
            out.write((this.body).getBytes());
        }
        out.flush();

        //Wait for response
        return clientResponse = new HttpResponse(socket);
    }

    //Get from server response
    public int getStatusCode() {
        return clientResponse.getStatusCode();
    }
    public String getResponseHeader(String key) {
        return clientResponse.getHeaderValue(key);
    }
    public String getResponseBody() {
        return clientResponse.getBody();
    }

    //Set client request properties
    public void setRequestHeader(String headerName, String headerValue) {
         this.headers.put(headerName, headerValue);
    }
    public void setBody(String body) {
        this.body = body;
    }
}
