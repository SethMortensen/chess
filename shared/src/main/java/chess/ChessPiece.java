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

        default boolean isValidMove(ChessPosition position, ChessPosition target, ChessBoard board) {
            if (target.getColumn() <= 8 && target.getRow() <= 8 && target.getColumn() >= 1 && target.getRow() >= 1) {
                return board.getPiece(target) == null || board.getPiece(target).pieceColor != board.getPiece(position).pieceColor;
            }
            return false;
        }
    }

    @Override
    public String toString() {
        return "Color: " + pieceColor + "\nType: " + type;
    }

    static class KingMoveStrategy implements ChessPiece.MoveStrategy {
        @Override
        public Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board) {
            Collection<ChessMove> moves = new ArrayList<>();
            int[][] offsets = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

            for (int[] offset : offsets) {
                ChessPosition target = new ChessPosition(position.getRow() + offset[0], position.getColumn() + offset[1]);
                if (isValidMove(position, target, board)) {
                    moves.add(new ChessMove(position, target, null));
                }
            }
            return moves;
        }
    }

    static class BishopMoveStrategy implements ChessPiece.MoveStrategy {
        @Override
        public Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board) {
            Collection<ChessMove> moves = new ArrayList<>();
            int[][] offsets = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
            int multiplier = 1;
            for (int[] offset : offsets) {
                ChessPosition target = new ChessPosition(position.getRow() + offset[0], position.getColumn() + offset[1]);
                while (isValidMove(position, target, board)) {
                    moves.add(new ChessMove(position, target, null));
                    if (board.getPiece(target) != null) {
                        break;
                    }
                    multiplier++;
                    target = new ChessPosition(position.getRow() + (offset[0] * multiplier), position.getColumn() + (offset[1] * multiplier));

                }
                multiplier = 1;
            }
            return moves;
        }
    }

    static class KnightMoveStrategy implements ChessPiece.MoveStrategy {
        @Override
        public Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board) {
            Collection<ChessMove> moves = new ArrayList<>();
            int[][] offsets = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

            for (int[] offset : offsets) {
                ChessPosition target = new ChessPosition(position.getRow() + offset[0], position.getColumn() + offset[1]);
                if (isValidMove(position, target, board)) {
                    moves.add(new ChessMove(position, target, null));
                }
            }
            return moves;
        }
    }

    static class RookMoveStrategy implements ChessPiece.MoveStrategy {
        @Override
        public Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board) {
            Collection<ChessMove> moves = new ArrayList<>();
            int[][] offsets = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
            int multiplier = 1;
            for (int[] offset : offsets) {
                ChessPosition target = new ChessPosition(position.getRow() + offset[0], position.getColumn() + offset[1]);
                while (isValidMove(position, target, board)) {
                    moves.add(new ChessMove(position, target, null));
                    if (board.getPiece(target) != null) {
                        break;
                    }
                    multiplier++;
                    target = new ChessPosition(position.getRow() + (offset[0] * multiplier), position.getColumn() + (offset[1] * multiplier));
                }
                multiplier = 1;
            }
            return moves;
        }
    }

    static class PawnMoveStrategy implements ChessPiece.MoveStrategy {
        @Override
        public Collection<ChessMove> getValidMoves(ChessPosition position, ChessBoard board) {
            Collection<ChessMove> moves = new ArrayList<>();
            if (board.getPiece(position).pieceColor == ChessGame.TeamColor.WHITE) {
                ChessPosition target = new ChessPosition(position.getRow() + 1, position.getColumn());
                if (isValidMove(position, target, board) && board.getPiece(target) == null) {
                    if (position.getRow() == 7) {
                        moves.add(new ChessMove(position, target, PieceType.QUEEN));
                        moves.add(new ChessMove(position, target, PieceType.BISHOP));
                        moves.add(new ChessMove(position, target, PieceType.KNIGHT));
                        moves.add(new ChessMove(position, target, PieceType.ROOK));
                    } else {
                        moves.add(new ChessMove(position, target, null));
                        if (position.getRow() == 2) {
                            target = new ChessPosition(position.getRow() + 2, position.getColumn());
                            if (isValidMove(position, target, board) && board.getPiece(target) == null) {
                                moves.add(new ChessMove(position, target, null));
                            }
                        }
                    }
                }
                target = new ChessPosition(position.getRow() + 1, position.getColumn() + 1);
                if (isValidMove(position, target, board) && board.getPiece(target) != null) {
                    if (board.getPiece(target).pieceColor == ChessGame.TeamColor.BLACK) {
                        if (position.getRow() == 7) {
                            moves.add(new ChessMove(position, target, PieceType.QUEEN));
                            moves.add(new ChessMove(position, target, PieceType.BISHOP));
                            moves.add(new ChessMove(position, target, PieceType.KNIGHT));
                            moves.add(new ChessMove(position, target, PieceType.ROOK));
                        } else {
                            moves.add(new ChessMove(position, target, null));
                        }
                    }
                }
                target = new ChessPosition(position.getRow() + 1, position.getColumn() - 1);
                if (isValidMove(position, target, board) && board.getPiece(target) != null) {
                    if (board.getPiece(target).pieceColor == ChessGame.TeamColor.BLACK) {
                        if (position.getRow() == 7) {
                            moves.add(new ChessMove(position, target, PieceType.QUEEN));
                            moves.add(new ChessMove(position, target, PieceType.BISHOP));
                            moves.add(new ChessMove(position, target, PieceType.KNIGHT));
                            moves.add(new ChessMove(position, target, PieceType.ROOK));
                        } else {
                            moves.add(new ChessMove(position, target, null));
                        }
                    }
                }
            } else {
                ChessPosition target = new ChessPosition(position.getRow() - 1, position.getColumn());
                if (isValidMove(position, target, board) && board.getPiece(target) == null) {
                    if (position.getRow() == 2) {
                        moves.add(new ChessMove(position, target, PieceType.QUEEN));
                        moves.add(new ChessMove(position, target, PieceType.BISHOP));
                        moves.add(new ChessMove(position, target, PieceType.KNIGHT));
                        moves.add(new ChessMove(position, target, PieceType.ROOK));
                    } else {
                        moves.add(new ChessMove(position, target, null));
                        if (position.getRow() == 7) {
                            target = new ChessPosition(position.getRow() - 2, position.getColumn());
                            if (isValidMove(position, target, board) && board.getPiece(target) == null) {
                                moves.add(new ChessMove(position, target, null));
                            }
                        }
                    }
                }
                target = new ChessPosition(position.getRow() - 1, position.getColumn() + 1);
                if (isValidMove(position, target, board) && board.getPiece(target) != null) {
                    if (board.getPiece(target).pieceColor == ChessGame.TeamColor.WHITE) {
                        if (position.getRow() == 2) {
                            moves.add(new ChessMove(position, target, PieceType.QUEEN));
                            moves.add(new ChessMove(position, target, PieceType.BISHOP));
                            moves.add(new ChessMove(position, target, PieceType.KNIGHT));
                            moves.add(new ChessMove(position, target, PieceType.ROOK));
                        } else {
                            moves.add(new ChessMove(position, target, null));
                        }
                    }
                }
                target = new ChessPosition(position.getRow() - 1, position.getColumn() - 1);
                if (isValidMove(position, target, board) && board.getPiece(target) != null) {
                    if (board.getPiece(target).pieceColor == ChessGame.TeamColor.WHITE) {
                        if (position.getRow() == 2) {
                            moves.add(new ChessMove(position, target, PieceType.QUEEN));
                            moves.add(new ChessMove(position, target, PieceType.BISHOP));
                            moves.add(new ChessMove(position, target, PieceType.KNIGHT));
                            moves.add(new ChessMove(position, target, PieceType.ROOK));
                        } else {
                            moves.add(new ChessMove(position, target, null));
                        }
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
        if (board.getPiece(myPosition).type == ChessPiece.PieceType.KING) {
            MoveStrategy strategy = new KingMoveStrategy();
            return strategy.getValidMoves(myPosition, board);
        } else if (board.getPiece(myPosition).type == ChessPiece.PieceType.BISHOP) {
            MoveStrategy strategy = new BishopMoveStrategy();
            return strategy.getValidMoves(myPosition, board);
        } else if (board.getPiece(myPosition).type == ChessPiece.PieceType.KNIGHT) {
            MoveStrategy strategy = new KnightMoveStrategy();
            return strategy.getValidMoves(myPosition, board);
        } else if (board.getPiece(myPosition).type == ChessPiece.PieceType.ROOK) {
            MoveStrategy strategy = new RookMoveStrategy();
            return strategy.getValidMoves(myPosition, board);
        } else if (board.getPiece(myPosition).type == ChessPiece.PieceType.PAWN) {
            MoveStrategy strategy = new PawnMoveStrategy();
            return strategy.getValidMoves(myPosition, board);
        }
        return null;
    }
}
