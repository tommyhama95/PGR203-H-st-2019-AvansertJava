package no.kristiania.dao;

import no.kristiania.dao.daos.*;
import no.kristiania.http.HttpServer;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class TaskManagerServer {

    private final HttpServer server;

    public TaskManagerServer(int port) throws IOException {
        Properties properties = new Properties();
        try(FileReader fileReader = new FileReader("task-manager.properties")) {
            properties.load(fileReader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(properties.getProperty("dataSource.url"));
        dataSource.setUser(properties.getProperty("dataSource.username"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));

        Flyway flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();

        server = new HttpServer(port);
        server.setFileLocation("src/main/resources");
        server.addController("/api/projects", new ProjectsController(new ProjectDao(dataSource)));
        server.addController("/api/tasks", new TaskController(new TaskDao(dataSource)));
        server.addController("/api/users", new UsersController(new UserDao(dataSource)));
        server.addController("/api/projectMembers",
                new ProjectMembersController(
                        new ProjectMemberDao(dataSource),
                        new UserDao(dataSource)));
        server.addController("/api/taskMembers",
                new TaskMembersController(
                        new TaskMemberDao(dataSource),
                        new TaskDao(dataSource),
                        new ProjectMemberDao(dataSource),
                        new ProjectDao(dataSource),
                        new UserDao(dataSource)
                ));
    }

    public static void main(String[] args) throws IOException {
        new TaskManagerServer(8080).start();
    }

    private void start() throws IOException {
        server.start();
    }
}
