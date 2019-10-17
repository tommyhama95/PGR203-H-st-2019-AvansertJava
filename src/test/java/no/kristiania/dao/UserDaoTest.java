package no.kristiania.dao;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {
  @Test
  void shouldGetUserFromQuery() throws SQLException {
    JdbcDataSource datasource = new JdbcDataSource();
    datasource.setUrl("jdbc:h2:mem:testDataBase;DB_CLOSE_DELAY=-1");

    Flyway.configure().dataSource(datasource).load().migrate();

    User user = new User();
    user.setName("Ole");
    user.setEmail("ole.nordmann@norsk.no");
    UserDao dao = new UserDao(datasource);
    dao.insert(user);
    assertThat(dao.listAll()).contains(user);
  }
}
