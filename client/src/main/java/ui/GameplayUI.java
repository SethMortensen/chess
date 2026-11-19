package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class GameplayUI {
    public static String drawPiece(ChessPiece.PieceType type, ChessGame.TeamColor color) {
        switch (type) {
            case ROOK:
                if (color == ChessGame.TeamColor.WHITE) {
                    return EscapeSequences.WHITE_ROOK;
                } else {
                    return EscapeSequences.BLACK_ROOK;
                }
            case KNIGHT:
                if (color == ChessGame.TeamColor.WHITE) {
                    return EscapeSequences.WHITE_KNIGHT;
                } else {
                    return EscapeSequences.BLACK_KNIGHT;
                }
            case BISHOP:
                if (color == ChessGame.TeamColor.WHITE) {
                    return EscapeSequences.WHITE_BISHOP;
                } else {
                    return EscapeSequences.BLACK_BISHOP;
                }
            case QUEEN:
                if (color == ChessGame.TeamColor.WHITE) {
                    return EscapeSequences.WHITE_QUEEN;
                } else {
                    return EscapeSequences.BLACK_QUEEN;
                }
            case KING:
                if (color == ChessGame.TeamColor.WHITE) {
                    return EscapeSequences.WHITE_KING;
                } else {
                    return EscapeSequences.BLACK_KING;
                }
            case PAWN:
                if (color == ChessGame.TeamColor.WHITE) {
                    return EscapeSequences.WHITE_PAWN;
                } else {
                    return EscapeSequences.BLACK_PAWN;
                }
        }
        return EscapeSequences.EMPTY;
    }

    public static void drawBoard(ChessBoard board, String color) {
        System.out.print(EscapeSequences.ERASE_SCREEN);
        if (color.equals("WHITE")) {
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("\u2003 \u2003a\u2003 b\u2003 c\u2003 d\u2003 e\u2003 f\u2003 g\u2003 h\u2003\n");
            String rowStarterColor = EscapeSequences.SET_BG_COLOR_WHITE;
            String squareColor = EscapeSequences.SET_BG_COLOR_WHITE;
            for (int i = 8; i >= 1; i--) {
                System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_RED + "\u2003" + i);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                for (int j = 1; j <= 8; j++) {
                    ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                    if (piece == null) {
                        System.out.print(squareColor + EscapeSequences.EMPTY);
                    } else {
                        System.out.print(squareColor + drawPiece(piece.getPieceType(), piece.getTeamColor()));
                    }
                    if (squareColor.equals(EscapeSequences.SET_BG_COLOR_WHITE)) {
                        squareColor = EscapeSequences.SET_BG_COLOR_BLUE;
                    } else {
                        squareColor = EscapeSequences.SET_BG_COLOR_WHITE;
                    }
                }
                System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_RED + i + "  \n");
                if (rowStarterColor.equals(EscapeSequences.SET_BG_COLOR_WHITE)) {
                    rowStarterColor = EscapeSequences.SET_BG_COLOR_BLUE;
                } else {
                    rowStarterColor = EscapeSequences.SET_BG_COLOR_WHITE;
                }
                squareColor = rowStarterColor;
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("\u2003 \u2003a\u2003 b\u2003 c\u2003 d\u2003 e\u2003 f\u2003 g\u2003 h\u2003\n" + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
        } else if (color.equals("BLACK")) {
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("\u2003 \u2003h\u2003 g\u2003 f\u2003 e\u2003 d\u2003 c\u2003 b\u2003 a\u2003\n");
            String rowStarterColor = EscapeSequences.SET_BG_COLOR_WHITE;
            String squareColor = EscapeSequences.SET_BG_COLOR_WHITE;
            for (int i = 1; i <= 8; i++) {
                System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_RED + "  " + i);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                for (int j = 8; j >= 1; j--) {
                    ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                    if (piece == null) {
                        System.out.print(squareColor + EscapeSequences.EMPTY);
                    } else {
                        System.out.print(squareColor + drawPiece(piece.getPieceType(), piece.getTeamColor()));
                    }
                    if (squareColor.equals(EscapeSequences.SET_BG_COLOR_WHITE)) {
                        squareColor = EscapeSequences.SET_BG_COLOR_BLUE;
                    } else {
                        squareColor = EscapeSequences.SET_BG_COLOR_WHITE;
                    }
                }
                System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_RED + i + "  \n");
                if (rowStarterColor.equals(EscapeSequences.SET_BG_COLOR_WHITE)) {
                    rowStarterColor = EscapeSequences.SET_BG_COLOR_BLUE;
                } else {
                    rowStarterColor = EscapeSequences.SET_BG_COLOR_WHITE;
                }
                squareColor = rowStarterColor;
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.print("\u2003 \u2003h\u2003 g\u2003 f\u2003 e\u2003 d\u2003 c\u2003 b\u2003 a\u2003\n" + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
        } else {
            PreloginUI.throwError("Invalid player color. Type Help for a list of valid commands");
        }
    }
}
