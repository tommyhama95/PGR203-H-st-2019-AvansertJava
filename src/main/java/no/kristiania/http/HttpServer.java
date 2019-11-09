package no.kristiania.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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


    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.localport = serverSocket.getLocalPort();
        this.fileLocation = "src/main/resources";
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
        Socket socket = serverSocket.accept();

        StringBuilder request = new StringBuilder();
        String line;
        while(!(line = readLine(socket)).isEmpty()){
            request.append(line);
            request.append("\r\n");
        }

        String[] requestLines = request.toString().split("\r\n");
        String requestTarget = requestLines[0].split(" ")[1];

        if(requestTarget.substring(0, 6).equals("/echo?")) {
            Map<String, String> targetHeaders = parseEchoRequest(requestTarget);
            OutputStream out = socket.getOutputStream();
            respondToEchoRequest(targetHeaders, out);
        } else {
            File file = new File(fileLocation + requestTarget);
            OutputStream out = socket.getOutputStream();
            out.write(("HTTP/1.1 200 OK\r\n").getBytes());
            out.write(("Content-Type: text/plain\r\n").getBytes());
            out.write(("Content-Length: " + file.length() + "\r\n").getBytes());
            out.write(("Connection: close\r\n").getBytes());
            out.write(("\r\n").getBytes());

            new FileInputStream(file).transferTo(out);
            out.flush();
            out.close();
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
        out.close();
        out.flush();
    }

    public int getLocalport() {
        return localport;
    }

}