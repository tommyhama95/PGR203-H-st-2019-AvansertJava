package no.kristiania.http;

import no.kristiania.http.controllers.EchoHttpController;
import no.kristiania.http.controllers.FileHttpController;
import no.kristiania.http.controllers.HttpController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {
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
    }

    private void run() throws IOException {
        System.out.println("Waiting on port: "+ serverSocket.getLocalPort());

        while(true) {
            Socket socket = serverSocket.accept();
            HttpRequest httpRequest = new HttpRequest(socket);
            httpRequest.parse();

            String requestTarget = httpRequest.getRequestTarget();
            int questionPos = requestTarget.indexOf('?');
            String requestPath = questionPos == -1 ? requestTarget : requestTarget.substring(0, questionPos);

            Map<String, String> query = httpRequest.parseEchoRequest(requestTarget, questionPos);
            if(requestTarget.length() > 1) {
                controllers
                        .getOrDefault(requestPath, defaultController)
                        .handle(requestTarget, query, socket.getOutputStream());
            } else {
                defaultController.handle("/index.html", query, socket.getOutputStream());
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
