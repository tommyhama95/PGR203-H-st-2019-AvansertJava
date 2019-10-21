package no.kristiania.dao;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProjectDao extends AbstractDao<Project> {

    public ProjectDao(DataSource datasource) {
        super(datasource);
    }

    @Override
    protected void insertObject(Project project, PreparedStatement statement) throws SQLException {
        statement.setString(1, project.getName());
    }

    @Override
    protected Project readObject(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setName(rs.getString("name"));
        project.setId((rs.getLong("id")));
        return project;
    }

    //Inserts a value into a column
    public long insert(Project name) throws SQLException{
        return insert(name, "INSERT INTO projects (name) VALUES (?)");
    }

    //This method returns all values in a certain column
    public List<Project> listAll() throws SQLException {
      return listAll("SELECT * FROM projects");
    }
}
