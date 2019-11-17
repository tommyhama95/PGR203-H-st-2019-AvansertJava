package no.kristiania.dao;

import no.kristiania.dao.daos.ProjectMemberDao;
import no.kristiania.dao.daos.TaskDao;
import no.kristiania.dao.daos.TaskMemberDao;
import no.kristiania.dao.objects.*;
import no.kristiania.http.controllers.TaskController;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskMemberControllerTest {

    @Test
    void shouldReturnTaskMembersFromDatabase() throws SQLException {
        DaoTest daoTestInstance = new DaoTest();
        JdbcDataSource dataSource = daoTestInstance.createDataSource();
        TaskMemberDao taskMemberDao = new TaskMemberDao(dataSource);
        TaskDao taskDao = new TaskDao(dataSource);
        ProjectMemberDao projectMemberDao = new ProjectMemberDao(dataSource);

        daoTestInstance.setUp();

        Project project1 = daoTestInstance.sampleProject();
        Task task1 = daoTestInstance.sampleTask(project1.getId());
        User user1 = daoTestInstance.sampleUser();
        ProjectMember projectMember1 = new ProjectMember(user1.getId(), project1.getId());

        TaskMember taskMember = new TaskMember(task1.getId(), task1.getProjectId(), projectMember1.getProjectId());

        projectMemberDao.insert(projectMember1);
        taskMemberDao.insert(taskMember);

        TaskController taskController = new TaskController(taskDao);
        taskController.setUrlQuery("taskid=1");

        assertThat(taskController.getBody())
            .contains(String.format("<li id='%s'><a href='task.html?projectid=%s&taskid=%s'>%s</a></li>", task1.getId(), task1.getProjectId(), task1.getId(), task1.getName()));
    }

}
