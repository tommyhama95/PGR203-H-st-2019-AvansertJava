package no.kristiania.http.controllers;

import no.kristiania.http.HttpServer;
import no.kristiania.http.HttpStatusCodes;
import no.kristiania.http.MimeTypes;

import java.io.*;
import java.util.Map;

public class FileHttpController implements HttpController {
    private HttpServer httpServer;

    public FileHttpController(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    @Override
    public void handle(String requestTarget, Map<String, String> query, OutputStream out) throws IOException {
        try {
            String requestPath = requestTarget;
            int questionPos = requestTarget.indexOf('?');
            if(questionPos != -1){
                requestPath = requestTarget.substring(0,questionPos);
            }
            File file = new File(httpServer.getFileLocation() + requestPath);
            if (file.exists()) {
                String fileExtension = requestPath.substring(requestPath.lastIndexOf('.')).trim();
                String contentType = MimeTypes.mimeTypeList.get(fileExtension);

                out.write(("HTTP/1.1 200 OK\r\n").getBytes());
                out.write(("Content-Type: " + contentType + "\r\n").getBytes());
                out.write(("Content-Length: " + file.length() + "\r\n").getBytes());
                out.write(("Connection: close\r\n").getBytes());
                out.write(("\r\n").getBytes());

                new FileInputStream(file).transferTo(out);
            } else {
                out.write(("HTTP/1.1 404 " + HttpStatusCodes.statusCodeList.get(404) + "\r\n").getBytes());
                out.write(("\r\n").getBytes());
                out.write(("404 - Not Found").getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();
    }
}
