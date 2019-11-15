package no.kristiania.dao;

import no.kristiania.dao.daos.ProjectMemberDao;
import no.kristiania.dao.daos.UserDao;
import no.kristiania.dao.objects.Project;
import no.kristiania.dao.objects.ProjectMember;
import no.kristiania.dao.objects.User;
import no.kristiania.http.controllers.ProjectMembersController;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectMembersControllerTest {
    @Test
    void shouldReturnProjectMembersFromDatabase() throws SQLException{
        DaoTest daoTestInstance = new DaoTest();
        JdbcDataSource dataSource = daoTestInstance.createDataSource();
        ProjectMemberDao pmDao = new ProjectMemberDao(dataSource);
        UserDao uDao = new UserDao(dataSource);


        daoTestInstance.setUp();

        Project project1 = daoTestInstance.sampleProject();

        User user1 = daoTestInstance.sampleUser();

        long projectId = project1.getId();
        ProjectMember member1 = new ProjectMember(projectId, user1.getId());

        pmDao.insert(member1);

        ProjectMembersController pmController = new ProjectMembersController(pmDao, uDao);
        pmController.setUrlQuery("projectid=1");

        assertThat(pmController.getBody())
                .contains(String.format("<li id='%s'><a href=filterOn.html?projectid=%s&userid=%s>%s</a></li>", member1.getProjectId() +
                        "-" + member1.getUserId(),projectId, member1.getUserId(), uDao.getUserById(member1.getUserId()).getName()));
    }
}
