package no.kristiania.dao.daos;

import no.kristiania.dao.objects.ProjectMember;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProjectMemberDao extends AbstractDao<ProjectMember>{

    public ProjectMemberDao(DataSource datasource) {
        super(datasource);
    }

    //Inserts new projectmember with id from project and user
    public void insert(ProjectMember projectMember) throws SQLException{
        insert(projectMember, "INSERT INTO project_members (project_id, user_id) VALUES (?, ?)");
    }

    //List out all members in database
    public List<ProjectMember> listAll() throws SQLException{
        return listAll("SELECT * FROM project_members");
    }

    //List out members from specific project
    public List<ProjectMember> listMembersOf(long projectId) throws SQLException{
        return listAllWithStatement(new long[]{projectId},  "SELECT * FROM project_members WHERE project_id = ?");
    }

    @Override
    protected void insertObject(ProjectMember projectMember, PreparedStatement statement) throws SQLException {
        statement.setLong(1, projectMember.getProjectId());
        statement.setLong(2, projectMember.getUserId());
    }

    @Override
    protected ProjectMember readObject(ResultSet rs) throws SQLException {
        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(rs.getLong("project_id"));
        projectMember.setUserId(rs.getLong("user_id"));
        return projectMember;
    }
}


