package ui;

import model.GameData;

public class PostloginUI {
    public static void help() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "create <NAME> " + EscapeSequences.SET_TEXT_COLOR_GREEN + "- to create a game" + EscapeSequences.RESET_TEXT_COLOR);
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "list " + EscapeSequences.SET_TEXT_COLOR_GREEN + "- to list active games" + EscapeSequences.RESET_TEXT_COLOR);
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK] " + EscapeSequences.SET_TEXT_COLOR_GREEN + "- to join a game as given color" + EscapeSequences.RESET_TEXT_COLOR);
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "observe <ID> " + EscapeSequences.SET_TEXT_COLOR_GREEN + "- to spectate a game" + EscapeSequences.RESET_TEXT_COLOR);
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "logout " + EscapeSequences.SET_TEXT_COLOR_GREEN + "- to logout" + EscapeSequences.RESET_TEXT_COLOR);
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "quit " + EscapeSequences.SET_TEXT_COLOR_GREEN + "- to exit program" + EscapeSequences.RESET_TEXT_COLOR);
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + "help " + EscapeSequences.SET_TEXT_COLOR_GREEN + "- to view available commands" + EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR);
    }

    public static void listGame(int index, GameData game) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_BLUE + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + index + ": " + EscapeSequences.SET_TEXT_COLOR_GREEN + "name: " + game.gameName() + " White: " + game.whiteUsername() + " Black: " + game.blackUsername() + EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR);
    }

    public static void listNoGames() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + "No games were found" + EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR);
    }
}
