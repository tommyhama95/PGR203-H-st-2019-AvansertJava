package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;

public class HttpClient {
  private String host;
  private int port;
  private String requestTarget;
  private HttpClientRespone clientRespone;


  public HttpClient(String host, int port, String requestTarget) {
    this.host = host;
    this.port = port;
    this.requestTarget = requestTarget;
  }

  public void executeRequest() throws IOException {
    Socket socket = new Socket(host, port);
    socket.getOutputStream().write(("GET " + requestTarget +" HTTP/1.1\r\n").getBytes());
    socket.getOutputStream().write(("Host: " + host +"\r\n").getBytes());
    socket.getOutputStream().write(("Connection: close\r\n").getBytes());
    socket.getOutputStream().write(("\r\n").getBytes());
    socket.getOutputStream().flush();

    StringBuilder response = new StringBuilder();
    int c;
    while((c = socket.getInputStream().read()) != -1){
      response.append((char)c);
    }
    System.out.println(response.toString());

    clientRespone = new HttpClientRespone(response.toString());


  }

  public int getStatusCode() {
    return clientRespone.getStatusCode();
  }
}
