package no.kristiania.dao;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectDaoTest {

    @Test
    void shouldListOutProjects() throws SQLException {
        JdbcDataSource datasource = new JdbcDataSource();
        datasource.setUrl("jdbc:h2:mem:testDataBase");
        datasource.getConnection().createStatement().executeUpdate(
          "CREATE TABLE projects (name VARCHAR(300), project_id SERIAL NOT NULL PRIMARY KEY)"
        );

        Project project = new Project();
        project.setName("JDBC");
        ProjectDao dao = new ProjectDao(datasource);
        dao.insert(project);
        assertThat(dao.listAll()).contains(project);

    }
}
