package client;

import ui.PreloginUI;
import ui.PostloginUI;
import java.util.Scanner;

public class ClientMain {
    private static State state = State.PRELOGIN;
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
        ServerFacade.quit();
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
            }
            ServerFacade.register(registerArgs[1], registerArgs[2], registerArgs[3]);
            state = State.POSTLOGIN;
            System.out.println("Logged in as " + registerArgs[1]);
        } else if (input.startsWith("login ") || input.startsWith("Login ")) {
            String[] loginArgs = input.split(" ");
            if (loginArgs.length != 3) {
                PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
            }
            ServerFacade.login(loginArgs[1], loginArgs[2]);
            state = State.POSTLOGIN;
            System.out.println("Logged in as " + loginArgs[1]);
        } else {
            PreloginUI.throwError("Invalid command. Type Help for a list of valid commands");
        }
    }

    public static void handlePostLogin(String input) {
        if (input.equals("help") || input.equals("Help")) {
            PostloginUI.help();
        } else if (input.equals("quit") || input.equals("Quit")) {
            state = State.QUIT;
        } else if (input.equals("logout") || input.equals("Logout")) {
            ServerFacade.logout();
            state = State.PRELOGIN;
        } else if (input.equals("list") || input.equals("List")) {
            ServerFacade.listGames();
        }
    }
}
