package no.kristiania.dao;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectsControllerTest {
    @Test
    void shouldReturnProjectsFromDatabase() throws SQLException {
        ProjectDaoTest daoTestInstance = new ProjectDaoTest();
        ProjectDao dao = new ProjectDao(ProjectDaoTest.createDataSource());
        daoTestInstance.setUp();

        ProjectsController controller = new ProjectsController(dao);
        Project project1 = daoTestInstance.sampleProject();
        Project project2 = daoTestInstance.sampleProject();

        assertThat(controller.getBody())
                .contains(String.format("<li id='%s'><a>%s</a></li>", project1.getId(), project1.getName()))
                .contains(String.format("<li id='%s'><a>%s</a></li>", project2.getId(), project2.getName()));
    }
}