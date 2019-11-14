package no.kristiania.http.controllers;

import no.kristiania.http.HttpStatusCodes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

abstract class AbstractDaoController implements HttpController {
    private String urlQuery;

    @Override
    public void handle(String requestTarget, Map<String, String> query, OutputStream out) throws IOException {
        urlQuery = requestTarget.substring(requestTarget.indexOf('?')+1);
        try {
            int statusCode = Integer.parseInt(query.getOrDefault("status","200"));
            String body = query.getOrDefault("body", getBody());
            out.write(("HTTP/1.1 " + statusCode + " " + HttpStatusCodes.statusCodeList.get(statusCode) + "\r\n").getBytes());
            out.write(("Content-Type: text/html\r\n").getBytes());
            out.write(("Content-Length: " + body.length() + "\r\n").getBytes());
            out.write(("Connection: close\r\n").getBytes());
            Iterator it = query.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry targetPair = (Map.Entry)it.next();
                if(!(targetPair.getKey().equals("status")) && !(targetPair.getKey().equals("body"))) {
                    out.write((targetPair.getKey() + ": " + targetPair.getValue() + "\r\n").getBytes());
                }
                it.remove();
            }
            out.write(("\r\n").getBytes());
            out.write((URLDecoder.decode(body, StandardCharsets.UTF_8)).getBytes());
            out.flush();
            out.close();
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
