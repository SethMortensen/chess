package client;

import chess.ChessBoard;
import model.AuthData;
import ui.GameplayUI;
import ui.PreloginUI;
import ui.PostloginUI;

import java.util.ArrayList;
import java.util.Scanner;

public class ClientMain {
    private static State state = State.PRELOGIN;
    private static final ServerFacade SERVER_FACADE = new ServerFacade(8080);
    private static final ArrayList<GameIdMap> ID_LIST = new ArrayList<>();
    private static int gameCount;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("♕ Welcome to 240 Chess. Type Help to get started.");
        while (!state.equals(State.QUIT)) {
            String input = scanner.nextLine();
            if (state == State.PRELOGIN) {
                handlePreLogin(input);
            } else if (state == State.POSTLOGIN) {

                ListGamesResult response = SERVER_FACADE.listGames();
                if (response == null) {
                    gameCount = 0;
                } else {
                    gameCount = response.games().size();
                    for (int i = 0; i < gameCount; i++) {
                        ID_LIST.add(new GameIdMap(i + 1, response.games().get(i).gameID()));
                    }
                }
                handlePostLogin(input);
            }
        }
        SERVER_FACADE.quit();
    }

    public static void handlePreLogin(String input) {
        if (input.equals("help") || input.equals("Help")) {
            PreloginUI.help();
        } else if (input.equals("quit") || input.equals("Quit")) {
            state = State.QUIT;
        } else if (input.startsWith("register ") || input.startsWith("Register ")) {
            String[] registerArgs = input.split(" ");
            if (registerArgs.length != 4) {
                PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
                return;
            }
            AuthData auth = SERVER_FACADE.register(registerArgs[1], registerArgs[2], registerArgs[3]);
            if (auth != null) {
                state = State.POSTLOGIN;
                System.out.println("Logged in as " + registerArgs[1]);
            }
        } else if (input.startsWith("login ") || input.startsWith("Login ")) {
            String[] loginArgs = input.split(" ");
            if (loginArgs.length != 3) {
                PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
                return;
            }
            AuthData auth = SERVER_FACADE.login(loginArgs[1], loginArgs[2]);
            if (auth != null) {
                state = State.POSTLOGIN;
                System.out.println("Logged in as " + loginArgs[1]);
            }
        } else {
            PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
        }
    }

    public static void handlePostLogin(String input) {
        if (input.equals("help") || input.equals("Help")) {
            PostloginUI.help();
        } else if (input.equals("quit") || input.equals("Quit")) {
            SERVER_FACADE.logout();
            state = State.QUIT;
        } else if (input.equals("logout") || input.equals("Logout")) {
            SERVER_FACADE.logout();
            state = State.PRELOGIN;
        } else if (input.equals("list") || input.equals("List")) {
            ListGamesResult response = SERVER_FACADE.listGames();
            if (response == null) {
                PostloginUI.listNoGames();
            } else {
                gameCount = response.games().size();
                for (int i = 0; i < gameCount; i++) {
                    ID_LIST.add(new GameIdMap(i + 1, response.games().get(i).gameID()));
                    PostloginUI.listGame(i + 1, response.games().get(i));
                }
            }
        } else if (input.startsWith("create ") || input.startsWith("Create ")) {
            String[] createArgs = input.split(" ");
            if (createArgs.length != 2) {
                PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
                return;
            }
            CreateGameResult response = SERVER_FACADE.createGame(createArgs[1]);
            if (response == null) {
                return;
            }
            gameCount++;
            ID_LIST.add(new GameIdMap(gameCount, response.gameID()));
            System.out.printf("Game %s created. Game ID: %d%n", createArgs[1], gameCount);
        } else if (input.startsWith("join ") || input.startsWith("Join ")) {
            String[] joinArgs = input.split(" ");
            if (joinArgs.length != 3) {
                PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
                return;
            }
            try {
                if (Integer.parseInt(joinArgs[1]) > gameCount) {
                    PreloginUI.throwError("Game ID exceeds number of games");
                    return;
                }
            } catch (NumberFormatException e) {
                PreloginUI.throwError("Invalid game ID");
                return;
            }
            if (joinArgs[2].equals("BLACK") || joinArgs[2].equals("WHITE")) {
                int gameID = -1;
                for (GameIdMap idMap : ID_LIST) {
                    if (Integer.toString(idMap.userGameID()).equals(joinArgs[1])) {
                        gameID = idMap.serverGameID();
                    }
                }
                if (gameID == -1) {
                    PreloginUI.throwError("Game not found");
                    return;
                }
                ChessBoard board = SERVER_FACADE.joinGame(gameID, joinArgs[2]);
                if (board == null) {
                    return;
                }
                GameplayUI.drawBoard(board, joinArgs[2]);
            } else {
                PreloginUI.throwError("Invalid command. Please specify BLACK or WHITE; Type Help for a list of valid commands");
            }
        } else if (input.startsWith("observe ") || input.startsWith("Observe ")) {
            String[] observeArgs = input.split(" ");
            if (observeArgs.length != 2) {
                PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
            }
            int gameID = -1;
            for (GameIdMap idMap : ID_LIST) {
                if (String.valueOf(idMap.userGameID()).equals(observeArgs[1])) {
                    gameID = idMap.serverGameID();
                }
            }
            if (gameID == -1) {
                PreloginUI.throwError("Invalid game ID");
            }
            ChessBoard board = SERVER_FACADE.spectateGame(gameID);
            state = State.SPECTATOR;
            GameplayUI.drawBoard(board, "WHITE");
        } else {
            PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
        }
    }
}
