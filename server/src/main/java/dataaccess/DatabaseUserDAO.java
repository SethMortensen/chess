package dataaccess;

import model.UserData;
import java.sql.*;
import static dataaccess.DatabaseManager.*;

public class DatabaseUserDAO implements UserDAO {
    public DatabaseUserDAO() throws DataAccessException {
        try {
            createDatabase();
            var conn = getConnection();
            conn.setCatalog("chess");
            var createUserTable = """
                CREATE TABLE IF NOT EXISTS users (
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL
            )""";
            try (var createTableStatement = conn.prepareStatement(createUserTable)) {
                createTableStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("User Database initialization unsuccessful.");
        }
    }
    public void clear() {

    }

    public void createUser(UserData u) throws Exception {

    }

    public UserData getUser(String username, String password) throws Exception {

    }
}
