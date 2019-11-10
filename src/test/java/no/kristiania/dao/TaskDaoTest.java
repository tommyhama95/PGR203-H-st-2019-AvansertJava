package no.kristiania.dao;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static no.kristiania.dao.ProjectDaoTest.createDataSource;
import static org.assertj.core.api.Assertions.assertThat;

public class TaskDaoTest {
    Random random = new Random();
    private TaskDao dao;
    private ProjectDao projectDao;

    @BeforeEach
    void setUp(){
        JdbcDataSource dataSource = createDataSource();
        dao = new TaskDao(dataSource);
        projectDao = new ProjectDao(dataSource);
    }

    @Test
    void shouldListOutTasks() throws SQLException {
        Task task = sampleTask();
        task.setId(dao.insert(task));
        assertThat(dao.listAll()).contains(task);
    }

    public Task sampleTask() throws SQLException {
        Task task = new Task();
        task.setName(selectRandom(new String[]{}));
        task.setStatus(selectRandom(new String[]{}));
        task.setProjectID(projectDao.insert(new Project("Sample")));
        return task;
    }

    private String selectRandom(String[] alternatives) {
        return alternatives[random.nextInt(alternatives.length)];
    }
}
