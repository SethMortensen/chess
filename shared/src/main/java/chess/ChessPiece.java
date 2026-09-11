package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    interface MoveStrategy {
        Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board);
    }

    @Override
    public String toString() {
        return "Color: " + pieceColor + "\nType: " + type;
    }

    static class KnightMoveStrategy implements ChessPiece.MoveStrategy {
        @Override
        public Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board) {
            Collection<ChessMove> moves = new ArrayList<>();
            int[][] offsets = { {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2} };

            for (int[] offset : offsets) {
                ChessPosition target = new ChessPosition(position.getRow() + offset[0], position.getColumn() + offset[1]);
                if (target.getColumn() <= 8 && target.getColumn() >= 1 && target.getRow() <= 8 && target.getRow() >= 1) {
                    if(board.getPiece(target) == null) {
                        moves.add(new ChessMove(position, target, null));
                    } else if (board.getPiece(position).pieceColor != board.getPiece(target).pieceColor) {
                        moves.add(new ChessMove(position, target, null));
                    }
                }
            }
            return moves;
        }
    }

    static class RookMoveStrategy implements ChessPiece.MoveStrategy {
        @Override
        public Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board) {
            Collection<ChessMove> moves = new ArrayList<>();
            int x_offset = 1;
            int y_offset = 1;
            while (x_offset + position.getRow() <= 8 && (board.getPiece(new ChessPosition(position.getRow() + x_offset, position.getColumn())) == null)) {
                moves.add(new ChessMove(position, new ChessPosition(position.getRow() + x_offset, position.getColumn()), null));
                x_offset++;
            }
            if (x_offset + position.getRow() <= 8) {
                if (board.getPiece(new ChessPosition(position.getRow() + x_offset, position.getColumn())) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow() + x_offset, position.getColumn())).pieceColor != board.getPiece(position).pieceColor) {
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() + x_offset, position.getColumn()), null));
                    }
                }
            }
            while (y_offset + position.getColumn() <= 8 && (board.getPiece(new ChessPosition(position.getRow(), position.getColumn() + y_offset)) == null)) {
                moves.add(new ChessMove(position, new ChessPosition(position.getRow(), position.getColumn() + y_offset), null));
                y_offset++;
            }
            if (y_offset + position.getColumn() <= 8) {
                if (board.getPiece(new ChessPosition(position.getRow(), position.getColumn() + y_offset)) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow(), position.getColumn() + y_offset)).pieceColor != board.getPiece(position).pieceColor) {
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow(), position.getColumn() + y_offset), null));
                    }
                }
            }
            x_offset = -1;
            y_offset = -1;
            while (x_offset + position.getRow() >= 1 && (board.getPiece(new ChessPosition(position.getRow() + x_offset, position.getColumn())) == null)) {
                moves.add(new ChessMove(position, new ChessPosition(position.getRow() + x_offset, position.getColumn()), null));
                x_offset--;
            }
            if (x_offset + position.getRow() >= 1) {
                if (board.getPiece(new ChessPosition(position.getRow() + x_offset, position.getColumn())) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow() + x_offset, position.getColumn())).pieceColor != board.getPiece(position).pieceColor) {
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow() + x_offset, position.getColumn()), null));
                    }
                }
            }
            while (y_offset + position.getColumn() >= 1 && (board.getPiece(new ChessPosition(position.getRow(), position.getColumn() + y_offset)) == null)) {
                moves.add(new ChessMove(position, new ChessPosition(position.getRow(), position.getColumn() + y_offset), null));
                y_offset--;
            }
            if (y_offset + position.getColumn() >= 1) {
                if (board.getPiece(new ChessPosition(position.getRow(), position.getColumn() + y_offset)) != null) {
                    if (board.getPiece(new ChessPosition(position.getRow(), position.getColumn() + y_offset)).pieceColor != board.getPiece(position).pieceColor) {
                        moves.add(new ChessMove(position, new ChessPosition(position.getRow(), position.getColumn() + y_offset), null));
                    }
                }
            }
            return moves;
        }
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition).type == ChessPiece.PieceType.KNIGHT) {
            MoveStrategy strategy = new KnightMoveStrategy();
            return strategy.getValidMoves(myPosition, board);
        } else if (board.getPiece(myPosition).type == ChessPiece.PieceType.ROOK) {
            MoveStrategy strategy = new RookMoveStrategy();
            return strategy.getValidMoves(myPosition, board);
        }
        return null;
    }
}
