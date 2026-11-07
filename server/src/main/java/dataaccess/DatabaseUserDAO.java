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
    public void clear() throws DataAccessException {
        var conn = getConnection();
        try {
            conn.setCatalog("chess");
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE users")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to clear data");
        }
    }

    public void createUser(UserData u) throws Exception {
        var conn = getConnection();
        try {
            conn.setCatalog("chess");
            try (var preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, u.username());
                preparedStatement.setString(2, u.password());
                preparedStatement.setString(3, u.email());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to create user");
        }
    }

    public UserData getUser(String username, String password) throws Exception {

    }
}
