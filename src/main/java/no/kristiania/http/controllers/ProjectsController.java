package no.kristiania.http.controllers;

import no.kristiania.dao.daos.ProjectDao;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class ProjectsController extends AbstractDaoController {
    private final ProjectDao dao;

    public ProjectsController(ProjectDao dao) {
        this.dao = dao;
    }

    public String getBody() throws SQLException {
        return dao.listAll().stream()
                .map(p -> String.format("<li id='%s'><a href=project.html?projectid=%s>%s</a></li>", p.getId(), p.getId(), p.getName()))
                .collect(Collectors.joining(""));
    }
}
