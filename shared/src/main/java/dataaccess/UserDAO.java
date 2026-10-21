package dataaccess;

import model.UserData;

public interface UserDAO {
    void clear();

    void createUser(UserData u) throws DataAccessException;

    UserData getUser(String username);
}
