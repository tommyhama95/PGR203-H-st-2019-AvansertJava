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

    //Inserts new task member to database
    public long insert(TaskMember taskMember) throws SQLException {
       return insert(taskMember, "INSERT INTO task_members (task_id, project_id, user_id) VALUES (?, ?, ?)");
    }

    //List out all task members
    public List<TaskMember> listAll() throws SQLException {
        return listAll("SELECT * FROM task_members");
    }

    //List out all task members from specific task
    public List<TaskMember> listMembersOf(long taskId) throws SQLException {
        return listAllWithStatement(new long[]{taskId}, "SELECT * FROM task_members WHERE task_id = (?)");
    }

    //List out all task member has from specific project
    public List<TaskMember> listTasksOnMemberInProject(long userId, long projectId) throws SQLException {
        return listAllWithStatement(new long[]{userId, projectId},
                "SELECT * FROM task_members WHERE user_id = (?) AND project_id = (?)");
    }

    @Override
    protected void insertObject(TaskMember obj, PreparedStatement statement) throws SQLException {
        statement.setLong(1,obj.getTaskId());
        statement.setLong(2,obj.getProjectId());
        statement.setLong(3,obj.getUserId());
    }

    @Override
    protected TaskMember readObject(ResultSet rs) throws SQLException {
        TaskMember taskMember = new TaskMember();
        taskMember.setTaskId(rs.getLong(1));
        taskMember.setProjectId(rs.getLong(2));
        taskMember.setUserId(rs.getLong(3));
        return taskMember;
    }

}
