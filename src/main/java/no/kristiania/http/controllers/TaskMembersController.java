package no.kristiania.http.controllers;

import no.kristiania.dao.daos.TaskMemberDao;
import no.kristiania.dao.daos.UserDao;
import no.kristiania.dao.objects.TaskMember;
import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskMembersController extends AbstractDaoController {

    private TaskMemberDao taskMemberDao;
    private UserDao userDao;

    public TaskMembersController(TaskMemberDao taskMemberDao, UserDao userDao) {
        this.userDao = userDao;
        this.taskMemberDao = taskMemberDao;
    }

    @Override
    public void handle(String requestAction, String requestTarget, Map<String, String> query, String body, OutputStream out) throws IOException {
        setUrlQuery(HttpMessage.getQueryString(requestTarget));
        try {
            if(requestAction.equals("POST")){
                body = URLDecoder.decode(body, StandardCharsets.UTF_8);
                query = HttpMessage.parseQueryString(body);
                setUrlQuery(query.get("taskid"));
                String taskId = query.get("taskid");
                String userId = query.get("member");
                TaskMember taskMember =  new TaskMember();
                taskMember.setTaskId(Long.parseLong(taskId));
                taskMember.setUserId(Long.parseLong(userId.substring(userId.indexOf('#')+1)));
                if(taskMemberDao.listMembersOf(taskMember.getUserId()).contains(taskMember)){
                    clientErrorResponse(out, "User is already part of this task!", 409);
                } else {
                    taskMemberDao.insert(taskMember);
                }
            }
            serverDaoResponse(query, out);
        } catch (SQLException e) {
            serverErrorResponse(out,e);
        }
    }

    public String getBody() throws SQLException {
        String urlQuery = super.getUrlQuery();
        String[] urlQueries = urlQuery.substring(urlQuery.indexOf('?')+1).split("&");
        long taskId = Long.parseLong(urlQueries[1].substring(urlQueries[1].indexOf('=')+1));;
        return taskMemberDao.listMembersOf(taskId).stream()
                .map(tm -> {
                    try {
                        long userId = tm.getUserId();
                        return String.format("<li id=%s>%s</li>",
                                tm.getUserId(), userDao.getUserById(userId).getName());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return "Internal Server Error - 500";
                    }
                })
                .collect(Collectors.joining(""));
    }

}
