package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    void clear();

    AuthData createAuth(UserData user);

    AuthData getAuth(String authToken);

    void deleteAuth(AuthData auth);
}
