package no.kristiania.http.controllers;

import no.kristiania.dao.daos.ProjectMemberDao;
import no.kristiania.dao.daos.UserDao;
import no.kristiania.dao.objects.ProjectMember;
import no.kristiania.http.HttpMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectMembersController extends AbstractDaoController {
    private ProjectMemberDao projectMemberDao;
    private UserDao userDao;

    public ProjectMembersController(ProjectMemberDao projectMemberDao, UserDao userDao) {
        this.projectMemberDao = projectMemberDao;
        this.userDao = userDao;
    }

    @Override
    public void handle(String requestAction, String requestTarget, Map<String, String> query, String body, OutputStream out) throws IOException {
        setUrlQuery(HttpMessage.getQueryString(requestTarget));
        try {
            if(requestAction.equals("POST")){
                body = URLDecoder.decode(body, StandardCharsets.UTF_8);
                query = HttpMessage.parseQueryString(body);
                setUrlQuery(query.get("projectid"));

                String projectId = query.get("projectid");
                String userId = query.get("user");
                ProjectMember projectMember = new ProjectMember();
                projectMember.setProjectId(Long.parseLong(query.get("projectid")));
                projectMember.setUserId(Long.parseLong(userId.substring(userId.indexOf('#')+1).trim()));
                if(projectMemberDao.listMembersOf(Long.parseLong(projectId)).contains(projectMember)){
                    clientErrorResponse(out, "User is already part of this project!", 409);
                } else {
                    projectMemberDao.insert(projectMember);
                }
            }
            serverDaoResponse(query, out);
        } catch (SQLException e) {
            serverErrorResponse(out,e);
        }
    }

    public String getBody() throws SQLException {
        String urlQuery = super.getUrlQuery();
        long projectId = Long.parseLong(urlQuery.substring(urlQuery.indexOf('=')+1));
        return projectMemberDao.listMembersOf(projectId).stream()
                .map(pm -> {
                    try {
                        return String.format("<li id='%s'><a href=filterOn.html?projectid=%s&userid=%s>%s</a></li>",
                                pm.getProjectId() + "-" + pm.getUserId(),
                                pm.getProjectId(), pm.getUserId(), userDao.getUserById(pm.getUserId()).getName());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return "Internal Server Error - 500";
                    }
                })
                .collect(Collectors.joining(""));
    }

    public void setUrlQuery(String urlQuery){
        super.setUrlQuery(urlQuery);
    }
}