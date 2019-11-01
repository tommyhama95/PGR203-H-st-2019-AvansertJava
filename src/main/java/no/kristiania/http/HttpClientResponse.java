package no.kristiania.http;


import java.util.HashMap;
import java.util.Map;

public class HttpClientResponse {


    private Map<String, String> headers = new HashMap<>();
    private final String response;
    private int statusCode;

    public HttpClientResponse(String response) {
        this.response = response;
        parseResponse(response);
    }

    public void parseResponse(String response){
        String[] requestLines = response.split("\r\n");
        this.statusCode = Integer.parseInt(requestLines[0].split(" ")[1]);


        for(int i = 1; i < requestLines.length; i++) {
            int colonPos = requestLines[i].indexOf(":");
            if(colonPos  != -1) {
                String headerName = requestLines[i].substring(0, colonPos);
                String headerValue = requestLines[i].substring(colonPos + 1).trim();
                headers.put(headerName, headerValue);
            }
        }

    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseHeader(String key) {
        return headers.get(key);
    }
}
