package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends HttpMessage {
    private Socket socket;
    private String httpMethod;
    private String requestTarget;
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private String message;

    public HttpRequest(Socket socket) throws IOException {
        this.socket = socket;
        this.message = super.read(socket);
    }

    @Override
    public void parse() throws IOException {
        String[] messageLines = message.split("\r\n");
        httpMethod = messageLines[0].split(" ")[0];
        requestTarget = messageLines[0].split(" ")[1];

        int i;
        int colonPos;
        for(i = 1; i < messageLines.length; i++){
            colonPos = messageLines[i].indexOf(":");
            String headerName = messageLines[i].substring(0, colonPos);
            String headerValue = messageLines[i].substring(colonPos+1).trim();
            headers.put(headerName, headerValue);
        }

        this.body = HttpMessage.readBody(headers, socket);
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public void setRequestTarget(String requestTarget) {
        this.requestTarget = requestTarget;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
