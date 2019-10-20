package no.kristiania.dao;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectDaoTest {

    @Test
    void shouldListOutProjects() throws SQLException {
        JdbcDataSource datasource = new JdbcDataSource();
        datasource.setUrl("jdbc:h2:mem:testDataBase;DB_CLOSE_DELAY=-1");

        Flyway.configure().dataSource(datasource).load().migrate();

        Project project = new Project();
        project.setName("JDBC");
        ProjectDao dao = new ProjectDao(datasource);
        project.setId(dao.insert(project));
        assertThat(dao.listAll()).contains(project);

    }
}
