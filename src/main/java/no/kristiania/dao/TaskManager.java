package no.kristiania.dao;

import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class TaskManager {

    private final PGSimpleDataSource datasource = new PGSimpleDataSource();

    public TaskManager() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("task-manager.properties"));

        datasource.setUrl(properties.getProperty("datasource.url"));
        datasource.setUser(properties.getProperty("datasource.username"));
        datasource.setPassword(properties.getProperty("datasource.password"));
    }

    public static void main(String[] args) throws IOException, SQLException {
        new TaskManager().run();
    }

    private void run() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        UserDao userDao = new UserDao(datasource);

        System.out.println("== Please type the name of a user you want to create ==");
        String userInput = scanner.nextLine();

        userDao.insert(userInput);
        System.out.println("= You created the user: " + userInput + " =");
    }

}
