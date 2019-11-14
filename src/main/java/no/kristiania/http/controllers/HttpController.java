package no.kristiania.http.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface HttpController {
    void handle(String requestAction, String requestTarget, Map<String, String> query, String body, OutputStream out) throws IOException;
}
