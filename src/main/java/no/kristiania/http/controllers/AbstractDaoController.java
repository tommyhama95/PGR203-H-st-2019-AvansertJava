package no.kristiania.http.controllers;

import no.kristiania.http.HttpStatusCodes;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

abstract class AbstractDaoController implements HttpController {
    private String urlQuery;

    @Override
    public void handle(String requestTarget, Map<String, String> query, OutputStream out) throws IOException {
        urlQuery = requestTarget.substring(requestTarget.indexOf('?')+1);
        try {
            int statusCode = Integer.parseInt(query.getOrDefault("status","200"));
            String body = query.getOrDefault("body", getBody());
            new EchoHttpController().respondWithParams(query, out, statusCode, body);
        } catch (SQLException e) {
            int statusCode = 500;
            String body = e.toString();
            out.write(("HTTP/1.1 " + statusCode + " " + HttpStatusCodes.statusCodeList.get(statusCode) + "\r\n").getBytes());
            out.write(("Content-Type: text/html\r\n").getBytes());
            out.write(("Content-Length: " + body.length() + "\r\n").getBytes());
            out.write(("Connection: close\r\n").getBytes());
            out.write(("\r\n").getBytes());
            out.write((body).getBytes());
        }
    }

    protected String getUrlQuery(){
        return this.urlQuery;
    };

    protected abstract String getBody() throws SQLException;


}
