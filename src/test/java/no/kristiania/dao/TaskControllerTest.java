package no.kristiania.dao;
/*
import no.kristiania.dao.daos.TaskDao;
import no.kristiania.dao.objects.Task;
import no.kristiania.http.HttpClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskControllerTest {
    @Test
    void shouldReturnTasksFromDatabase() throws SQLException, IOException {
        DaoTest daoTestInstance = new DaoTest();
        TaskDao dao = new TaskDao(new DaoTest().createDataSource());
        daoTestInstance.setUp();

        TaskController controller = new TaskController(dao);
        Task task1 = daoTestInstance.sampleTask();
        Task task2 = daoTestInstance.sampleTask();

        HttpClient client = new HttpClient("localhost", 8080, "/project.html?projectid=1");
        client.executeRequest();

        assertThat(client.getBody())
                .contains(String.format("<li id='%s'><a>%s</a></li>", task1.getId(), task1.getName()))
                .contains(String.format("<li id='%s'><a>%s</a></li>", task2.getId(), task2.getName()));

        //TODO: Give LI tags href attributes for links later
    }
}
*/