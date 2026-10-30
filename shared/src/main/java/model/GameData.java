package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName) {
    private static final ChessGame game = new ChessGame();
}
