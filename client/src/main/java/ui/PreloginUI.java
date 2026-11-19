package ui;

public class PreloginUI {
    public static void help() {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA + EscapeSequences.SET_BG_COLOR_DARK_GREY + "register <USERNAME> <PASSWORD> <EMAIL> " + EscapeSequences.SET_TEXT_COLOR_GREEN + "- to create an account" + EscapeSequences.RESET_TEXT_COLOR);
        System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA + "login <USERNAME> <PASSWORD> " + EscapeSequences.SET_TEXT_COLOR_GREEN + "- to login" + EscapeSequences.RESET_TEXT_COLOR);
        System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA + "quit " + EscapeSequences.SET_TEXT_COLOR_GREEN + "- to exit program" + EscapeSequences.RESET_TEXT_COLOR);
        System.out.println(EscapeSequences.SET_TEXT_COLOR_MAGENTA + "help " + EscapeSequences.SET_TEXT_COLOR_GREEN + "- to view available commands" + EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.RESET_BG_COLOR);
    }

    public static void throwError(String message) {
        System.err.println(EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.SET_BG_COLOR_DARK_GREY + message + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
    }
}
