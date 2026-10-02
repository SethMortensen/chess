package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board = new ChessPiece[9][9];
    public ChessBoard() {

    }

    public ChessBoard(ChessBoard other) {
        this.board = new ChessPiece[9][9];
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (other.board[i][j] != null) {
                    this.board[i][j] = new ChessPiece(other.board[i][j]);
                }
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()][position.getColumn()];
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder outputString = new StringBuilder();
        for (int i = 8; i > 0; i--) {
            outputString.append("|");
            for (int j = 8; j > 0; j--) {
                if (board[i][j] == null) {
                    outputString.append(" |");
                } else {
                    if (board[i][j].getPieceType() == ChessPiece.PieceType.KING) {
                        outputString.append("K|");
                    } else if (board[i][j].getPieceType() == ChessPiece.PieceType.QUEEN) {
                        outputString.append("Q|");
                    } else if (board[i][j].getPieceType() == ChessPiece.PieceType.BISHOP) {
                        outputString.append("B|");
                    } else if (board[i][j].getPieceType() == ChessPiece.PieceType.KNIGHT) {
                        outputString.append("N|");
                    } else if (board[i][j].getPieceType() == ChessPiece.PieceType.ROOK) {
                        outputString.append("R|");
                    } else if (board[i][j].getPieceType() == ChessPiece.PieceType.PAWN) {
                        outputString.append("P|");
                    }
                }
            }
            outputString.append("\n");
        }
        return outputString.toString();
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 1; i < 9; i++) {
            for (int j = 3; j < 7; j++) {
                board[i][j] = null;
            }
        }
        board[1][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        board[1][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[1][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board[1][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        board[1][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        board[1][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        board[1][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        board[1][8] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        for (int i = 1; i < 9; i++) {
            board[2][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            board[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
        board[8][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        board[8][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board[8][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board[8][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        board[8][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        board[8][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        board[8][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        board[8][8] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
    }
}
