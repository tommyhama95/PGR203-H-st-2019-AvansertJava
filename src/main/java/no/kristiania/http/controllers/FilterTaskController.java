package no.kristiania.http.controllers;

import no.kristiania.dao.daos.TaskDao;
import no.kristiania.dao.daos.TaskMemberDao;
import no.kristiania.http.HttpMessage;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterTaskController extends AbstractDaoController{

    private final TaskMemberDao taskMemberDao;
    private final TaskDao taskDao;

    public FilterTaskController(TaskMemberDao taskMemberDao, TaskDao taskDao) {

        this.taskMemberDao = taskMemberDao;
        this.taskDao = taskDao;
    }

    public String getBody() throws SQLException {
        String urlQuery = super.getUrlQuery();
        Map<String,String> query = HttpMessage.parseQueryString(urlQuery);
        long userId = Long.parseLong(query.get("userid"));
        long projectId = Long.parseLong(query.get("projectid"));
        return taskMemberDao.listTasksOnMemberInProject(userId, projectId).stream()
                .map(tm -> {
                    try {
                        long taskId = tm.getTaskId();
                        return String.format("<li id=%s><a href=task.html?projectid=%s&taskid=%s>%s</a></li>",
                                tm.getTaskId(), tm.getProjectId(), tm.getTaskId(),
                                taskDao.getTaskFromId(taskId).get(0).getName());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return "Internal Server Error - 500";
                    }
                })
                .collect(Collectors.joining(""));
    }
}
