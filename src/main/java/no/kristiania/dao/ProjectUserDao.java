package no.kristiania.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProjectUserDao extends AbstractDao<ProjectUser>{

    public ProjectUserDao(DataSource datasource) {
        super(datasource);
    }

    //Calls abstract method for inserting new object for postgres database
    public void insert(ProjectUser pu) throws SQLException{
        insert(pu, "INSERT INTO project_users (project_id, user_id) VALUES (?, ?)");
    }

    //Calls abstract method for listing out table values
    public List<ProjectUser> listAll() throws SQLException{
        return listAll("SELECT * FROM project_users");
    }

    //Calls method in same class to make the ResultSet of this query
    public List<ProjectUser> listMembersOf(long projectId) throws SQLException{
        return listAllWithStatement(projectId,  "SELECT * FROM project_users WHERE project_id = ?");
    }

    //Calls method in same class to make the ResultSet of this query
    public List<ProjectUser> listProjectsWith(long userId) throws SQLException {
        return listAllWithStatement(userId, "SELECT * FROM project_users WHERE user_id = ?");
    }

    //Makes ResultSet of query with one parameter
    public List<ProjectUser> listAllWithStatement(long idValue, String sql) throws SQLException {
        List<ProjectUser> result = new ArrayList<>();
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


