package no.kristiania.http.controllers;

import no.kristiania.dao.daos.TaskDao;
import no.kristiania.dao.objects.Task;
import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskController extends AbstractDaoController {
    private final TaskDao taskDao;

    public TaskController(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    @Override
    public void handle(String requestAction, String requestTarget, Map<String, String> query, String body, OutputStream out) throws IOException {
        setUrlQuery(HttpRequest.getQueryString(requestTarget));
        try {
            if(requestAction.equals("POST")){
                body = URLDecoder.decode(body,StandardCharsets.UTF_8);
                query = HttpMessage.parseQueryString(body);
                setUrlQuery(query.get("projectid"));
                Task task = new Task();
                task.setName(query.get("taskName"));
                task.setStatus(query.get("taskStatus"));
                task.setProjectId(Long.parseLong(query.get("projectid")));
                task.setId(taskDao.insert(task));
            }
            serverDaoResponse(query, out);
        } catch (SQLException e) {
            serverErrorResponse(out, e);
        }
    }

    public String getBody() throws SQLException {
        String urlQuery = super.getUrlQuery();
        long projectId = Long.parseLong(urlQuery.substring(urlQuery.indexOf('=')+1));
        return taskDao.listTasksOfProject(projectId).stream()
                .map(t -> String.format("<li id='%s'><a href='task.html?projectid=%s&taskid=%s'>%s</a></li>", t.getId(), t.getProjectId(), t.getId(), t.getName()))
                .collect(Collectors.joining(""));
    }

    public void setUrlQuery(String urlQuery){
        super.setUrlQuery(urlQuery);
    }
}
