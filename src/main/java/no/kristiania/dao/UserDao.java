package no.kristiania.dao;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao extends AbstractDao<User> {

  public UserDao(DataSource datasource) {
    super(datasource);
  }

  @Override
  protected void insertObject(User user, PreparedStatement statement) throws SQLException {
    statement.setString(1, user.getName());
    statement.setString(2, user.getEmail());
  }

  @Override
  protected User readObject(ResultSet rs) throws SQLException {
    User user = new User();
    user.setName(rs.getString("name"));
    user.setEmail(rs.getString("email"));
    return user;
  }

  //Inserts a value into a column
  public void insert(User user) throws SQLException{
    insert(user, "INSERT INTO users (name, email) VALUES (?, ?)");
  }

  //This method returns all values in a certain column
  public List<User> listAll() throws SQLException {
    return listAll("SELECT * FROM users");
  }

}
