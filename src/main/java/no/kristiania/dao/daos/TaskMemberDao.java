package no.kristiania.dao.daos;

import no.kristiania.dao.objects.TaskMember;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TaskMemberDao extends AbstractDao<TaskMember> {

    public TaskMemberDao(DataSource datasource) {
        super(datasource);
    }

    public long insert(TaskMember taskMember) throws SQLException {
       return insert(taskMember, "INSERT INTO task_members (task_id, user_id, project_id) VALUES (?, ?, ?)");
    }

    public List<TaskMember> listAll() throws SQLException {
        return listAll("SELECT * FROM task_members");
    }

    @Override
    protected void insertObject(TaskMember obj, PreparedStatement statement) throws SQLException {
        statement.setLong(1,obj.gettID());
        statement.setLong(2,obj.getuID());
        statement.setLong(3,obj.getpId());
    }

    @Override
    protected TaskMember readObject(ResultSet rs) throws SQLException {
        TaskMember taskMember = new TaskMember();
        taskMember.settID(rs.getLong(1));
        taskMember.setuID(rs.getLong(2));
        taskMember.setpId(rs.getLong(3));
        return taskMember;
    }
}
