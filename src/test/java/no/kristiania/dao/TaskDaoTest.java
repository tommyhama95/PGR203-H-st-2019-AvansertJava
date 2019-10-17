package no.kristiania.dao;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskDaoTest {
  @Test
  void shouldReturnTaskList(){
    JdbcDataSource datasource = new JdbcDataSource();
    //TODO: set up h2 database for test
    TaskDao dao = new TaskDao(datasource);
    dao.insert("Make a Database");
    assertThat(dao.listAll()).contains("Make a Database");
  }
}
