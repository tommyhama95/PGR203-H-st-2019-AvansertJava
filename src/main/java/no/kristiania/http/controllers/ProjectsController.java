package no.kristiania.http.controllers;

import no.kristiania.dao.daos.ProjectDao;
import no.kristiania.dao.objects.Project;
import no.kristiania.dao.objects.User;
import no.kristiania.http.HttpMessage;
import no.kristiania.http.HttpStatusCodes;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectsController extends AbstractDaoController {
    private final ProjectDao projectDao;

    public ProjectsController(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    @Override
    public void handle(String requestAction, String requestTarget, Map<String, String> query, String body, OutputStream out) throws IOException {
        try {
            if(requestAction.equals("POST")){
                query = HttpMessage.parseQueryString(body);
                Project project = new Project();
                project.setName(URLDecoder.decode(query.get("projectName"), StandardCharsets.UTF_8));
                project.setId(projectDao.insert(project));
            }
            int status = Integer.parseInt(query.getOrDefault("status","302"));
            String contentType = query.getOrDefault("content-type","text/html");
            String responseBody = query.getOrDefault("body", getBody());
            int contentLength = responseBody.length();
            out.write(("HTTP/1.1 " + status + " " + HttpStatusCodes.statusCodeList.getOrDefault(status,"OK\r\n")).getBytes());
            if(status == 302){
                out.write(("Location: http://localhost:8080/index.html\r\n").getBytes());
            }
            out.write(("Content-type: " + contentType + "\r\n").getBytes());
            out.write(("Content-length: " + contentLength + "\r\n").getBytes());
            out.write(("Connection: close\r\n").getBytes());
            out.write(("\r\n").getBytes());
            out.write((responseBody).getBytes());
        } catch (SQLException e) {
            super.serverErrorResponse(out, e);
        }
    }

    public String getBody() throws SQLException {
        return projectDao.listAll().stream()
                .map(p -> String.format("<li id='%s'><a href=project.html?projectid=%s>%s</a></li>", p.getId(), p.getId(), p.getName()))
                .collect(Collectors.joining(""));
    }
}
