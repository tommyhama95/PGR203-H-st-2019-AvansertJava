package no.kristiania.dao;

import no.kristiania.dao.daos.ProjectDao;
import no.kristiania.dao.objects.Project;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectsControllerTest {
    @Test
    void shouldReturnProjectsFromDatabase() throws SQLException {
        DaoTest daoTestInstance = new DaoTest();
        ProjectDao dao = new ProjectDao(new DaoTest().createDataSource());
        daoTestInstance.setUp();

        ProjectsController controller = new ProjectsController(dao);
        Project project1 = daoTestInstance.sampleProject();
        Project project2 = daoTestInstance.sampleProject();

        assertThat(controller.getBody())
                .contains(String.format("<li id='%s'><a href=project.html?id=%s>%s</a></li>", project1.getId(), project1.getId(), project1.getName()))
                .contains(String.format("<li id='%s'><a href=project.html?id=%s>%s</a></li>", project2.getId(), project2.getId(), project2.getName()));

        //TODO: Give LI tags href attributes for links later
    }
}