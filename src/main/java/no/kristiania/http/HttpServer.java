package no.kristiania.http;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
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

            StringBuilder request = new StringBuilder();
            String line;
            while (!(line = readLine(socket)).isEmpty()) {
                request.append(line);
                request.append("\r\n");
            }

            String[] requestLines = request.toString().split("\r\n");
            String requestTarget = requestLines[0].split(" ")[1];
            int questionPos = requestTarget.indexOf('?');
            String requestPath = questionPos == -1 ? requestTarget : requestTarget.substring(0, questionPos);

            OutputStream out = socket.getOutputStream();
            Map<String, String> query = parseEchoRequest(requestTarget, questionPos);
            if(requestTarget.length() > 1) {
                controllers
                        .getOrDefault(requestPath, defaultController)
                        .handle(requestTarget, query, out);
            }
        }
    }


    private String readLine(Socket socket) throws IOException {
        StringBuilder line = new StringBuilder();
        int c;
        while((c = socket.getInputStream().read()) != -1){
            if((char)c == '\r'){
                c = socket.getInputStream().read();
                if((char)c != '\n'){
                    System.err.println("Unexpected Character!");
                }
                return line.toString();
            } else {
                line.append((char)c);
            }
        }
        return line.toString();
    }

  private Map<String, String> parseEchoRequest(String requestTarget, int questionPos) {
    Map<String, String> targetHeaders = new HashMap<>();
    if(questionPos != -1) {
      String[] targets = requestTarget.substring(questionPos+1).trim().split("&");
      for(String target : targets) {
        int equalsPos = target.indexOf('=');
        String targetHeader = target.substring(0, equalsPos).trim();
        String targetValue = target.substring(equalsPos + 1).trim();
        targetHeaders.put(targetHeader, targetValue);
      }
    }
    return targetHeaders;
  }

    public int getLocalport() {
        return localport;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
}
