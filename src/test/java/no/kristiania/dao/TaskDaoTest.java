package no.kristiania.dao;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskDaoTest {
  @Test
  void shouldReturnTaskList() throws SQLException {
    JdbcDataSource datasource = new JdbcDataSource();
    datasource.setUrl("jdbc:h2:mem:testDataBase");
    datasource.getConnection().createStatement().executeUpdate(
            "CREATE TABLE tasks (name varchar(100))"
    );
    //TODO: set up h2 database for test
    TaskDao dao = new TaskDao(datasource);
    dao.insert("Make a Database");
    assertThat(dao.listAll()).contains("Make a Database");
  }
}
