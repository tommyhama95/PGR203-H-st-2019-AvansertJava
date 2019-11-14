package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse extends  HttpMessage{
    private String message;
    private String httpMethod;
    private String requestTarget;
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private int statusCode;

    public  HttpResponse(Socket socket) throws IOException {
        this.message = read(socket);
        parse();
    }

    @Override
    protected String read(Socket socket) throws IOException {
        StringBuilder response =  new StringBuilder();
        int c;
        while((c = socket.getInputStream().read()) != -1 ) {
            response.append((char)c);
        }
        return  response.toString();
    }

    @Override
    public void parse() {
        String[] messageLines = message.split("\r\n");
        httpMethod = messageLines[0].split(" ")[0];
        statusCode = Integer.parseInt(messageLines[0].split(" ")[1]);
        System.out.println(statusCode);

        int i;
        int colonPos;
        for(i = 1; i < messageLines.length; i++){
            if((colonPos = messageLines[i].indexOf(":")) != -1) {
                String headerName = messageLines[i].substring(0, colonPos);
                String headerValue = messageLines[i].substring(colonPos + 1).trim();
                headers.put(headerName, headerValue);
            } else {
                break;
            }
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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getHeaderValue(String key) {
        return headers.get(key);
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
}
