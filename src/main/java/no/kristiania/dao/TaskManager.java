package no.kristiania.dao;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class TaskManager {

    /*
    TODO: Vis alle users i ProjectUsers tabellen "ListAllProjectMembers()"
    TODO: Mulighet til å legge in eksisterende brukere til et prosjekt "AddUserToProject(User,Project)"
    TODO: Skrive ut hele ProjectUsers tabellen "ListAll()" in ProjectUsersDao
    TODO: - - -
    TODO:
    TODO: Extras!
    TODO:
    TODO: - - -
    TODO: Maven Package og ".jar"
     */

    private final PGSimpleDataSource datasource = new PGSimpleDataSource();
    private final Scanner scanner = new Scanner(System.in);

    private final UserDao uDao;
    private final ProjectDao pDao;
    private final ProjectUserDao puDao;

    public TaskManager() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("task-manager.properties"));

        datasource.setUrl(properties.getProperty("datasource.url"));
        datasource.setUser(properties.getProperty("datasource.username"));
        datasource.setPassword(properties.getProperty("datasource.password"));

        Flyway.configure().dataSource(datasource).load().migrate();

        this.uDao = new UserDao(datasource);
        this.pDao = new ProjectDao(datasource);
        this.puDao = new ProjectUserDao(datasource);
    }

    public static void main(String[] args) throws IOException, SQLException {
        new TaskManager().run();
    }

    private void run() throws SQLException {
        System.out.println("== What do you want to do? ['user' | 'project'] ==");
        String userInput = scanner.nextLine();


        switch(userInput){
            case "user":
                handleUser();
                break;
            case "project":
                handleProject();
                break;
            default:
                System.err.println("== No valid selection made ==");

        }
    }

    private void handleUser() throws SQLException {
        System.out.println("== USER: What do you want to do: ['createuser' | 'listusers' | 'listuserprojects'] ==");
        switch(scanner.nextLine().toLowerCase()){
            case "createuser":
                createUser();
                break;
            case "listusers":
                System.out.println(uDao.listAll());
                break;
            case "listuserprojects":
                System.out.println("== Type the ID of the user: ==");
                System.out.println(puDao.listProjectsWith(Long.parseLong(scanner.nextLine())));
                break;
            default:
                System.err.println("== No valid selection: Aborting ==");
        }

    }

    private User createUser() throws SQLException {
        User newUser = new User();
        System.out.println("== Please enter the new user's name: ==");
        newUser.setName(scanner.nextLine());
        System.out.println("== Please enter the new user's e-mail address ==");
        newUser.setEmail(scanner.nextLine());
        newUser.setId(uDao.insert(newUser));
        System.out.println("!= New user has been created: " +
                newUser.getName() +" | " + newUser.getEmail() +
                " with ID: " + newUser.getId() + " =!");
        return newUser;
    }

    private void handleProject() throws SQLException {
        System.out.println("== PROJECT: What do you want to do: [ 'newproject' | 'listprojects' | 'listprojectmembers' | 'addusertoproject' ]");
        switch(scanner.nextLine().toLowerCase()){
            case "newproject":
                createProject();
                break;
            case "listprojects":
                System.out.println(pDao.listAll());
                break;
            case "listprojectmembers":
                System.out.println("== Type the ID of the project; ==");
                System.out.println(puDao.listMembersOf(Long.parseLong(scanner.nextLine())));
                break;
            case "addusertoproject":
                addUserToProject();
                break;
            default:
                System.out.println("== Not a valid argument: Aborting ==");
        }
    }

    //Expects you to know user and project ID.
    private void addUserToProject() throws SQLException {
        System.out.println("== Input the ID of a project you want to add a member to: ==");
        long projectId = Long.parseLong(scanner.nextLine());

        System.out.println("== Input the ID of a user you want to add to project: " + projectId + " ==");
        long userId = Long.parseLong(scanner.nextLine());

        ProjectUser newProjectUser = new ProjectUser();
        newProjectUser.setProjectID(projectId);
        newProjectUser.setUserID(userId);

        puDao.insert(newProjectUser);
    }

    private void createProject() throws SQLException {
        Project newProject = new Project();
        System.out.println("== PROJECT: Give a name to the new project: ==");
        newProject.setName(scanner.nextLine());
        newProject.setId(pDao.insert(newProject));
        System.out.println("== Project name has been set to + " + newProject.getName() +
                " with ID: " + newProject.getId());
        System.out.println("== Please assign an owner to the project. [ exists | newuser ]==");
        ProjectUser newProjectOwner = new ProjectUser();
        newProjectOwner.setProjectID(newProject.getId());

        switch (scanner.nextLine()){
            case "exists":
                long ownerId = Long.parseLong(scanner.nextLine());
                newProjectOwner.setUserID(ownerId);
                System.out.println("== User: " + ownerId +
                        " has been set as owner of " + newProject);

                break;
            case "newuser":
                User newUser = createUser();
                newProjectOwner.setUserID(newUser.getId());
                System.out.println("== User: " + newUser.getName() +
                        " has been set as owner of " + newProject);
                break;
            default:
                System.out.println("== No valid selection made: Aborting");
        }

        puDao.insert(newProjectOwner);

    }

}
