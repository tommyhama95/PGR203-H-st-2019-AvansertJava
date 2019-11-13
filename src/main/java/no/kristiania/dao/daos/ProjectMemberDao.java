package no.kristiania.dao.daos;

import no.kristiania.dao.objects.ProjectMember;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectMemberDao extends AbstractDao<ProjectMember>{

    public ProjectMemberDao(DataSource datasource) {
        super(datasource);
    }

    //Calls abstract method for inserting new object for postgres database
    public void insert(ProjectMember pu) throws SQLException{
        insert(pu, "INSERT INTO project_members (project_id, user_id) VALUES (?, ?)");
    }

    //Calls abstract method for listing out table values
    public List<ProjectMember> listAll() throws SQLException{
        return listAll("SELECT * FROM project_members");
    }

    //Calls method in same class to make the ResultSet of this query
    public List<ProjectMember> listMembersOf(long projectId) throws SQLException{
        return listAllWithStatement(projectId,  "SELECT * FROM project_members WHERE project_id = ?");
    }

    //Calls method in same class to make the ResultSet of this query
    public List<ProjectMember> listProjectsWith(long userId) throws SQLException {
        return listAllWithStatement(userId, "SELECT * FROM project_members WHERE user_id = ?");
    }

    @Override
    protected void insertObject(ProjectMember obj, PreparedStatement statement) throws SQLException {
        statement.setLong(1, obj.getProjectID());
        statement.setLong(2, obj.getUserID());
    }

    @Override
    protected ProjectMember readObject(ResultSet rs) throws SQLException {
        ProjectMember pu = new ProjectMember();
        pu.setProjectID(rs.getLong("project_id"));
        pu.setUserID(rs.getLong("user_id"));
        return pu;
    }
}


