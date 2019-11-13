package no.kristiania.dao;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectDaoTest {

    Random random = new Random();
    private ProjectDao dao;

    static JdbcDataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testDataBase;DB_CLOSE_DELAY=-1");
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();
        return dataSource;
    }


    public Project sampleProject() throws SQLException {
        Project project = new Project();
        project.setName(selectRandom(new String[]{"Sansa","Soup","Lemonmaking","MUFFIN"}));
        project.setId(dao.insert(project));
        return project;
    }

    @BeforeEach
    void setUp(){
        JdbcDataSource dataSource = createDataSource();
        dao = new ProjectDao(dataSource);
    }

    @Test
    void shouldListOutProjects() throws SQLException {
        Project project = sampleProject();
        project.setId(dao.insert(project));

        assertThat(dao.listAll()).contains(project);
    }

    private String selectRandom(String[] alternatives) {
        return alternatives[random.nextInt(alternatives.length)];
    }
}
