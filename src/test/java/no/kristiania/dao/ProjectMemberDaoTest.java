package no.kristiania.dao;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectMemberDaoTest {

    private ProjectMemberDao pmDao;
    private ProjectDao projectDao;
    private UserDao userDao;

    @BeforeEach
    void setUp(){
        JdbcDataSource dataSource = ProjectDaoTest.createDataSource();
        pmDao = new ProjectMemberDao(dataSource);
        projectDao = new ProjectDao(dataSource);
        userDao = new UserDao(dataSource);
    }

    @Test
    void shouldListProjectMembers() throws SQLException {
        long pID = projectDao.insert(new Project("Do Thing"));
        long uID = userDao.insert(new User("TestUser", "Test@testing.no"));

        ProjectMember pm = new ProjectMember();
        pm.setProjectID(pID);
        pm.setUserID(uID);

        pmDao.insert(pm);

        assertThat(pmDao.listAll()).contains(pm);

    }

}
