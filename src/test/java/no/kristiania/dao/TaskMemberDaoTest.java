package no.kristiania.dao;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskMemberDaoTest {

    private ProjectDao pDao;
    private UserDao uDao;
    private ProjectMemberDao pmDao;
    private TaskDao tDao;
    private TaskMemberDao tmDao;

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = ProjectDaoTest.createDataSource();
        pDao = new ProjectDao(dataSource);
        uDao = new UserDao(dataSource);
        pmDao = new ProjectMemberDao(dataSource);
        tDao = new TaskDao(dataSource);
        tmDao = new TaskMemberDao(dataSource);
    }

    @Test
    void shouldListAllTaskMembers() throws SQLException {
        long uID = uDao.insert(new User("Olav", "Something@test.nk"));
        long pID = pDao.insert(new Project("Finish java"));
        ProjectMember pm = new ProjectMember(uID, pID);
        long tID = tDao.insert(new Task("Finish Task", "In progress", pID));
        TaskMember taskMember = new TaskMember(tID, uID);
        tmDao.insert(taskMember);

        assertThat(tmDao.listAll()).contains(taskMember);

    }

}
