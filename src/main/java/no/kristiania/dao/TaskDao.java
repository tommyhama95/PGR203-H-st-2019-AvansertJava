package no.kristiania.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {

  private List<String> taskList = new ArrayList<>();
  private DataSource datasource;

  public TaskDao(DataSource datasource) {
    this.datasource = datasource;
  }

  public void insert(String taskName) {
    taskList.add(taskName);


    try (Connection conn = datasource.getConnection()) {
      PreparedStatement statement = conn.prepareStatement("INSERT INTO tablename (colonName) VALUES (?)");
      statement.setString(1, taskName);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  public List<String> listAll() {
    return taskList;
  }
}
