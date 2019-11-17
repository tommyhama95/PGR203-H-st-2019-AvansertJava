package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public abstract class HttpMessage {

    public static String getQueryString(String requestTarget) {
        int questionPos = requestTarget.indexOf('?');
        if(questionPos != -1) {
            return requestTarget.substring(questionPos+1);
        }
        //Defaults to returning just the requestTarget
        return requestTarget;
    }

    //Returns a map of all headers and values of a queryString. Headers are keys
    public static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> parameters = new HashMap<>();
        String[] properties = queryString.split("&");
        for(String property : properties) {
            int equalsPos = property.indexOf('=');
            if(equalsPos != -1){
                String header = property.substring(0, equalsPos).trim();
                String value = property.substring(equalsPos + 1).trim();
                parameters.put(header, value);
            }
        }
        return parameters;
    }

    //Reads the stream line-by-line
    protected String read(Socket socket) throws IOException {
        StringBuilder message = new StringBuilder();
        String line;
        while(!(line = readLine(socket)).isEmpty()){
            message.append(line);
            message.append("\r\n");
        }
        return message.toString();
    }

    //Reads one line of the stream
    static String readLine(Socket socket) throws IOException {
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

    //Reads body based on content length
    static String readBody(Map<String, String> headers, Socket socket) throws IOException {
        if(headers.containsKey("Content-Length")) {
            StringBuilder body = new StringBuilder();
            for(int i = 0; i < Integer.parseInt(headers.get("Content-Length")); i++) {
                body.append((char)socket.getInputStream().read());
            }
            return body.toString();
        } else {
            return null;
        }
    }

}
