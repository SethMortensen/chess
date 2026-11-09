package dataaccess.sql;

import model.UserData;
import org.junit.jupiter.api.Test;

public class DatabaseUserDAOTests {
    @Test
    public void createAndGetUserTest() throws Exception {
        DatabaseUserDAO userDAO = new DatabaseUserDAO();
        userDAO.createUser(new UserData("username", "password", "email"));
        assert(userDAO.getUser("username", "password") != null);
        userDAO.clear();
    }
}
