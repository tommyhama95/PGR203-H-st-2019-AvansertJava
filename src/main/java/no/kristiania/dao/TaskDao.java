package no.kristiania.dao;

import java.util.ArrayList;
import java.util.List;

public class TaskDao {

  private List<String> taskList = new ArrayList<>();

  public void insert(String make_a_database) {
    taskList.add(make_a_database);
  }

  public List<String> listAll() {
    return taskList;
  }
}
