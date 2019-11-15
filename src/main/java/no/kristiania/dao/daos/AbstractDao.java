package no.kristiania.dao.daos;

import no.kristiania.dao.objects.ProjectMember;
import no.kristiania.dao.objects.TaskMember;

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
            if(object instanceof ProjectMember || object instanceof TaskMember){
                return 0; //Do not attempt to get ID on Connection Entities
            }
            return generatedkeys.getLong("id");
        }
    }

    //Makes ResultSet of query with one parameter
    public List<T> listAllWithStatement(long idValue, String sql) throws SQLException {
        List<T> result = new ArrayList<>();
        try(Connection connection = datasource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, idValue);
                try(ResultSet rs = statement.executeQuery()){
                    while(rs.next()){
                        result.add(readObject(rs));
                    }
                    return result;
                }
            }
        }
    }

    public void updateValueWithStatement(String newValue, long idValue, String sql) throws SQLException {
        try(Connection connection = datasource.getConnection()){
            try(PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1, newValue);
                statement.setLong(2, idValue);
                statement.executeUpdate();
            }
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
