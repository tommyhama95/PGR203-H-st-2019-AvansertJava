package no.kristiania.dao;

import no.kristiania.dao.ProjectDao.ProjectDao;
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
          "CREATE TABLE projects (projectName VARCHAR(300))"
        );
        ProjectDao dao = new ProjectDao();
        dao.insertProject("Make Soup");
        assertThat(dao.listProjects("projectName")).contains("Make Soup");

    }
}
