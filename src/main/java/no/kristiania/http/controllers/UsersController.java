package no.kristiania.http.controllers;

import no.kristiania.dao.daos.UserDao;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class UsersController extends AbstractDaoController {

    private final UserDao uDao;

    public UsersController(UserDao uDao) {
        this.uDao = uDao;
    }

    public String getBody() throws SQLException {
        return uDao.listAll().stream()
                .map(u -> String.format("<li id='%s'>%s</li>", u.getId(), u.getName()))
                .collect(Collectors.joining(""));
    }

}
