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
    protected void insertObject(Task task, PreparedStatement statement) throws SQLException {
        statement.setString(1, task.getName());
        statement.setString(2, task.getStatus());
        statement.setLong(3, task.getProjectID());
    }

    @Override
    protected Task readObject(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setName(rs.getString("name"));
        task.setStatus(rs.getString("status"));
        task.setProjectID(rs.getLong("project_id"));
        return task;
    }

    public long insert(Task task) throws SQLException {
        return insert(task, "INSERT INTO tasks (name, status, project_id) VALUES (?, ?, ?)");
    }

    public List<Task> listAll() throws SQLException {
        return listAll("SELECT * FROM Tasks");
    }
}
