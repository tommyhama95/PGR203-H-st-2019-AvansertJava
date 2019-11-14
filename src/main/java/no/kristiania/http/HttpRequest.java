package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends HttpMessage {
    private String httpMethod;
    private String requestTarget;
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private String message;

    public HttpRequest(Socket socket) throws IOException {
        this.message = super.read(socket);
    }

    @Override
    public void parse() {
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

        StringBuilder bodyContent = new StringBuilder();
        for( i += 1; i < messageLines.length; i++){
            bodyContent.append(messageLines[i]);
            if(i != messageLines.length-1){
                bodyContent.append("\n");
            }
        }
        this.body = bodyContent.toString();
    }

    public static Map<String, String> parseEchoRequest(String requestTarget) {
        Map<String, String> targetHeaders = new HashMap<>();
        int questionPos = requestTarget.indexOf('?');
        if(questionPos != -1) {
            String[] targets = requestTarget.substring(questionPos+1).trim().split("&");
            for(String target : targets) {
                int equalsPos = target.indexOf('=');
                String targetHeader = target.substring(0, equalsPos).trim();
                String targetValue = target.substring(equalsPos + 1).trim();
                targetHeaders.put(targetHeader, targetValue);
            }
        }
        return targetHeaders;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequestTarget() {
        return requestTarget;
    }

    public void setRequestTarget(String requestTarget) {
        this.requestTarget = requestTarget;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
