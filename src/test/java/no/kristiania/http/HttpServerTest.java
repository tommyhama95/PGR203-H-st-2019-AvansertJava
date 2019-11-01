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
    server = new HttpServer(0);
    server.start();
    localport = server.getLocalport();
  }

  @Test
  void shouldRespondWithStatus200() throws IOException {
    client = new HttpClient("localhost", localport,"/echo?status=200");
    client.executeRequest();
    assertEquals(200,client.getStatusCode());
  }
}
