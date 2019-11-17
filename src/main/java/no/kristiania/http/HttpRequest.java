package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends HttpMessage {
    private final Socket socket;
    private final Map<String, String> headers = new HashMap<>();
    private String body;
    private String message;

    public HttpRequest(Socket socket) throws IOException {
        this.socket = socket;
        this.message = super.read(socket);
    }

    public void parse() throws IOException {
        String[] messageLines = message.split("\r\n");
        String httpMethod = messageLines[0].split(" ")[0];
        String requestTarget = messageLines[0].split(" ")[1];

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

    public String getBody() {
        return body;
    }

}
