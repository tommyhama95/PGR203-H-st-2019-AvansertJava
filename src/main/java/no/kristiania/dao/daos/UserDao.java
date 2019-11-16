package no.kristiania.dao.daos;

import no.kristiania.dao.objects.User;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao extends AbstractDao<User> {

    public UserDao(DataSource datasource) {
        super(datasource);
    }

    @Override
    protected void insertObject(User user, PreparedStatement statement) throws SQLException {
        statement.setString(1, user.getName());
        statement.setString(2, user.getEmail());
    }

    @Override
    protected User readObject(ResultSet rs) throws SQLException {
        User user = new User();
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setId((rs.getLong("id")));
        return user;
    }

    //Inserts a value into a column
    public long insert(User user) throws SQLException{
        return insert(user, "INSERT INTO users (name, email) VALUES (?, ?)");
    }

    //This method returns all values in a certain column
    public List<User> listAll() throws SQLException {
        return listAll("SELECT * FROM users");
    }

    public User getUserById(long id) throws SQLException {
        return listAllWithStatement(new long[]{id}, "SELECT * FROM users WHERE id = (?)").get(0);
    }

    public void updateUserValues(String name, String email, long userId) throws SQLException {
        updateAllValuesWithStatement(name, email, userId, "UPDATE users SET name = (?), email = (?) WHERE id = (?)");
    }
}
