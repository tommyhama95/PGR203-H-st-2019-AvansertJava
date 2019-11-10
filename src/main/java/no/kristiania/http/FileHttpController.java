package no.kristiania.http;

import java.io.*;
import java.util.Map;

class FileHttpController implements HttpController {
    private HttpServer httpServer;

    public FileHttpController(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    @Override
    public void handle(String requestTarget, Map<String, String> query, OutputStream out) throws IOException {
        try {
            File file = new File(httpServer.getFileLocation() + requestTarget);
            if (file.exists()) {
                String fileExtension = requestTarget.substring(requestTarget.lastIndexOf('.')).trim();
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
