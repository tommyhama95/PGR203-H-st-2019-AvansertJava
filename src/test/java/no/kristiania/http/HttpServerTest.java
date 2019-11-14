package no.kristiania.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerTest {
    HttpClient client;
    HttpServer server;
    private int localport;

    @BeforeEach
    void initialize() throws IOException {
        server = startServer();
        localport = server.getLocalPort();
    }

    @Test
    void shouldRespondWithStatus200() throws IOException {
        client = new HttpClient("localhost", localport,"/echo?status=200");
        client.executeRequest("GET");
        assertEquals(200, client.getStatusCode());
    }

    @Test
    void shouldRespondToRedirect() throws IOException {
        client = new HttpClient("localhost", localport, "/echo?status=302&Location=https://www.example.com");
        client.executeRequest("GET");
        assertEquals(302, client.getStatusCode());
        assertEquals("https://www.example.com", client.getResponseHeader("Location"));
    }

    @Test
    void shouldReturnBody() throws  IOException {
        client = new HttpClient("localhost", localport,  "/echo?body=Hello%20World!");
        client.executeRequest("GET");
        assertEquals(200, client.getStatusCode());
        assertEquals("Hello World!", client.getBody());
    }

    @Test
    void shouldReturnFile() throws IOException {
        client = new HttpClient("localhost", localport, "/sample.txt");
        client.executeRequest("POST");
        assertEquals(200, client.getStatusCode());
        assertEquals("Hello World!", client.getBody());
    }

    @Test
    void shouldReturnPostParameter() throws IOException {
        String formBody = "content-type=text/html&body=foobar";
        client = new HttpClient("localhost", localport, "/echo?" + formBody);
        client.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        client.setBody(formBody);
        HttpResponse response = client.executeRequest("POST");
        assertThat(response.getHeaderValue("Content-Type")).isEqualTo("text/html");
        assertThat(response.getBody()).isEqualTo("foobar");
    }

    HttpServer startServer() throws IOException {
        HttpServer server = new HttpServer(0);
        new Thread(() -> {
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return server;
    }

}
