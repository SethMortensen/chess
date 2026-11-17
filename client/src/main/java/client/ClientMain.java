package client;

import chess.ChessBoard;
import model.AuthData;
import model.GameData;
import ui.GameplayUI;
import ui.PreloginUI;
import ui.PostloginUI;

import java.util.ArrayList;
import java.util.Scanner;

public class ClientMain {
    private static State state = State.PRELOGIN;
    private static final ServerFacade facade = new ServerFacade(8080);
    private static final ArrayList<gameIDMap> idList = new ArrayList<>();
    private static int gameCount = 0;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("♕ Welcome to 240 Chess. Type Help to get started.");
        while (!state.equals(State.QUIT)) {
            String input = scanner.nextLine();
            if (state == State.PRELOGIN) {
                handlePreLogin(input);
            } else if (state == State.POSTLOGIN) {
                handlePostLogin(input);
            }
        }
        facade.quit();
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
            AuthData auth = facade.register(registerArgs[1], registerArgs[2], registerArgs[3]);
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
            AuthData auth = facade.login(loginArgs[1], loginArgs[2]);
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
            facade.logout();
            state = State.QUIT;
        } else if (input.equals("logout") || input.equals("Logout")) {
            facade.logout();
            state = State.PRELOGIN;
        } else if (input.equals("list") || input.equals("List")) {
            ListGamesResult response = facade.listGames();
            if (response == null) {
                PostloginUI.listNoGames();
                return;
            }
            for (GameData game : response.games()) {
                for (gameIDMap idMap : idList) {
                    if (idMap.serverGameID() == game.gameID()) {
                        PostloginUI.listGame(idMap.userGameID(), game);
                    }
                }
            }
        } else if (input.startsWith("create ") || input.startsWith("Create ")) {
            String[] createArgs = input.split(" ");
            if (createArgs.length != 2) {
                PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
                return;
            }
            CreateGameResult response = facade.createGame(createArgs[1]);
            if (response == null) {
                return;
            }
            gameCount++;
            idList.add(new gameIDMap(gameCount, response.gameID()));
        } else if (input.startsWith("join ") || input.startsWith("Join ")) {
            String[] joinArgs = input.split(" ");
            if (joinArgs.length != 3) {
                PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
                return;
            }
            if (joinArgs[2].equals("BLACK") || joinArgs[2].equals("WHITE")) {
                int gameID = -1;
                for (gameIDMap idMap : idList) {
                    if (String.valueOf(idMap.userGameID()).equals(joinArgs[1])) {
                        gameID = idMap.serverGameID();
                    }
                }
                if (gameID == -1) {
                    PreloginUI.throwError("Invalid game ID");
                }
                ChessBoard board = facade.joinGame(gameID, joinArgs[2]);
                state = State.GAMEPLAY;
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
            for (gameIDMap idMap : idList) {
                if (String.valueOf(idMap.userGameID()).equals(observeArgs[1])) {
                    gameID = idMap.serverGameID();
                }
            }
            if (gameID == -1) {
                PreloginUI.throwError("Invalid game ID");
            }
            ChessBoard board = facade.spectateGame(gameID);
            state = State.SPECTATOR;
            GameplayUI.drawBoard(board, "WHITE");
        } else {
            PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
        }
    }
}
