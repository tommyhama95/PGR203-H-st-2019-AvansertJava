package no.kristiania.http.controllers;

import no.kristiania.dao.daos.TaskDao;

import java.sql.SQLException;
import java.util.stream.Collectors;

public class TaskStatusController extends AbstractDaoController {
    private final TaskDao taskDao;

    public TaskStatusController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public String getBody() throws SQLException {
        String urlQuery = super.getUrlQuery();
        String taskLine = urlQuery.split("&")[1];
        long taskId = Long.parseLong(taskLine.substring(taskLine.indexOf('=')+1));
        return taskDao.getTaskFromId(taskId).stream()
                .map(t -> String.format("<a id='%s' href=setStatus.html?projectId='%s'&taskId='%s>%s</a> <= Click to change", t.getId(), t.getProjectId(), t.getId(), t.getStatus()))
                .collect(Collectors.joining(""));
    }

    public void setUrlQuery(String urlQuery){
        super.setUrlQuery(urlQuery);
    }
}
