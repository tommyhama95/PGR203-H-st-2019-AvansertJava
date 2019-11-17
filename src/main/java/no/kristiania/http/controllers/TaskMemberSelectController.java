package no.kristiania.http.controllers;

import no.kristiania.dao.daos.ProjectMemberDao;
import no.kristiania.dao.daos.UserDao;
import no.kristiania.http.HttpMessage;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskMemberSelectController extends AbstractDaoController {

    private final UserDao uDao;
    private final ProjectMemberDao projectMemberDao;

    public TaskMemberSelectController(UserDao uDao, ProjectMemberDao projectMemberDao) {
        this.uDao = uDao;
        this.projectMemberDao = projectMemberDao;
    }

    public String getBody() throws SQLException {
        String urlQuery = super.getUrlQuery();
        Map<String, String> query = HttpMessage.parseQueryString(urlQuery);
        long projectId = Long.parseLong(query.get("projectid"));
        return projectMemberDao.listMembersOf(projectId).stream()
                .map(pm -> {
                    try {
                        long userId = pm.getUserId();
                        return String.format("<option id=%s>%s#%s</option>", pm.getUserId(), uDao.getUserById(userId).getName(), pm.getUserId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return "Internal Server Error - 500";
                    }
                })
                .collect(Collectors.joining(""));
    }

}
