package no.kristiania.http.controllers;

import no.kristiania.dao.daos.UserDao;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class UsersController extends AbstractDaoController {

    private final UserDao dao;

    public UsersController(UserDao dao) {
        this.dao = dao;
    }

    public String getBody() throws SQLException {
        return dao.listAll().stream()
                .map(u -> String.format("<li id='%s'><a>%s</a></li>", u.getId(), u.getName()))
                .collect(Collectors.joining(""));
    }

}
