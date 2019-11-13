package no.kristiania.dao;

import no.kristiania.dao.daos.ProjectDao;
import no.kristiania.http.HttpServer;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileReader;
import java.io.IOException;
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

        Flyway.configure().dataSource(dataSource).load().migrate();

        server = new HttpServer(port);
        server.setFileLocation("src/main/resources");
        server.addController("/api/projects", new ProjectsController(new ProjectDao(dataSource)));
    }

    public static void main(String[] args) throws IOException {
        new TaskManagerServer(8080).start();
    }

    private void start() throws IOException {
        server.start();
    }
}
