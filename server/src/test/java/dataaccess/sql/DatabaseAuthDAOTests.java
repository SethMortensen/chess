package dataaccess.sql;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatabaseAuthDAOTests {
    @Test
    public void createAndGetAuthTests() throws Exception {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        AuthData auth = authDAO.createAuth(new UserData("username", "password", "email"));
        assert(authDAO.getAuth(auth.authToken()).equals(auth));
        authDAO.clear();
    }

    @Test
    public void deleteAuthTest() throws Exception {
        DatabaseAuthDAO authDAO = new DatabaseAuthDAO();
        AuthData auth = authDAO.createAuth(new UserData("username", "password", "email"));
        authDAO.deleteAuth(auth.authToken());
        assertThrows(DataAccessException.class, () -> authDAO.getAuth(auth.authToken()));
        authDAO.clear();
    }
}
