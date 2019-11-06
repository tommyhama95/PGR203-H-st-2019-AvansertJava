package no.kristiania.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpServer {
    private final int localport;
    private final ServerSocket serverSocket;


    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.localport = serverSocket.getLocalPort();
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
            System.out.print(line);
            request.append("\r\n");
        }

        System.out.println(request.toString());

        Map<String, String> targetHeaders = parseRequest(request.toString());

        OutputStream out = socket.getOutputStream();
        out.write(("HTTP/1.1 " + targetHeaders.get("status") + " OK\r\n").getBytes());
        out.write("Content-Type: text/html\r\n".getBytes());
        out.write("Content-Length: 4\r\n".getBytes());
        out.write("Connection: close\r\n".getBytes());
        Iterator it = targetHeaders.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry targetPair = (Map.Entry)it.next();
            if(!(targetPair.getKey().equals("status")) && !(targetPair.getKey().equals("body"))) {
                out.write((targetPair.getKey() + ": " + targetPair.getValue() + "\r\n").getBytes());
            }
            it.remove();
        }
        out.write("\r\n".getBytes());
        out.write("None".getBytes());
        out.close();
        out.flush();
    }

    private Map<String, String> parseRequest(String request) {
        String[] requestLines = request.split("\r\n");
        String requestTarget = requestLines[0].split(" ")[1];
        Map<String, String> targetHeaders = new HashMap<>();
        int questionPos = requestTarget.indexOf("?");
        if(questionPos != -1) {
            String[] targets = requestTarget.substring(questionPos+1).trim().split("&");
            for(String target : targets) {
                int equalsPos = target.indexOf("=");
                String targetHeader = target.substring(0, equalsPos).trim();
                String targetValue = target.substring(equalsPos+1).trim();
                targetHeaders.put(targetHeader, targetValue);
            }
        }
        return targetHeaders;
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

    public int getLocalport() {
        return localport;
    }

}
