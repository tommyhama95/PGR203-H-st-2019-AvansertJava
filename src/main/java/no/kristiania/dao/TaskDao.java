package no.kristiania.dao;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.ConnectionEvent;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TaskDao {

  private DataSource datasource;

  public TaskDao(DataSource datasource) {
    this.datasource = datasource;
  }

  //Inserts a value into a column
  public void insert(String taskName) {
    try (Connection conn = datasource.getConnection()) {
      PreparedStatement statement = conn.prepareStatement("INSERT INTO tasks (name) VALUES (?)");
      statement.setString(1, taskName);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  //This method returns all values in a certain column
  public List<String> listAll(String columnName) throws SQLException {
    List<String> result = new ArrayList<>();
    try(Connection connection = datasource.getConnection()){
      try(PreparedStatement statement = connection.prepareStatement(
              "SELECT * FROM tasks"
      )) {
        try(ResultSet rs = statement.executeQuery()){
          while(rs.next()){
            result.add(rs.getString(columnName));
          }
          return result;
        }
      }
    }
  }

  public static void main(String[] args) throws IOException {
    Properties properties = new Properties();
    properties.load(new FileReader("task-manager.properties"));

    PGSimpleDataSource datasource = new PGSimpleDataSource();
    datasource.setUrl("jdbc:postgresql://localhost:5432/taskmanager");
    datasource.setUser("tomas"); //Reference to databases year one hehe
    datasource.setPassword(properties.getProperty("datasource.password"));
    TaskDao dao = new TaskDao(datasource);
    dao.insert("test");

  }

}
