package no.kristiania.http.controllers;

import no.kristiania.dao.daos.TaskMemberDao;
import no.kristiania.dao.daos.UserDao;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class TaskMembersController extends AbstractDaoController {

    private TaskMemberDao tmDao;
    private UserDao uDao;

    public TaskMembersController(TaskMemberDao tmDao, UserDao uDao) {
        this.uDao = uDao;
        this.tmDao = tmDao;
    }

    public String getBody() throws SQLException {
        String urlQuery = super.getUrlQuery();
        String[] urlQueries = urlQuery.substring(urlQuery.indexOf('?')+1).split("&");
        long projectId = Long.parseLong(urlQueries[0].substring(urlQueries[0].indexOf('=')+1)); //TODO: Maybe use later for visuals
        long taskId = Long.parseLong(urlQueries[1].substring(urlQueries[1].indexOf('=')+1));;
        return tmDao.listMembersOfTask(taskId).stream()
                .map(tm -> {
                    try {
                        long userId = tm.getUserId();
                        return String.format("<li id=%s>%s</li>", tm.getUserId(), uDao.getUserById(userId).getName());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return "Internal Server Error - 500";
                    }
                })
                .collect(Collectors.joining(""));
    }

}
