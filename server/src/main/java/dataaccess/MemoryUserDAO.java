package dataaccess;

import model.UserData;
import java.util.ArrayList;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO {
    ArrayList<UserData> users;
    public void clear() {
        users.clear();
    }

    public void createUser(UserData u) throws DataAccessException {
        if (users.contains(u)) {
            throw new DataAccessException("User already exists");
        }
        for (UserData user : users) {
            if (Objects.equals(user.username(), u.username())) {
                throw new DataAccessException("Username already taken");
            }
            if (Objects.equals(user.email(), u.email())) {
                throw new DataAccessException("Email already in use");
            }
        }
        users.add(u);
    }

    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : users) {
            if (Objects.equals(user.username(), username)) {
                return user;
            }
        }
        throw new DataAccessException("User not found");
    }

}
