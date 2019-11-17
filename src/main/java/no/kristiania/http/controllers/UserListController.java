package no.kristiania.http.controllers;

import no.kristiania.dao.daos.UserDao;
import no.kristiania.dao.objects.User;
import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class UserListController extends AbstractDaoController {

    private final UserDao userDao;

    public UserListController(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void handle(String requestAction, String requestTarget, Map<String, String> query, String body, OutputStream out) throws IOException {
        try {
            if(requestAction.equals("POST")){
                query = HttpMessage.parseQueryString(body);
                User user = new User();
                String name = URLDecoder.decode(query.get("userName"), StandardCharsets.UTF_8);
                String email = URLDecoder.decode(query.get("userEmail"), StandardCharsets.UTF_8);
                name = checkValue(name);
                email = checkValue(email);
                user.setName(name);
                user.setEmail(email);
                user.setId(this.userDao.insert(user));
                serverRedirectResponse(query, out, "http://localhost:8080/index.html");
                return;
            }
            serverResponse(query, out);
        } catch (SQLException e) {
            serverErrorResponse(out,e);
        }
    }


    public String getBody() throws SQLException {
        return userDao.listAll().stream()
                .map(u -> String.format("<li id='%s'><a href='alterUser.html?userid=%s&userName=%s&userEmail=%s'>%s #%s </a><br> E-Mail: %s</li><br>",
                        u.getId(), u.getId(), u.getName(), u.getEmail(), u.getName(), u.getId(), u.getEmail()))
                .collect(Collectors.joining(""));
    }

}
