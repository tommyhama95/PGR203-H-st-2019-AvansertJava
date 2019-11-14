package no.kristiania.http.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface HttpController {
    void handle(String requestTarget, Map<String, String> query, OutputStream out) throws IOException;
}
