package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

import java.util.ArrayList;

public class Service {
    static MemoryUserDAO userDAO = new MemoryUserDAO(new ArrayList<>());
    static MemoryAuthDAO authDAO = new MemoryAuthDAO(new ArrayList<>());
    static MemoryGameDAO gameDAO = new MemoryGameDAO(new ArrayList<>());
    public static RegisterResult register(RegisterRequest registerRequest) throws Exception {
        UserData user = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
        userDAO.createUser(user);
        AuthData authData = authDAO.createAuth(user);
        return new RegisterResult(registerRequest.username(), authData.authToken());
    }

    public static void clear() {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }
}
