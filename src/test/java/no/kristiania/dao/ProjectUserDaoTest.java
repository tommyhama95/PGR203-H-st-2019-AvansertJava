package no.kristiania.dao;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectUserDaoTest {

    @Test
    void shouldListProjectUsers() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testDataBase");
        dataSource.getConnection().createStatement().executeUpdate(
                "CREATE TABLE projects (name varchar(150), id serial not null primary key)"
        );
        dataSource.getConnection().createStatement().executeUpdate(
                "CREATE TABLE users (name varchar(150), id serial not null primary key, email varchar(150))"
        );
        dataSource.getConnection().createStatement().executeUpdate(
                "CREATE TABLE project_users (" +
                        "project_id int not null," +
                        "user_id int not null," +
                        "PRIMARY KEY (project_id, user_id)," +
                        "CONSTRAINT fk_pid FOREIGN KEY (project_id) REFERENCES projects (id)," +
                        "CONSTRAINT fk_uid FOREIGN KEY (user_id) REFERENCES  users (id))"

        );

        ProjectDao projectDao = new ProjectDao(dataSource);
        UserDao userDao = new UserDao(dataSource);

        Project pObject = new Project("Coffe test, yummy");
        long pID = projectDao.insert(pObject);

        User uObject = new User("Tommy", "No@sniff.yes");
        long uID = userDao.insert(uObject);

        ProjectUserDao puDao = new ProjectUserDao(dataSource);

        ProjectUser pu = new ProjectUser();
        pu.setProjectID(pID);
        pu.setUserID(uID);

        puDao.insert(pu);
        assertThat(puDao.listAll()).contains(pu);

    }

}
