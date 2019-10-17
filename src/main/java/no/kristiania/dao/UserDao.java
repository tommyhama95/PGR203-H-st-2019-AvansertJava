package no.kristiania.dao;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class UserDao extends AbstractDao<String> {

  public UserDao(DataSource datasource) {
    super(datasource);
  }

  @Override
  protected void insertObject(String userName, PreparedStatement statement) throws SQLException {
    statement.setString(1, userName);
  }

  @Override
  protected String readObject(ResultSet rs) throws SQLException {
    return rs.getString("name");
  }

  //Inserts a value into a column
  public void insert(String userName) {
    insert(userName, "INSERT INTO users (name) VALUES (?)");
  }

  //This method returns all values in a certain column
  public List<String> listAll() throws SQLException {
    return listAll("SELECT * FROM users");
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
