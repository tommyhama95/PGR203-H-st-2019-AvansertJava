package no.kristiania.dao;

import no.kristiania.dao.daos.UserDao;
import no.kristiania.dao.objects.User;
import no.kristiania.http.controllers.UserListController;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerTest {
    @Test
    void shouldReturnUsersFromDatabase() throws SQLException {
        DaoTest daoTestInstance = new DaoTest();
        UserDao dao = new UserDao(new DaoTest().createDataSource());

        daoTestInstance.setUp();

        UserListController controller = new UserListController(dao);
        User user1 = daoTestInstance.sampleUser();
        User user2 = daoTestInstance.sampleUser();

        assertThat(controller.getBody())
                .contains(String.format("<li id='%s'><a href='alterUser.html?userid=%s&userName=%s&userEmail=%s'>%s #%s </a><br> E-Mail: %s</li><br>",
                        user1.getId(), user1.getId(), user1.getName(), user1.getEmail(), user1.getName(), user1.getId(), user1.getEmail()))
                .contains(String.format("<li id='%s'><a href='alterUser.html?userid=%s&userName=%s&userEmail=%s'>%s #%s </a><br> E-Mail: %s</li><br>",
                        user2.getId(), user2.getId(), user2.getName(), user2.getEmail(), user2.getName(), user2.getId(), user2.getEmail()));
    }
}
