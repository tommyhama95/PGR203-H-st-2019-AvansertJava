package no.kristiania.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpClientTest {
  @Test
  void shouldReturnStatusCode200(){
    HttpClient client = new HttpClient();
    client.executeRequest();
    assertEquals(200,client.getStatusCode());
  }
}
