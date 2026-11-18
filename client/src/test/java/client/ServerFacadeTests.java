package client;

import chess.ChessBoard;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade SERVER_FACADE;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        SERVER_FACADE = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @AfterEach
    void clearData() {
        SERVER_FACADE.clear();
    }

    @Test
    public void registerPositiveTest() {
        AuthData auth = SERVER_FACADE.register("username", "password", "email");
        assertNotNull(auth);
        assertTrue(auth.authToken().length() > 10);
        assertEquals("username", auth.username());
    }

    @Test
    public void registerAlreadyTakenTest() {
        SERVER_FACADE.register("username", "password", "email");
        AuthData auth = SERVER_FACADE.register("username", "password", "email");
        assertNull(auth);
    }

    @Test
    public void logoutTest() {
        SERVER_FACADE.register("username", "password", "email");
        SERVER_FACADE.logout();
        assertThrows(NullPointerException.class, SERVER_FACADE::listGames);
    }

    @Test
    public void loginPositiveTest() {
        SERVER_FACADE.register("username", "password", "email");
        SERVER_FACADE.logout();
        AuthData auth = SERVER_FACADE.login("username", "password");
        assertNotNull(auth);
        assertTrue(auth.authToken().length() > 10);
        assertEquals("username", auth.username());
    }

    @Test
    public void loginBadInputTest() {
        AuthData auth = SERVER_FACADE.login("bad", "bad");
        assertNull(auth);
    }

    @Test
    public void createGamePositiveTest() {
        SERVER_FACADE.register("username", "password", "email");
        CreateGameResult result = SERVER_FACADE.createGame("game1");
        assertNotNull(result);
    }

    @Test
    public void createGameNoAuth() {
        assertThrows(NullPointerException.class, () -> SERVER_FACADE.createGame("game1"));
    }

    @Test
    public void listGamesPositiveTest() {
        SERVER_FACADE.register("username", "password", "email");
        SERVER_FACADE.createGame("game1");
        ListGamesResult result = SERVER_FACADE.listGames();
        assertNotNull(result);
    }

    @Test
    public void joinGamePositiveTest() {
        SERVER_FACADE.register("username", "password", "email");
        CreateGameResult result = SERVER_FACADE.createGame("game1");
        ChessBoard board = SERVER_FACADE.joinGame(result.gameID(), "WHITE");
        assertNotNull(board);
    }

    @Test
    public void joinGameBadId() {
        SERVER_FACADE.register("username", "password", "email");
        SERVER_FACADE.createGame("game1");
        ChessBoard board = SERVER_FACADE.joinGame(67, "WHITE");
        assertNull(board);
    }

    @Test
    public void joinGameBadColor() {
        SERVER_FACADE.register("username", "password", "email");
        CreateGameResult result = SERVER_FACADE.createGame("game1");
        ChessBoard board = SERVER_FACADE.joinGame(result.gameID(), "PURPLE");
        assertNull(board);
    }
}
