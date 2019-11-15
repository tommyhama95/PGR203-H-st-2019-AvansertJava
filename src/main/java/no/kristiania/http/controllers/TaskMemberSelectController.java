package no.kristiania.http.controllers;

import no.kristiania.dao.daos.ProjectMemberDao;
import no.kristiania.dao.daos.TaskMemberDao;
import no.kristiania.dao.daos.UserDao;
import no.kristiania.http.HttpMessage;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskMemberSelectController extends AbstractDaoController {

    private TaskMemberDao tmDao;
    private UserDao uDao;
    private ProjectMemberDao projectMemberDao;

    public TaskMemberSelectController(TaskMemberDao tmDao, UserDao uDao, ProjectMemberDao projectMemberDao) {
        this.tmDao = tmDao;
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
