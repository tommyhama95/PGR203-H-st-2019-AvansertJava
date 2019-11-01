package no.kristiania.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {

  HttpClient client;
  HttpServer server;
  private int localport;

  @BeforeEach
  void initialize() throws IOException {
    server = startServer();
    localport = server.getLocalport();
  }

  @Test
  void shouldRespondWithStatus200() throws IOException {
    client = new HttpClient("localhost", localport,"/echo?status=200");
    client.executeRequest();
    assertEquals(200,client.getStatusCode());
  }

  //TODO: write test for more server parsing
  //TODO: Take a break, enjoy gaming

  HttpServer startServer() throws IOException {
    HttpServer server = new HttpServer(0);
    new Thread(() -> {
      try{
        server.start();
      } catch (IOException e){
        e.printStackTrace();
      }
    }).start();
    return server;
  }

}
