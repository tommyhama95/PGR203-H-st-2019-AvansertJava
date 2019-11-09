package no.kristiania.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class HttpClient {
    private String host;
    private int port;
    private String requestTarget;
    private HttpClientResponse clientResponse;


    public HttpClient(String host, int port, String requestTarget) {
        this.host = host;
        this.port = port;
        this.requestTarget = requestTarget;
    }

    public void executeRequest() throws IOException {
        Socket socket = new Socket(host, port);
        OutputStream out = socket.getOutputStream();
        out.write(("GET " + requestTarget +" HTTP/1.1\r\n").getBytes());
        out.write(("Host: " + host +"\r\n").getBytes());
        out.write(("Connection: close\r\n").getBytes());
        out.write(("\r\n").getBytes());
        out.flush();

        StringBuilder response = new StringBuilder();
        int c;
        while((c = socket.getInputStream().read()) != -1){
            System.out.print((char)c);
            response.append((char)c);
        }

        clientResponse = new HttpClientResponse(response.toString());
    }

    public int getStatusCode() {
        return clientResponse.getStatusCode();
    }

    public String getResponseHeader(String key) {
        return clientResponse.getResponseHeader(key);
    }

    public String getBody() {
        return clientResponse.getResponseBody();
    }
}
