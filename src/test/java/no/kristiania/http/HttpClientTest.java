package no.kristiania.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpClientTest {

  @Test
  void shouldReturnStatusCode200(){
    HttpClient client = new HttpClient("urlecho.appspot.com",80,"/echo?status=200");
    client.executeRequest();
    assertEquals(200,client.getStatusCode());
  }

  @Test
  void shouldReturnErrorCode404(){
    HttpClient client = new HttpClient("urlecho.appspot.com",80,"/echo?status=404");
    client.executeRequest();
    assertEquals(404, client.getStatusCode());

  }
}
