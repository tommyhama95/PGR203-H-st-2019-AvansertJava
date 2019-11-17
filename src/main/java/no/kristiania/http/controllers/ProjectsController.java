package no.kristiania.http.controllers;

import no.kristiania.dao.daos.ProjectDao;
import no.kristiania.dao.objects.Project;
import no.kristiania.http.HttpMessage;

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
                String name = URLDecoder.decode(query.get("projectName"), StandardCharsets.UTF_8);
                name = checkValue(name);
                project.setName(name);
                project.setId(projectDao.insert(project));
                serverRedirectResponse(query, out, "http://localhost:8080/index.html");
                return;
            }
            serverResponse(query, out);
        } catch (SQLException e) {
            serverErrorResponse(out, e);
        }
    }

    public String getBody() throws SQLException {
        return projectDao.listAll().stream()
                .map(p -> String.format("<li id='%s'><a href=project.html?projectid=%s>%s</a></li>", p.getId(), p.getId(), p.getName()))
                .collect(Collectors.joining(""));
    }
}
