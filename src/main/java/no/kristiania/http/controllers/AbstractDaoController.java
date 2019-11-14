package no.kristiania.http.controllers;

import no.kristiania.http.HttpStatusCodes;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

abstract class AbstractDaoController implements HttpController {
    private String urlQuery;

    @Override
    public void handle(String requestAction, String requestTarget, Map<String, String> query, String body, OutputStream out) throws IOException {
        urlQuery = requestTarget.substring(requestTarget.indexOf('?')+1);
        try {
            int statusCode = Integer.parseInt(query.getOrDefault("status","200"));
            String responseBody = query.getOrDefault("body", getBody());
            new EchoHttpController().respondWithParams(query, out, statusCode, responseBody);
        } catch (SQLException e) {
            int statusCode = 500;
            String responseBody = e.toString();
            out.write(("HTTP/1.1 " + statusCode + " " + HttpStatusCodes.statusCodeList.get(statusCode) + "\r\n").getBytes());
            out.write(("Content-Type: text/html\r\n").getBytes());
            out.write(("Content-Length: " + responseBody.length() + "\r\n").getBytes());
            out.write(("Connection: close\r\n").getBytes());
            out.write(("\r\n").getBytes());
            out.write((responseBody).getBytes());
        }
    }

    protected String getUrlQuery(){
        return this.urlQuery;
    };

    protected void setUrlQuery(String urlQuery) {
        this.urlQuery = urlQuery;
    }

    protected abstract String getBody() throws SQLException;


}
