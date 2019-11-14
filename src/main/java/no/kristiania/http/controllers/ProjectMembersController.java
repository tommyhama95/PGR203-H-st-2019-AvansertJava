package no.kristiania.http.controllers;

import no.kristiania.dao.daos.ProjectMemberDao;
import no.kristiania.dao.daos.UserDao;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class ProjectMembersController extends AbstractDaoController {
    private ProjectMemberDao pmDao;
    private UserDao uDao;

    public ProjectMembersController(ProjectMemberDao projectMemberDao, UserDao userDao) {
        this.pmDao = projectMemberDao;
        this.uDao = userDao;
    }

    public String getBody() throws SQLException {
        String urlQuery = super.getUrlQuery();
        long projectId = Long.parseLong(urlQuery.substring(urlQuery.indexOf('=')+1));
        return pmDao.listMembersOf(projectId).stream()
                .map(pm -> {
                    try {
                        return String.format("<li id='%s'>%s</li>", pm.getProjectID() +
                                "-" + pm.getUserID(), uDao.getUserById(pm.getUserID()).getName());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return "Internal Server Error - 500";
                    }
                })
                .collect(Collectors.joining(""));
    }
}