package no.kristiania.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

    OutputStream out = socket.getOutputStream();
    out.write("HTTP/1.1 200 OK\r\n".getBytes());
    out.write("Content-Type: text/html\r\n".getBytes());
    out.write("Content-Length: 4\r\n".getBytes());
    out.write("Connection: close\r\n".getBytes());
    out.write("\r\n".getBytes());
    out.write("none".getBytes());
    out.close();
    out.flush();
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
