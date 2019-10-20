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

        ProjectUserDao puDao = new ProjectUserDao(dataSource);
        ProjectDao projectDao = new ProjectDao(dataSource);
        UserDao userDao = new UserDao(dataSource);

        dataSource.setUrl("jdbc:h2:mem:testDataBase;DB_CLOSE_DELAY=-1");

        Flyway.configure().dataSource(dataSource).load().migrate();

        Project pObject = new Project("Coffee");
        long pID = projectDao.insert(pObject);

        User uObject = new User("Tommy", "No@sniff.yes");
        long uID = userDao.insert(uObject);

        Project pObject2 = new Project("No");
        long pID2 = projectDao.insert(pObject2);

        User uObject2 = new User("sdbg", "No@dhafhadsf.yes");
        long uID2 = userDao.insert(uObject2);

        User uObject3 = new User("Krisp", "Soupyes");
        long uID3 = userDao.insert(uObject3);

        ProjectUser pu = new ProjectUser();
        pu.setProjectID(pID);
        pu.setUserID(uID);

        ProjectUser pu2 = new ProjectUser();
        pu2.setProjectID(pID2);
        pu2.setUserID(uID2);

        ProjectUser pu3 = new ProjectUser();
        pu3.setProjectID(pID);
        pu3.setUserID(uID3);

        puDao.insert(pu);
        puDao.insert(pu2);
        puDao.insert(pu3);

        System.out.println(puDao.listMembersOf(1));
        assertThat(puDao.listAll()).contains(pu);

    }

}
