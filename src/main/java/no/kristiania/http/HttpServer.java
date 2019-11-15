package no.kristiania.http;

import no.kristiania.http.controllers.EchoHttpController;
import no.kristiania.http.controllers.FileHttpController;
import no.kristiania.http.controllers.HttpController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    private final int localport;
    private final ServerSocket serverSocket;
    private String fileLocation;
    private HttpController defaultController = new FileHttpController(this);
    private Map<String, HttpController> controllers = new HashMap<>();

    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.localport = serverSocket.getLocalPort();
        this.fileLocation = "src/main/resources";

        controllers.put("/echo", new EchoHttpController());
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(8080);
        server.start();
    }

    public void start() throws IOException {
        run();
        logger.info("Started Server on http://localhost:{}",getLocalPort());
    }

    private void run() throws IOException {
        while(true) {
            Socket socket = serverSocket.accept();
            String requestLine = HttpMessage.readLine(socket);
            logger.debug("Handling Client Request: {}", requestLine);
            HttpRequest httpRequest = new HttpRequest(socket);
            httpRequest.parse();
            String body = httpRequest.getBody();

            String requestTarget = requestLine.split(" ")[1];
            String requestAction = requestLine.split(" ")[0];
            int questionPos = requestTarget.indexOf('?');
            String requestPath = questionPos == -1 ? requestTarget : requestTarget.substring(0, questionPos);

            Map<String, String> query = HttpMessage.getQueryParameters(requestTarget);
            if(requestTarget.length() > 1) {
                controllers
                        .getOrDefault(requestPath, defaultController)
                        .handle(requestAction, requestTarget, query, body, socket.getOutputStream());
            } else {
                defaultController.handle(requestAction, "/index.html", query, body, socket.getOutputStream());
            }
        }
    }


    public int getLocalPort() {
        return localport;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public void addController(String requestPath, HttpController controller) {
        controllers.put(requestPath, controller);
    }
}
