package no.kristiania.dao;

import no.kristiania.dao.daos.*;
import no.kristiania.http.HttpController;
import no.kristiania.http.HttpStatusCodes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskMembersController implements HttpController {

    private TaskMemberDao tmDao;
    private TaskDao tDao;
    private UserDao uDao;
    private ProjectDao pDao;
    private ProjectMemberDao pmDao;
    private String urlQuery;

    public TaskMembersController(TaskMemberDao tmDao, TaskDao tDao, ProjectMemberDao pmDao, ProjectDao pDao, UserDao uDao) {
        this.tDao = tDao;
        this.pmDao = pmDao;
        this.pDao = pDao;
        this.uDao = uDao;
        this.tmDao = tmDao;
    }

    @Override
    public void handle(String requestTarget, Map<String, String> query, OutputStream out) throws IOException {
        urlQuery = requestTarget.substring(requestTarget.indexOf('?')+1);
        try {
            int statusCode = Integer.parseInt(query.getOrDefault("status","200"));
            String body = query.getOrDefault("body", getBody());
            out.write(("HTTP/1.1 " + statusCode + " " + HttpStatusCodes.statusCodeList.get(statusCode) + "\r\n").getBytes());
            out.write(("Content-Type: text/html\r\n").getBytes());
            out.write(("Content-Length: " + body.length() + "\r\n").getBytes());
            out.write(("Connection: close\r\n").getBytes());
            Iterator it = query.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry targetPair = (Map.Entry)it.next();
                if(!(targetPair.getKey().equals("status")) && !(targetPair.getKey().equals("body"))) {
                    out.write((targetPair.getKey() + ": " + targetPair.getValue() + "\r\n").getBytes());
                }
                it.remove();
            }
            out.write(("\r\n").getBytes());
            out.write((URLDecoder.decode(body, StandardCharsets.UTF_8)).getBytes());
            out.flush();
            out.close();
        } catch (SQLException e) {
            int statusCode = 500;
            String body = e.toString();
            out.write(("HTTP/1.1 " + statusCode + " " + HttpStatusCodes.statusCodeList.get(statusCode) + "\r\n").getBytes());
            out.write(("Content-Type: text/html\r\n").getBytes());
            out.write(("Content-Length: " + body.length() + "\r\n").getBytes());
            out.write(("Connection: close\r\n").getBytes());
            out.write(("\r\n").getBytes());
            out.write((body).getBytes());
        }
    }

    public String getBody() throws SQLException {
        String[] urlQueries = urlQuery.substring(urlQuery.indexOf('?')+1).split("&");
        long projectId = Long.parseLong(urlQueries[0].substring(urlQueries[0].indexOf('=')+1)); //TODO: Maybe use later for visuals
        long taskId = Long.parseLong(urlQueries[1].substring(urlQueries[1].indexOf('=')+1));
        return tmDao.listMembersOfTask(taskId).stream()
                .map(tm -> {
                    try {
                        return String.format("<li id=%s>%s</li>", tm.getuID(), uDao.getUserById(tm.getuID()).getName());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return "Internal Server Error - 500";
                    }
                })
                .collect(Collectors.joining(""));
    }

}
