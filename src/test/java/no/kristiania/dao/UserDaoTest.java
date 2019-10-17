package no.kristiania.dao;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {
  @Test
  void shouldReturnTaskList() throws SQLException {
    JdbcDataSource datasource = new JdbcDataSource();
    datasource.setUrl("jdbc:h2:mem:testDataBase");
    datasource.getConnection().createStatement().executeUpdate(
            "CREATE TABLE users (name varchar(100))"
    );
    UserDao dao = new UserDao(datasource);
    dao.insert("Tommy");
    assertThat(dao.listAll("name")).contains("Tommy");
  }
}
