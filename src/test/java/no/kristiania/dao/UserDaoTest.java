package no.kristiania.dao;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {
    Random random = new Random();
    private UserDao dao;
    private JdbcDataSource dataSource;

    public User sampleUser() throws SQLException {
        User user = new User();

        int randomValue = random.nextInt(4);
        user.setName(selectRandom(new String[]{"Sansa","Soup","Lemonmaking","MUFFIN"},randomValue));
        user.setEmail(selectRandom(new String[]{"Sansa@nope.no", "Soup-can@gmail.eh", "Lemon@making.it","Muffin@choco.late"},randomValue));
        user.setId(dao.insert(user));
        return user;
    }

    @BeforeEach
    void setUp(){
        this.dataSource = ProjectDaoTest.createDataSource();
        dao = new UserDao(dataSource);
    }

    @Test
    void shouldListOutUsers() throws SQLException {
        User user = sampleUser();
        user.setId(dao.insert(user));
        assertThat(dao.listAll()).contains(user);
    }

    private String selectRandom(String[] alternatives, int randomValue) {
        return alternatives[randomValue];
    }
}
