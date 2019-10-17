package no.kristiania.dao;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProjectUserDao extends AbstractDao<ProjectUser>{

    public ProjectUserDao(DataSource datasource) {
        super(datasource);
    }

    public void insert(ProjectUser pu) throws SQLException{
        insert(pu, "INSERT INTO project_users (project_id, user_id) VALUES (?, ?)");
    }

    public List<ProjectUser> listAll() throws SQLException{
        return listAll("SELECT * FROM project_users");
    }


    @Override
    protected void insertObject(ProjectUser obj, PreparedStatement statement) throws SQLException {
        statement.setLong(1, obj.getProjectID());
        statement.setLong(2, obj.getUserID());
    }

    @Override
    protected ProjectUser readObject(ResultSet rs) throws SQLException {
        ProjectUser pu = new ProjectUser();
        pu.setProjectID(rs.getLong("project_id"));
        pu.setUserID(rs.getLong("user_id"));
        return pu;
    }
}


