package no.kristiania.http.controllers;

import no.kristiania.dao.daos.ProjectDao;
import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectNameController extends AbstractDaoController{

    private final ProjectDao projectDao;

    public ProjectNameController(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    @Override
    public void handle(String requestAction, String requestTarget, Map<String, String> query, String body, OutputStream out) throws IOException {
        setUrlQuery(HttpMessage.getQueryString(requestTarget));
        try{
            if(requestAction.equals("POST")){
                query = HttpMessage.parseQueryString(body);
                setUrlQuery(HttpMessage.getQueryString(body));
                String name = query.get("projectName");
                name = checkValue(name);
                String projectId = query.get("projectid");
                projectDao.updateProjectName(name, Long.parseLong(projectId));
                serverRedirectResponse(query, out,
                        "http://localhost:8080/project.html?projectid=" + projectId);
                return;
            }
            serverResponse(query, out);
        } catch (SQLException e) {
            serverErrorResponse(out, e);
        }
    }


    public String getBody() throws SQLException {
     String urlQuery = super.getUrlQuery();
     long projectId = Long.parseLong(urlQuery.substring(urlQuery.indexOf('=')+1));
     return projectDao.getProjectFromId(projectId).stream()
             .map(p -> String.format("<h3><a id=%s href='setProjectName.html?projectid=%s'>%s</a></h3>", p.getId(), p.getId(), p.getName()))
             .collect(Collectors.joining(""));
    }

}

