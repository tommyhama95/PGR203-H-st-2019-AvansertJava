package no.kristiania.dao;
/*
import no.kristiania.dao.daos.ProjectDao;
import no.kristiania.dao.daos.ProjectMemberDao;
import no.kristiania.dao.daos.UserDao;
import no.kristiania.dao.objects.Project;
import no.kristiania.dao.objects.ProjectMember;
import no.kristiania.dao.objects.User;
import no.kristiania.http.HttpClient;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectMembersControllerTest {
    @Test
    void shouldReturnProjectsFromDatabase() throws SQLException, IOException {
        DaoTest daoTestInstance = new DaoTest();
        JdbcDataSource dataSource = daoTestInstance.createDataSource();
        ProjectMemberDao pmDao = new ProjectMemberDao(dataSource);
        ProjectDao pDao = new ProjectDao(dataSource);
        UserDao uDao = new UserDao(dataSource);


        daoTestInstance.setUp();

        ProjectsController pController = new ProjectsController(pDao);
        Project project1 = daoTestInstance.sampleProject();

        UsersController uController = new UsersController(uDao);
        User user1 = daoTestInstance.sampleUser();
        User user2 = daoTestInstance.sampleUser();

        ProjectMembersController pmController = new ProjectMembersController(pmDao, uDao);
        long projectId = pDao.insert(project1);
        ProjectMember member1 = new ProjectMember(projectId, uDao.insert(user1));
        ProjectMember member2 = new ProjectMember(projectId, uDao.insert(user2));

        HttpClient client = new HttpClient("localhost", 8080, "/project.html?id=1");
        client.executeRequest();

        assertThat(client.getBody())
                .contains(String.format("<li id='%s'>%s</li>", member1.getProjectID() +
                        "-" + member1.getUserID(), uDao.getUserById(member1.getUserID()).getName()))
                .contains(String.format("<li id='%s'>%s</li>", member2.getProjectID() +
                        "-" + member2.getUserID(), uDao.getUserById(member2.getUserID()).getName()));
    }
}
*/