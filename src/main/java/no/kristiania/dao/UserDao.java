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
  public void insert(String userName) throws SQLException{
    insert(userName, "INSERT INTO users (name) VALUES (?)");
  }

  //This method returns all values in a certain column
  public List<String> listAll() throws SQLException {
    return listAll("SELECT * FROM users");
  }

}
