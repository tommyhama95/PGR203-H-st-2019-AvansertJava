package no.kristiania.dao;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TaskMemberDao extends AbstractDao<TaskMember> {

    public TaskMemberDao(DataSource datasource) {
        super(datasource);
    }

    public long insert(TaskMember taskMember) {
       return 2;
    }

    public List<TaskMember> listAll(){
        return null;
    }

    @Override
    protected void insertObject(TaskMember obj, PreparedStatement statement) throws SQLException {

    }

    @Override
    protected TaskMember readObject(ResultSet rs) throws SQLException {
        return null;
    }
}
