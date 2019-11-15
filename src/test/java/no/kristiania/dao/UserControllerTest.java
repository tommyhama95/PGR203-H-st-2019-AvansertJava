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
                .contains(String.format("<li id='%s'>%s</li>", user1.getId(), user1.getName()))
                .contains(String.format("<li id='%s'>%s</li>", user2.getId(), user2.getName()));

        //TODO: Give LI tags href attributes for links later
    }
}
