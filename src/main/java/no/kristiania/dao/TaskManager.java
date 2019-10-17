package no.kristiania.dao;

import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileNotFoundException;
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

    public TaskManager() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("task-manager.properties"));

        datasource.setUrl(properties.getProperty("datasource.url"));
        datasource.setUser(properties.getProperty("datasource.username"));
        datasource.setPassword(properties.getProperty("datasource.password"));

        Flyway.configure().dataSource(datasource).load().migrate();
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
        UserDao userDao = new UserDao(datasource);
        System.out.println("== Do you want to create a user? ['Y' | 'N'] ==");
        switch(scanner.nextLine().toUpperCase()){
            case "Y":
                createUser(userDao);
                break;
            case "N":
                System.out.println("== Aborting ==");
                break;
            default:
                System.err.println("== No valid selection: Aborting ==");
        }

    }

    private void createUser(UserDao userDao) throws SQLException {
        User newUser = new User();
        System.out.println("== Please enter the new user's name: ==");
        newUser.setName(scanner.nextLine());
        System.out.println("== Please enter the new user's e-mail address ==");
        newUser.setEmail(scanner.nextLine());
        userDao.insert(newUser); //Insert new user into DAO
        System.out.println("!= New user has been created: "+ newUser.getName() +" | " + newUser.getEmail() + " =!");
    }

    private void handleProject(){
        System.err.println("Derp");
    }

}
