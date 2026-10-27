package dataaccess;

import model.AuthData;
import model.UserData;
import java.util.UUID;
import java.util.ArrayList;

public class MemoryAuthDAO implements AuthDAO {
    ArrayList<AuthData> auths = new ArrayList<>();
    public void clear() {
        auths.clear();
    }

    public AuthData createAuth(UserData user) {

    }

    public AuthData getAuth(String authToken) {

    }

    public void deleteAuth(AuthData auth) {

    }
}
