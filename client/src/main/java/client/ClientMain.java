package client;

import chess.ChessBoard;
import ui.GameplayUI;
import ui.PreloginUI;
import ui.PostloginUI;
import java.util.Scanner;

public class ClientMain {
    private static State state = State.PRELOGIN;
    private static final ServerFacade facade = new ServerFacade(8080);
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
            boolean success = facade.register(registerArgs[1], registerArgs[2], registerArgs[3]);
            if (success) {
                state = State.POSTLOGIN;
                System.out.println("Logged in as " + registerArgs[1]);
            }
        } else if (input.startsWith("login ") || input.startsWith("Login ")) {
            String[] loginArgs = input.split(" ");
            if (loginArgs.length != 3) {
                PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
                return;
            }
            boolean success = facade.login(loginArgs[1], loginArgs[2]);
            if (success) {
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
            facade.listGames();
        } else if (input.startsWith("create ") || input.startsWith("Create ")) {
            String[] createArgs = input.split(" ");
            if (createArgs.length != 2) {
                PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
                return;
            }
            facade.createGame(createArgs[1]);
        } else if (input.startsWith("join ") || input.startsWith("Join ")) {
            String[] joinArgs = input.split(" ");
            if (joinArgs.length != 3) {
                PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
            }
            if (joinArgs[2].equals("BLACK") || joinArgs[2].equals("WHITE")) {
                ChessBoard board = facade.joinGame(joinArgs[1], joinArgs[2]);
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
            ChessBoard board = facade.spectateGame(observeArgs[1]);
            state = State.SPECTATOR;
            GameplayUI.drawBoard(board, "WHITE");
        } else {
            PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
        }
    }
}
