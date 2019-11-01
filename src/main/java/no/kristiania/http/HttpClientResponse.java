package no.kristiania.http;


import java.util.HashMap;
import java.util.Map;

public class HttpClientResponse {


    private Map<String, String> headers = new HashMap<>();
    private final String response;
    private int statusCode;
    private String body;

    public HttpClientResponse(String response) {
        this.response = response;
        parseResponse(response);
    }

    public void parseResponse(String response){
        String[] requestLines = response.split("\r\n");

        //Parse Status Code
        this.statusCode = Integer.parseInt(requestLines[0].split(" ")[1]);

        int i;
        //Parse Response Headers
        int colonPos;
        for(i = 1; (colonPos = requestLines[i].indexOf(":")) != -1; i++) {
            String headerName = requestLines[i].substring(0, colonPos);
            String headerValue = requestLines[i].substring(colonPos + 1).trim();
            headers.put(headerName, headerValue);
        }

        //Parse Response Body
        StringBuilder body = new StringBuilder();
        for(i += 1; i < requestLines.length; i++){
            body.append(requestLines[i]);
            if(i != requestLines.length-1){
                body.append("\n");
            }
        }
        this.body = body.toString();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseHeader(String key) {
        return headers.get(key);
    }

    public String getResponseBody() {
        return body;
    }

    public String getResponse() {
        return response;
    }
}
