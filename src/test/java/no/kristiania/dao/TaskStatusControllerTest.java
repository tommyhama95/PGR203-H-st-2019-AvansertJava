package no.kristiania.dao;

import no.kristiania.dao.daos.ProjectDao;
import no.kristiania.dao.daos.TaskDao;
import no.kristiania.dao.objects.Project;
import no.kristiania.dao.objects.Task;
import no.kristiania.http.controllers.TaskController;
import no.kristiania.http.controllers.TaskStatusController;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskStatusControllerTest {

    @Test
    void shouldReturnChangedTaskStatus() throws SQLException {
        DaoTest daoTestInstance = new DaoTest();
        JdbcDataSource dataSource = daoTestInstance.createDataSource();
        TaskDao taskDao = new TaskDao(dataSource);

        daoTestInstance.setUp();

        Project project = daoTestInstance.sampleProject();
        Task task = daoTestInstance.sampleTask(project.getId());

        taskDao.insert(task);

        TaskStatusController tsController = new TaskStatusController(taskDao);
        tsController.setUrlQuery("taskStatus=NewStatus& taskid=1");

        assertThat(tsController.getBody())
                .contains(String.format("<a id='%s' href=setStatus.html?projectid=%s&taskid=%s>%s</a> <= Click to change", task.getId(), task.getProjectId(), task.getId(), task.getStatus()));

    }

}
