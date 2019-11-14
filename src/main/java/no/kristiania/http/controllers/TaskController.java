package no.kristiania.http.controllers;

import no.kristiania.dao.daos.TaskDao;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class TaskController extends AbstractDaoController {
    private final TaskDao tDao;

    public TaskController(TaskDao tDao) {
        this.tDao = tDao;
    }


    public String getBody() throws SQLException {
        String urlQuery = super.getUrlQuery();
        System.out.println(urlQuery);
        long projectId = Long.parseLong(urlQuery.substring(urlQuery.indexOf('=')+1));
        return tDao.listTasksOfProject(projectId).stream()
                .map(t -> String.format("<li id='%s'><a href='task.html?projectid=%s&taskid=%s'>%s</a></li>", t.getId(), t.getProjectID(), t.getId(), t.getName()))
                .collect(Collectors.joining(""));
    }
}
