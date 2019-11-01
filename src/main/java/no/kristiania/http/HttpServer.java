package no.kristiania.http;

import java.io.IOException;
import java.net.ServerSocket;

public class HttpServer {
  private final int localport;

  public HttpServer(int port) throws IOException {
    ServerSocket serverSocket = new ServerSocket(port);
    this.localport = serverSocket.getLocalPort();
  }

  public int getLocalport() {
    return localport;
  }

  public void start() {
  }
}
