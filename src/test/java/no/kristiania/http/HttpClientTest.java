package no.kristiania.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpClientTest {

  @Test
  void shouldReturnStatusCode200() throws IOException {
    HttpClient client = new HttpClient("urlecho.appspot.com",80,"/echo?status=200");
    client.executeRequest();
    assertEquals(200,client.getStatusCode());
  }

  @Test
  void shouldReturnErrorCode404() throws IOException {
    HttpClient client = new HttpClient("urlecho.appspot.com",80,"/echo?status=404");
    client.executeRequest();
    assertEquals(404, client.getStatusCode());

  }
}
