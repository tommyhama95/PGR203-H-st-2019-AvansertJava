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
    private final String fileLocation;


    private HttpController defaultController = new  HttpController(){
        @Override
        public void handle(String requestTarget, Map<String, String> query, OutputStream out) throws IOException {
            respondToFileRequest(requestTarget, out);
        }
    };

    private Map<String, HttpController> controllers = new HashMap<>();


    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.localport = serverSocket.getLocalPort();
        this.fileLocation = "src/main/resources";

        controllers.put("/echo", new HttpController() {
            @Override
            public void handle(String requestTarget, Map<String, String> query, OutputStream out) throws IOException {
                respondToEchoRequest(query, out);
            }
        });
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
            Map<String, String> query = parseEchoRequest(requestTarget);
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

  private Map<String, String> parseEchoRequest(String requestTarget) {
    Map<String, String> targetHeaders = new HashMap<>();
    int questionPos = requestTarget.indexOf("?");
    if(questionPos != -1) {
      String[] targets = requestTarget.substring(questionPos+1).trim().split("&");
      for(String target : targets) {
        int equalsPos = target.indexOf("=");
        String targetHeader = target.substring(0, equalsPos).trim();
        String targetValue = target.substring(equalsPos + 1).trim();
        targetHeaders.put(targetHeader, targetValue);
      }
    }
    return targetHeaders;
  }

    private void respondToEchoRequest(Map<String, String> targetHeaders, OutputStream out) throws IOException {
        int statusCode = Integer.parseInt(targetHeaders.getOrDefault("status","200"));
        String body = targetHeaders.getOrDefault("body", "None");
        System.out.println("HTTP/1.1 " + statusCode + " " + HttpStatusCodes.statusCodeList.get(statusCode) + "\r\n");
        out.write(("HTTP/1.1 " + statusCode + " " + HttpStatusCodes.statusCodeList.get(statusCode) + "\r\n").getBytes());
        out.write(("Content-Type: text/html\r\n").getBytes());
        out.write(("Content-Length: " + body.length() + "\r\n").getBytes());
        out.write(("Connection: close\r\n").getBytes());
        Iterator it = targetHeaders.entrySet().iterator();
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
    }

    private void respondToFileRequest(String requestTarget, OutputStream out) throws IOException {
        try {
            File file = new File(fileLocation + requestTarget);
            if (!(file.exists())) {
                out.write(("HTTP/1.1 404 " + HttpStatusCodes.statusCodeList.get(404) + "\r\n").getBytes());
                out.write(("\r\n").getBytes());
                out.write(("404 - Not Found").getBytes());
            } else {
                String fileExtension = requestTarget.substring(requestTarget.lastIndexOf('.')).trim();
                String contentType = MimeTypes.mimeTypeList.get(fileExtension);

                out.write(("HTTP/1.1 200 OK\r\n").getBytes());
                out.write(("Content-Type: " + contentType + "\r\n").getBytes());
                out.write(("Content-Length: " + file.length() + "\r\n").getBytes());
                out.write(("Connection: close\r\n").getBytes());
                out.write(("\r\n").getBytes());

                new FileInputStream(file).transferTo(out);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();
    }

    public int getLocalport() {
        return localport;
    }

}
