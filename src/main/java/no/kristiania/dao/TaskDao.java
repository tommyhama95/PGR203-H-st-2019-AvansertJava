package no.kristiania.dao;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TaskDao extends AbstractDao<Task>{
    public TaskDao(JdbcDataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void insertObject(Task obj, PreparedStatement statement) throws SQLException {

    }

    @Override
    protected Task readObject(ResultSet rs) throws SQLException {
        return null;
    }

    public Object insert(Task task) {
        return null;
    }

    public List<Task> listAll() {
        return null;
    }
}
