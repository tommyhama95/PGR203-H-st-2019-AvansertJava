package no.kristiania.dao;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectUserDaoTest {

    @Test
    void shouldListProjectUsers() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();

        ProjectMemberDao puDao = new ProjectMemberDao(dataSource);
        ProjectDao projectDao = new ProjectDao(dataSource);
        UserDao userDao = new UserDao(dataSource);

        dataSource.setUrl("jdbc:h2:mem:testDataBase;DB_CLOSE_DELAY=-1");

        Flyway.configure().dataSource(dataSource).load().migrate();

        Project pObject = new Project("Coffee");
        long pID = projectDao.insert(pObject);

        User uObject = new User("Tommy", "No@sniff.yes");
        long uID = userDao.insert(uObject);


        ProjectMember pu = new ProjectMember();
        pu.setProjectID(pID);
        pu.setUserID(uID);

        puDao.insert(pu);

        assertThat(puDao.listAll()).contains(pu);

    }

}
