package no.kristiania.dao;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TaskDaoTest {
  @Test
  void shouldReturnTaskList(){
    TaskDao dao = new TaskDao();
    dao.insert("Make a Database");
    assertThat(dao.listAll()).contains("Make a Database");
  }
}
