package no.kristiania.http.controllers;

import no.kristiania.dao.daos.UserDao;
import no.kristiania.dao.objects.User;
import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpStatusCodes;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class UsersController extends AbstractDaoController {

    private final UserDao uDao;

    public UsersController(UserDao uDao) {
        this.uDao = uDao;
    }

    @Override
    public void handle(String requestAction, String requestTarget, Map<String, String> query, String body, OutputStream out) throws IOException {
        try {
            if(requestAction.equals("POST")){
                query = HttpMessage.parseQueryString(body);
                User user = new User();
                user.setName(query.get("userName"));
                user.setEmail(query.get("userEmail"));
                user.setId(uDao.insert(user));
            }
            int status = Integer.parseInt(query.getOrDefault("status","302"));
            String contentType = query.getOrDefault("content-type","text/html");
            String responseBody = query.getOrDefault("body", getBody());
            int contentLength = responseBody.length();
            out.write(("HTTP/1.1 " + status + " " + HttpStatusCodes.statusCodeList.getOrDefault(status,"OK\r\n")).getBytes());
            if(status == 302){
                out.write(("Location: http://localhost:8080/index.html\r\n").getBytes());
            }
            out.write(("Content-type: " + contentType + "\r\n").getBytes());
            out.write(("Content-length: " + contentLength + "\r\n").getBytes());
            out.write(("Connection: close\r\n").getBytes());
            out.write(("\r\n").getBytes());
            out.write((responseBody).getBytes());
        } catch (SQLException e) {
            int statusCode = 500;
            String responseBody = e.toString();
            out.write(("HTTP/1.1 " + statusCode + " " + HttpStatusCodes.statusCodeList.get(statusCode) + "\r\n").getBytes());
            out.write(("Content-Type: text/html\r\n").getBytes());
            out.write(("Content-Length: " + responseBody.length() + "\r\n").getBytes());
            out.write(("Connection: close\r\n").getBytes());
            out.write(("\r\n").getBytes());
            out.write((responseBody).getBytes());
        }
    }


    public String getBody() throws SQLException {
        return uDao.listAll().stream()
                .map(u -> String.format("<li id='%s'>%s</li>", u.getId(), u.getName()))
                .collect(Collectors.joining(""));
    }

}
