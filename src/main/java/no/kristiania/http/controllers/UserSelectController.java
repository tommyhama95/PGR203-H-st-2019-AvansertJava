package no.kristiania.http.controllers;

import no.kristiania.dao.daos.UserDao;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class UserSelectController extends AbstractDaoController{

    private final UserDao userDao;

    public UserSelectController(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getBody() throws SQLException {
        return userDao.listAll().stream()
                .map(u -> String.format("<option id='%s'>%s</option>", u.getId(), u.getName()))
                .collect(Collectors.joining(""));
    }
}
