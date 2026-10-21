package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    void clear();

    void createAuth(UserData user);

    AuthData getAuth(String authToken);

    void deleteAuth(AuthData auth);
}
