package no.kristiania.dao;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class UserDao {

  private DataSource datasource;

  public UserDao(DataSource datasource) {
    this.datasource = datasource;
  }

  //Inserts a value into a column
  public void insert(String userName) {
    try (Connection conn = datasource.getConnection()) {
      PreparedStatement statement = conn.prepareStatement("INSERT INTO users (name) VALUES (?)");
      statement.setString(1, userName);
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
              "SELECT * FROM users"
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
    Scanner scanner = new Scanner(System.in);

    Properties properties = new Properties();
    properties.load(new FileReader("task-manager.properties"));

    PGSimpleDataSource datasource = new PGSimpleDataSource();
    datasource.setUrl("jdbc:postgresql://localhost:5432/taskmanager");
    datasource.setUser("tomas"); //Reference to databases year one hehe
    datasource.setPassword(properties.getProperty("datasource.password"));
    UserDao dao = new UserDao(datasource);

    System.out.println("== Please type the name of a user you want to create ==");
    String userInput = scanner.nextLine();

    dao.insert(userInput);
    System.out.println("= You created the user: " + userInput + " =");

  }

}
