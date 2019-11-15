package no.kristiania.http.controllers;

import no.kristiania.dao.daos.TaskMemberDao;
import no.kristiania.dao.daos.UserDao;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class TaskMemberSelectController extends AbstractDaoController {

    private TaskMemberDao tmDao;
    private UserDao uDao;

    public TaskMemberSelectController(TaskMemberDao tmDao, UserDao uDao) {
        this.tmDao = tmDao;
        this.uDao = uDao;
    }

    public String getBody() throws SQLException {
        String urlQuery = super.getUrlQuery();
        String[] urlQueries = urlQuery.substring(urlQuery.indexOf('?')+1).split("&");
        long taskId = Long.parseLong(urlQueries[1].substring(urlQueries[1].indexOf('=')+1));;
        return tmDao.listMembersOf(taskId).stream()
                .map(tm -> {
                    try {
                        long userId = tm.getUserId();
                        return String.format("<option id=%s>%s#%s</option>", tm.getUserId(), uDao.getUserById(userId).getName(), tm.getUserId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return "Internal Server Error - 500";
                    }
                })
                .collect(Collectors.joining(""));
    }

}
