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

    Random random = new Random();
    private ProjectDao pDao;
    private UserDao uDao;
    private ProjectMemberDao pmDao;
    private TaskDao tDao;
    private TaskMemberDao tmDao;

    @BeforeEach
    void setUp() {
        JdbcDataSource dataSource = createDataSource();
        pDao = new ProjectDao(dataSource);
        uDao = new UserDao(dataSource);
        pmDao = new ProjectMemberDao(dataSource);
        tDao = new TaskDao(dataSource);
        tmDao = new TaskMemberDao(dataSource);
    }

    @Test
    void shouldListOutProjects() throws SQLException {
        Project project = sampleProject(); // Generates and inserts a sample project
        assertThat(pDao.listAll()).contains(project);
        assertThat(pDao.listAll().get(0)).isEqualToComparingFieldByField(project);
    }

    @Test
    void shouldListOutUsers() throws SQLException {
        User user = sampleUser(); // Generates and inserts a sample user
        assertThat(uDao.listAll()).contains(user);
        assertThat(uDao.listAll().get(0)).isEqualToComparingFieldByField(user);
    }

    @Test
    void shouldListProjectMembers() throws SQLException {
        long pID = pDao.insert(new Project("Do Thing"));
        long uID = uDao.insert(new User("TestUser", "Test@testing.no"));

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(pID);
        projectMember.setUserId(uID);
        pmDao.insert(projectMember);

        assertThat(pmDao.listAll()).contains(projectMember);
        assertThat(pmDao.listAll().get(0)).isEqualToComparingFieldByField(projectMember);
    }

    @Test
    void shouldListOutTasks() throws SQLException {
        Task task = sampleTask(sampleProject().getId()); // Generates and inserts a sample task
        assertThat(tDao.listAll()).contains(task);
        assertThat(tDao.listAll().get(0)).isEqualToComparingFieldByField(task);
    }

    @Test
    void shouldListAllTaskMembers() throws SQLException {
        long uID = uDao.insert(new User("Olav", "Something@test.nk"));
        long pID = pDao.insert(new Project("Finish java"));
        pmDao.insert(new ProjectMember(uID, pID));
        long tID = tDao.insert(new Task("Finish Task", "In progress", pID));

        TaskMember taskMember = new TaskMember(tID, pID, uID);
        tmDao.insert(taskMember);

        assertThat(tmDao.listAll()).contains(taskMember);
        assertThat(tmDao.listAll().get(0)).isEqualToComparingFieldByField(taskMember);
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
        project.setId(pDao.insert(project));
        return project;
    }

    public User sampleUser() throws SQLException {
        User user = new User();
        user.setName(selectRandom(new String[]{"Sansa","Soup","Lemonmaking","MUFFIN"}));
        user.setEmail(selectRandom(new String[]{"Sansa@nope.no", "Soup-can@gmail.eh", "Lemon@making.it","Muffin@choco.late"}));
        user.setId(uDao.insert(user));
        return user;
    }

    public Task sampleTask(long projectId) throws SQLException {
        Task task = new Task();
        task.setName(selectRandom(new String[]{"Ask Johannes", "Somehting", "Drink water"}));
        task.setStatus(selectRandom(new String[]{"To-Do", "In Progress", "Done"}));
        task.setProjectId(projectId);
        task.setId(tDao.insert(task));
        return task;
    }
}
