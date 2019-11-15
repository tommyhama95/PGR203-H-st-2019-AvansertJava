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
            serverDaoResponse(query, out);
        } catch (SQLException e) {
            serverErrorResponse(out,e);
        }
    }


    public String getBody() throws SQLException {
        return uDao.listAll().stream()
                .map(u -> String.format("<li id='%s'>%s</li>", u.getId(), u.getName()))
                .collect(Collectors.joining(""));
    }

}
