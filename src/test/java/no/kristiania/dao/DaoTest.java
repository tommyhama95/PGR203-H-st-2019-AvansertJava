package no.kristiania.dao;

import no.kristiania.dao.daos.*;
import no.kristiania.dao.objects.*;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoTest {

    final Random random = new Random();
    private ProjectDao projectDao;
    private UserDao userDao;
    private ProjectMemberDao projectMemberDao;
    private TaskDao taskDao;
    private TaskMemberDao taskMemberDao;
    private long projectId;
    private long userId;

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = createDataSource();
        projectDao = new ProjectDao(dataSource);
        userDao = new UserDao(dataSource);
        projectMemberDao = new ProjectMemberDao(dataSource);
        taskDao = new TaskDao(dataSource);
        taskMemberDao = new TaskMemberDao(dataSource);
    }

    @Test
    void shouldListOutProjects() throws SQLException {
        Project project = sampleProject(); // Generates and inserts a sample project
        assertThat(projectDao.listAll()).contains(project);
        assertThat(projectDao.listAll().get(0)).isEqualToComparingFieldByField(project);
    }

    @Test
    void shouldListOutUsers() throws SQLException {
        User user = sampleUser(); // Generates and inserts a sample user
        assertThat(userDao.listAll()).contains(user);
        assertThat(userDao.listAll().get(0)).isEqualToComparingFieldByField(user);
    }

    @Test
    void shouldListProjectMembers() throws SQLException {
        projectId = projectDao.insert(new Project("Do Thing"));
        userId = userDao.insert(new User("TestUser", "Test@testing.no"));

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(projectId);
        projectMember.setUserId(userId);
        projectMemberDao.insert(projectMember);

        assertThat(projectMemberDao.listAll()).contains(projectMember);
        assertThat(projectMemberDao.listAll().get(0)).isEqualToComparingFieldByField(projectMember);
    }

    @Test
    void shouldListOutTasks() throws SQLException {
        Task task = sampleTask(sampleProject().getId()); // Generates and inserts a sample task
        assertThat(taskDao.listAll()).contains(task);
        assertThat(taskDao.listAll().get(0)).isEqualToComparingFieldByField(task);
    }

    @Test
    void shouldListAllTaskMembers() throws SQLException {
        userId = userDao.insert(new User("Olav", "Something@test.nk"));
        projectId = projectDao.insert(new Project("Finish java"));
        projectMemberDao.insert(new ProjectMember(userId, projectId));
        long taskId = taskDao.insert(new Task("Finish Task", "In progress", projectId));

        TaskMember taskMember = new TaskMember(taskId, projectId, userId);
        taskMemberDao.insert(taskMember);

        assertThat(taskMemberDao.listAll()).contains(taskMember);
        assertThat(taskMemberDao.listAll().get(0)).isEqualToComparingFieldByField(taskMember);
    }


    private String selectRandom(String[] alternatives) {
        return alternatives[random.nextInt(alternatives.length)];
    }

    public JdbcDataSource createDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:testDataBase;DB_CLOSE_DELAY=-1");
        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();
        return dataSource;
    }


    public Project sampleProject() throws SQLException {
        Project project = new Project();
        project.setName(selectRandom(new String[]{"Project Sample 1","Project Sample 2","Project Sample 3","Project Sample 4"}));
        project.setId(projectDao.insert(project));
        return project;
    }

    public User sampleUser() throws SQLException {
        User user = new User();
        user.setName(selectRandom(new String[]{"Sansa","Soup","Lemonmaking","MUFFIN"}));
        user.setEmail(selectRandom(new String[]{"Sansa@nope.no", "Soup-can@gmail.eh", "Lemon@making.it","Muffin@choco.late"}));
        user.setId(userDao.insert(user));
        return user;
    }

    public Task sampleTask(long projectId) throws SQLException {
        Task task = new Task();
        task.setName(selectRandom(new String[]{"Ask Johannes", "Somehting", "Drink water"}));
        task.setStatus(selectRandom(new String[]{"To-Do", "In Progress", "Done"}));
        task.setProjectId(projectId);
        task.setId(taskDao.insert(task));
        return task;
    }
}
