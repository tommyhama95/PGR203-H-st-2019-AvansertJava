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
                body = URLDecoder.decode(body, StandardCharsets.UTF_8);
                query = HttpMessage.parseQueryString(body);
                User user = new User();
                user.setName(query.get("userName"));
                user.setEmail(query.get("userEmail"));
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
                .map(u -> String.format("<li id='%s'>%s #%s <br> E-Mail: %s</li><br>", u.getId(), u.getName(), u.getId(), u.getEmail()))
                .collect(Collectors.joining(""));
    }

}
