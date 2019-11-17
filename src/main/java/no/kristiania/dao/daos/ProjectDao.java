package no.kristiania.dao.daos;

import no.kristiania.dao.objects.Project;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProjectDao extends AbstractDao<Project> {

    public ProjectDao(DataSource datasource) {
        super(datasource);
    }

    //Inserts new project to database
    public long insert(Project name) throws SQLException{
        return insert(name, "INSERT INTO projects (name) VALUES (?)");
    }

    //List out all project from database
    public List<Project> listAll() throws SQLException {
      return listAll("SELECT * FROM projects");
    }

    //List out specific project from id
    public List<Project> getProjectFromId(long projectId) throws SQLException {
        return listAllWithStatement(new long[] {projectId}, "SELECT * FROM projects WHERE id = (?)");
    }

    //Update new Project name to database
    public void updateProjectName(String name, long projectId) throws SQLException {
        updateValueWithStatement(name, projectId, "UPDATE projects SET name = (?) WHERE id = (?)");
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

}
