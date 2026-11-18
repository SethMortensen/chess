package client;

import chess.ChessBoard;
import model.AuthData;
import org.junit.jupiter.api.*;
import server.Server;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static final ServerFacade serverFacade = new ServerFacade(8080);

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @AfterEach
    void clearData() {
        serverFacade.clear();
    }

    @Test
    public void registerPositiveTest() {
        AuthData auth = serverFacade.register("username", "password", "email");
        assertNotNull(auth);
        assertTrue(auth.authToken().length() > 10);
        assertEquals("username", auth.username());
    }

    @Test
    public void registerAlreadyTakenTest() {
        serverFacade.register("username", "password", "email");
        AuthData auth = serverFacade.register("username", "password", "email");
        assertNull(auth);
    }

    @Test
    public void logoutTest() {
        serverFacade.register("username", "password", "email");
        serverFacade.logout();
        assertThrows(NullPointerException.class, serverFacade::listGames);
    }

    @Test
    public void loginPositiveTest() {
        serverFacade.register("username", "password", "email");
        serverFacade.logout();
        AuthData auth = serverFacade.login("username", "password");
        assertNotNull(auth);
        assertTrue(auth.authToken().length() > 10);
        assertEquals("username", auth.username());
    }

    @Test
    public void loginBadInputTest() {
        AuthData auth = serverFacade.login("bad", "bad");
        assertNull(auth);
    }

    @Test
    public void createGamePositiveTest() {
        serverFacade.register("username", "password", "email");
        CreateGameResult result = serverFacade.createGame("game1");
        assertNotNull(result);
    }

    @Test
    public void createGameNoAuth() {
        assertThrows(NullPointerException.class, () -> serverFacade.createGame("game1"));
    }

    @Test
    public void listGamesPositiveTest() {
        serverFacade.register("username", "password", "email");
        serverFacade.createGame("game1");
        ListGamesResult result = serverFacade.listGames();
        assertNotNull(result);
    }

    @Test
    public void joinGamePositiveTest() {
        serverFacade.register("username", "password", "email");
        CreateGameResult result = serverFacade.createGame("game1");
        ChessBoard board = serverFacade.joinGame(result.gameID(), "WHITE");
        assertNotNull(board);
    }

    @Test
    public void joinGameBadId() {
        serverFacade.register("username", "password", "email");
        serverFacade.createGame("game1");
        ChessBoard board = serverFacade.joinGame(67, "WHITE");
        assertNull(board);
    }

    @Test
    public void joinGameBadColor() {
        serverFacade.register("username", "password", "email");
        CreateGameResult result = serverFacade.createGame("game1");
        ChessBoard board = serverFacade.joinGame(result.gameID(), "PURPLE");
        assertNull(board);
    }
}
