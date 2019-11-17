package no.kristiania.http.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpStatusCodes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;

abstract class AbstractDaoController implements HttpController {
    private String urlQuery;

    @Override
    public void handle(String requestAction, String requestTarget, Map<String, String> query, String body, OutputStream out) throws IOException {
        setUrlQuery(requestTarget.substring(requestTarget.indexOf('?') + 1));
        try {
            if (requestAction.equals("POST")) {
                query = HttpMessage.parseQueryString(HttpMessage.getQueryString(body));
            }
            serverResponse(query, out);
        } catch (SQLException e) {
            serverErrorResponse(out, e);
        }
    }

    protected void serverResponse(Map<String, String> query, OutputStream out) throws SQLException, IOException {
        int status = Integer.parseInt(query.getOrDefault("status", "200"));
        String contentType = query.getOrDefault("content-type", "text/html");
        String responseBody = query.getOrDefault("body", getBody());
        int contentLength = (responseBody.getBytes(StandardCharsets.UTF_8).length); //Handle UTF-8 Encoded characters
        out.write(("HTTP/1.1 " + status + " " + HttpStatusCodes.statusCodeList.getOrDefault(status, "OK\r\n")).getBytes());
        out.write(("Content-Type: " + contentType + "\r\n").getBytes());
        out.write(("Content-Length: " + contentLength + "\r\n").getBytes());
        out.write(("Connection: close\r\n").getBytes());
        out.write(("\r\n").getBytes());
        out.write((URLDecoder.decode(responseBody,StandardCharsets.UTF_8)).getBytes());
    }

    protected void serverRedirectResponse(Map<String, String> query, OutputStream out, String location) throws IOException {
        int status = Integer.parseInt(query.getOrDefault("status", "302"));
        String contentType = query.getOrDefault("content-type", "text/html");
        out.write(("HTTP/1.1 " + status + " " + HttpStatusCodes.statusCodeList.getOrDefault(status, "FOUND\r\n")).getBytes());
        out.write(("Content-Type: " + contentType + "\r\n").getBytes());
        out.write(("Location: " + location + "\r\n").getBytes());
        out.write(("Connection: close\r\n").getBytes());
        out.write(("\r\n").getBytes());
    }

    protected void serverErrorResponse(OutputStream out, SQLException e) throws IOException {
        int statusCode = 500;
        String responseBody = e.toString();
        out.write(("HTTP/1.1 " + statusCode + " " + HttpStatusCodes.statusCodeList.get(statusCode) + "\r\n").getBytes());
        respondWithHeaders(out, responseBody);
        out.write(("\r\n").getBytes());
        out.write((responseBody).getBytes());
    }

    protected void clientErrorResponse(OutputStream out, String responseBody, int statusCode) throws IOException {
        responseBody += ("\nStatus: " + statusCode + " - " + HttpStatusCodes.statusCodeList.get(statusCode));
        out.write(("HTTP/1.1 " + statusCode + " " + HttpStatusCodes.statusCodeList.get(statusCode) + "\r\n").getBytes());
        respondWithHeaders(out, responseBody);
        out.write(("\r\n").getBytes());
        out.write((responseBody).getBytes());
        out.write(("Status: " + statusCode + " - " + HttpStatusCodes.statusCodeList.get(statusCode)).getBytes());
    }

    private void respondWithHeaders(OutputStream out, String responseBody) throws IOException {
        out.write(("Content-Type: text/html\r\n").getBytes());
        out.write(("Content-Length: " + responseBody.length() + "\r\n").getBytes());
        out.write(("Connection: close\r\n").getBytes());
    }

    protected String getUrlQuery() {
        return this.urlQuery;
    }

    protected String checkValue(String value){
        if(value.equals("") || value.equals(" ") || value.equals("\n")){
            value = "[Undefined]";
            return value;
        } else {
            return value;
        }
    }

    protected void setUrlQuery(String urlQuery) {
        this.urlQuery = urlQuery;
    }

    protected abstract String getBody() throws SQLException;

}