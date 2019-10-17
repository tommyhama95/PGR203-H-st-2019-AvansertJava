package no.kristiania.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T> {
  protected DataSource datasource;

  public AbstractDao(DataSource datasource) {
    this.datasource = datasource;
  }

  //Inserts a value into a column (Method is overridden in child classes)
  public long insert(T object, String sql) throws SQLException{
    try (Connection conn = datasource.getConnection()) {
      PreparedStatement statement = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
      insertObject(object, statement);
      statement.executeUpdate();

      ResultSet generatedkeys = statement.getGeneratedKeys();
      generatedkeys.next();
      if(object instanceof ProjectUser){
        return 0; //Do not attempt to get ID on ProjectUser
      }
      return generatedkeys.getLong("id"); //TODO: Remove if remains unused later
    }
  }

  protected abstract void insertObject(T obj, PreparedStatement statement) throws SQLException;

  //This method returns all values in a certain column
  public List<T> listAll(String sql) throws SQLException {
    List<T> result = new ArrayList<>();
    try(Connection connection = datasource.getConnection()){
      try(PreparedStatement statement = connection.prepareStatement(sql)) {
        try(ResultSet rs = statement.executeQuery()){
          while(rs.next()){
            result.add(readObject(rs));
          }
          return result;
        }
      }
    }
  }

  protected abstract T readObject(ResultSet rs) throws SQLException;
}
