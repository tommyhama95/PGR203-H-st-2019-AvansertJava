package no.kristiania.http;

public class HttpClientRespone {


    private final String response;
    private int statusCode;

    public HttpClientRespone(String response) {
        this.response = response;
        parseResponse(response);
    }

    public void parseResponse(String response){
        String[] requestLines = response.split("\r\n");
        this.statusCode = Integer.parseInt(requestLines[0].split(" ")[1]);

    }

    public int getStatusCode() {
        return statusCode;
    }
}
