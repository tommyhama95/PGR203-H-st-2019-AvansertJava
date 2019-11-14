package no.kristiania.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class HttpMessage {

    public abstract void parse();

    protected String read(Socket socket) throws IOException {
        StringBuilder message = new StringBuilder();
        String line;
        while(!(line = readLine(socket)).isEmpty()){
            message.append(line);
            message.append("\r\n");
        }
        return message.toString();
    }

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

}
