package no.kristiania.http.controllers;

import no.kristiania.dao.daos.TaskDao;
import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskNameController extends AbstractDaoController {

    private final TaskDao taskDao;

    public TaskNameController(TaskDao taskDao){
        this.taskDao = taskDao;
    }

    @Override
    public void handle(String requestAction, String requestTarget, Map<String, String> query, String body, OutputStream out) throws IOException {
        setUrlQuery(HttpMessage.getQueryString(requestTarget));
        try{
            if(requestAction.equals("POST")){
                query = HttpMessage.parseQueryString(body);
                setUrlQuery(HttpMessage.getQueryString(body));
                String taskId = query.get("taskid");
                String projectId = query.get("projectid");
                String taskName = URLDecoder.decode(query.get("taskName"), StandardCharsets.UTF_8);
                taskName = checkValue(taskName);
                taskDao.updateTaskName((taskName), Long.parseLong(taskId));
                serverRedirectResponse(query, out,
                    "http://localhost:8080/task.html?projectid=" + projectId + "&taskid=" + taskId);
                    return;
            }
            serverResponse(query, out);
        } catch (SQLException e) {
            serverErrorResponse(out, e);
        }
    }

    public String getBody() throws SQLException {
        String urlQuery = super.getUrlQuery();
        Map<String, String> query = HttpMessage.parseQueryString(urlQuery);
        long taskId = Long.parseLong(query.get("taskid"));
        return taskDao.getTaskFromId(taskId).stream()
                .map(t -> String.format("<h4><a id=%s href='setTaskName.html?projectid=%s&taskid=%s'>%s</a></h4>", t.getId(), t.getProjectId(), t.getId(), t.getName()))
                .collect(Collectors.joining(""));
    }

}
