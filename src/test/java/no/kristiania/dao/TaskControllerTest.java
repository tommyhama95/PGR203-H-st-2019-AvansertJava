package no.kristiania.dao;

import no.kristiania.dao.daos.TaskDao;
import no.kristiania.dao.objects.Task;
import no.kristiania.http.controllers.TaskController;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskControllerTest {
    @Test
    void shouldReturnTasksFromDatabase() throws SQLException {
        DaoTest daoTestInstance = new DaoTest();
        TaskDao dao = new TaskDao(new DaoTest().createDataSource());

        daoTestInstance.setUp();

        long projectId = daoTestInstance.sampleProject().getId();

        TaskController taskController = new TaskController(dao);
        Task task1 = daoTestInstance.sampleTask(projectId);
        Task task2 = daoTestInstance.sampleTask(projectId);

        taskController.setUrlQuery("projectid=1");

        assertThat(taskController.getBody())
                .contains(String.format("<li id='%s'><a href='task.html?projectid=%s&taskid=%s'>%s</a></li>", task1.getId(), task1.getProjectId(), task1.getId(), task1.getName()))
                .contains(String.format("<li id='%s'><a href='task.html?projectid=%s&taskid=%s'>%s</a></li>", task2.getId(), task2.getProjectId(), task2.getId(), task2.getName()));
    }
}
