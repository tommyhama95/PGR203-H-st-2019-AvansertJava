package no.kristiania.dao;

import no.kristiania.dao.daos.ProjectDao;
import no.kristiania.dao.daos.ProjectMemberDao;
import no.kristiania.dao.daos.UserDao;
import no.kristiania.dao.objects.Project;
import no.kristiania.dao.objects.ProjectMember;
import no.kristiania.dao.objects.User;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class TaskManager {

    private final PGSimpleDataSource dataSource = new PGSimpleDataSource();
    private final Scanner scanner = new Scanner(System.in);

    private final UserDao uDao;
    private final ProjectDao pDao;
    private final ProjectMemberDao puDao;
    private final String errorMessage = "== No valid selection: Aborting ==";

    public TaskManager() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("task-manager.properties"));

        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setUser(properties.getProperty("dataSource.username"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.clean();
        flyway.migrate();

        this.uDao = new UserDao(dataSource);
        this.pDao = new ProjectDao(dataSource);
        this.puDao = new ProjectMemberDao(dataSource);
    }

    //Runs the interface in terminal to ask user to edit or print database
    public static void main(String[] args) throws IOException, SQLException {
        new TaskManager().userInterfaceTerminal();
    }

    //User chooses between user or project DB editing/listing
    private void userInterfaceTerminal() throws SQLException {
        while(true) {
            System.out.println("== What do you want to do? ['user' | 'project'] ==");
            String userInput = scanner.nextLine();

            switch (userInput) {
                case "user":
                    handleUser();
                    break;
                case "project":
                    handleProject();
                    break;
                default:
                    System.err.println(errorMessage);
            }
            System.out.println("\n!===== PROCESS FINISHED: Starting new process... =====!\n\n");
        }
    }

    //If user was used this will run
    private void handleUser() throws SQLException {
        System.out.println("\n== USER: What do you want to do: ['createuser' | 'listusers' | 'listuserprojects'] ==");
        switch(scanner.nextLine().toLowerCase()){
            case "createuser":
                createUser();
                break;
            case "listusers":
                System.out.println(uDao.listAll());
                break;
            case "listuserprojects":
                System.out.println("\n== Type the ID of the user: ==");
                List<ProjectMember> result = puDao.listProjectsWith(Long.parseLong(scanner.nextLine()));
                if(result.isEmpty()) {
                    System.out.println("\nThis user is not assigned to any projects!");
                }
                System.out.println(result);
                break;
            default:
                System.err.println(errorMessage);
        }

    }

    //If user wants to create a new user
    private User createUser() throws SQLException {
        User newUser = new User();
        System.out.println("\n== Please enter the new user's name: ==");
        newUser.setName(scanner.nextLine());
        System.out.println("\n== Please enter the new user's e-mail address ==");
        newUser.setEmail(scanner.nextLine());
        newUser.setId(uDao.insert(newUser));
        System.out.println("\n!= New user has been created: " +
                newUser.getName() +" | " + newUser.getEmail() +
                " with ID: " + newUser.getId() + " =!");
        return newUser;
    }

    //If user wants to create or edit project table
    private void handleProject() throws SQLException {
        System.out.println("\n== PROJECT: What do you want to do: [ 'newproject' | 'listprojects' | 'listprojectmembers' | 'addusertoproject' ]");
        switch(scanner.nextLine().toLowerCase()){
            case "newproject":
                createProject();
                break;
            case "listprojects":
                System.out.println(pDao.listAll());
                break;
            case "listprojectmembers":
                System.out.println("\n== Type the ID of the project; ==");
                System.out.println(puDao.listMembersOf(Long.parseLong(scanner.nextLine())));
                break;
            case "addusertoproject":
                addUserToProject();
                break;
            default:
                System.out.println(errorMessage);
        }
    }

    //Expects you to know user and project ID.
    private void addUserToProject() throws SQLException {
        System.out.println("\n== Input the ID of a project you want to add a member to: ==");
        long projectId = Long.parseLong(scanner.nextLine());

        System.out.println("\n== Input the ID of a user you want to add to project: " + projectId + " ==");
        long userId = Long.parseLong(scanner.nextLine());

        ProjectMember newProjectUser = new ProjectMember();
        newProjectUser.setProjectID(projectId);
        newProjectUser.setUserID(userId);

        puDao.insert(newProjectUser);
    }

    //If user wants to create new Project
    private void createProject() throws SQLException {
        Project newProject = new Project();
        System.out.println("\n== PROJECT: Give a name to the new project: ==");
        newProject.setName(scanner.nextLine());
        newProject.setId(pDao.insert(newProject));
        System.out.println("\n== Project name has been set to + " + newProject.getName() +
                " with ID: " + newProject.getId());
        System.out.println("\n== Please assign an owner to the project. [ exists | newuser ]==");
        ProjectMember newProjectOwner = new ProjectMember();
        newProjectOwner.setProjectID(newProject.getId());

        switch (scanner.nextLine()){
            case "exists":
                System.out.println("\n== Input the ID of the user ==");
                long ownerId = Long.parseLong(scanner.nextLine());
                newProjectOwner.setUserID(ownerId);
                System.out.println("\n== User: " + ownerId +
                        " has been set as owner of " + newProject);
                break;
            case "newuser":
                User newUser = createUser();
                newProjectOwner.setUserID(newUser.getId());
                System.out.println("\n== User: " + newUser.getName() +
                        " has been set as owner of " + newProject);
                break;
            default:
                System.out.println(errorMessage);
        }
        puDao.insert(newProjectOwner);
    }

}
