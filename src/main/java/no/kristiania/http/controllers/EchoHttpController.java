package no.kristiania.http.controllers;

import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpStatusCodes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

public class EchoHttpController implements HttpController {
    @Override
    public void handle(String requestAction, String requestTarget, Map<String, String> query, String body, OutputStream out) throws IOException {
        if(requestAction.equals("POST")){
            query = HttpMessage.parseQueryString(body);
        }
        int statusCode = Integer.parseInt(query.getOrDefault("status","200"));
        String responseBody = query.getOrDefault("body", "none");
        respondWithParams(query, out, statusCode, responseBody);
    }

    public void respondWithParams(Map<String, String> query, OutputStream out, int statusCode, String body) throws IOException {
        body = URLDecoder.decode(body,StandardCharsets.UTF_8);
        int contentLength = body.getBytes(StandardCharsets.UTF_8).length;
        out.write(("HTTP/1.1 " + statusCode + " " + HttpStatusCodes.statusCodeList.get(statusCode) + "\r\n").getBytes());
        out.write(("Content-Type: text/html; charset=utf-8\r\n").getBytes());
        out.write(("Content-Length: " + contentLength + "\r\n").getBytes());
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
        out.write((body).getBytes());
        out.flush();
        out.close();
    }
}
