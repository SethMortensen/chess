package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn = TeamColor.WHITE;
    private ChessBoard gameBoard = new ChessBoard();

    public ChessGame() {
        gameBoard.resetBoard();
    }

    public ChessGame(ChessGame other) {
        this.teamTurn = other.teamTurn;
        this.gameBoard = new ChessBoard(other.gameBoard);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (gameBoard.getPiece(startPosition) == null) {
            return null;
        }
        ChessGame test = new ChessGame(this);
        ChessPiece piece = test.gameBoard.getPiece(startPosition);
        TeamColor checkTeam = piece.getTeamColor();
        Collection<ChessMove> allMoves = piece.pieceMoves(test.gameBoard, startPosition);
        Collection<ChessMove> moves = new ArrayList<>();
        for (ChessMove move : allMoves) {
            test.gameBoard.addPiece(move.endPosition, piece);
            test.gameBoard.addPiece(move.startPosition, null);
            if (!test.isInCheck(checkTeam)) {
                moves.add(move);
            }
            test.gameBoard = new ChessBoard(gameBoard);
        }
        return moves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (gameBoard.getPiece(move.startPosition) == null) {
            throw new InvalidMoveException("No piece found at given position");
        }
        ChessPiece piece = gameBoard.getPiece(move.startPosition);
        if (piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("It is not your turn!");
        }
        Collection<ChessMove> valid_moves = validMoves(move.startPosition);
        for (ChessMove valid_move : valid_moves) {
            if (valid_move.equals(move)) {
                if (move.promotionPiece == null) {
                    gameBoard.addPiece(move.endPosition, piece);
                } else {
                    gameBoard.addPiece(move.endPosition, new ChessPiece(piece.getTeamColor(), move.promotionPiece));
                }
                gameBoard.addPiece(move.startPosition, null);
                if (teamTurn == TeamColor.WHITE) {
                    teamTurn = TeamColor.BLACK;
                } else {
                    teamTurn = TeamColor.WHITE;
                }
                return;
            }
        }
        throw new InvalidMoveException("Invalid move");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (gameBoard.getPiece(position) != null && gameBoard.getPiece(position).getTeamColor() != teamColor) {
                    Collection<ChessMove> pieceMoves = gameBoard.getPiece(position).pieceMoves(gameBoard, position);
                    for (ChessMove move : pieceMoves) {
                        if (gameBoard.getPiece(move.endPosition) != null && gameBoard.getPiece(move.endPosition).getPieceType() == ChessPiece.PieceType.KING) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean canMove = false;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (gameBoard.getPiece(position) != null && gameBoard.getPiece(position).getTeamColor() == teamColor) {
                    Collection<ChessMove> pieceMoves = validMoves(position);
                    if (!pieceMoves.isEmpty()) {
                        canMove = true;
                        break;
                    }
                }
            }
        }
        return isInCheck(teamColor) && !canMove;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean canMove = false;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                if (gameBoard.getPiece(position) != null && gameBoard.getPiece(position).getTeamColor() == teamColor) {
                    Collection<ChessMove> pieceMoves = validMoves(position);
                    if (!pieceMoves.isEmpty()) {
                        canMove = true;
                        break;
                    }
                }
            }
        }
        return !isInCheck(teamColor) && !canMove;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, gameBoard);
    }
}
