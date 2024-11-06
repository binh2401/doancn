package AI;

import quanco.King;
import quanco.xe;
import quanco.si;
import quanco.ma;
import quanco.phao;
import quanco.tuong;
import quanco.chot;
import quanco.Piece;

public class AIPlayer {
    private final int MAX_DEPTH = 3;

    public Move getBestMove(Board board, boolean isRed) {
        return minimax(board, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, isRed).move;
    }

    private MoveEvaluation minimax(Board board, int depth, int alpha, int beta, boolean isMaximizing) {
        if (depth == 0 || board.isGameOver()) {
            return new MoveEvaluation(null, evaluateBoard(board, isMaximizing));
        }

        Move bestMove = null;
        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : board.getAllPossibleMoves(isMaximizing)) {
                board.makeMove(move);
                MoveEvaluation eval = minimax(board, depth - 1, alpha, beta, false);
                board.undoMove(move);
                if (eval.value > maxEval) {
                    maxEval = eval.value;
                    bestMove = move;
                }
                alpha = Math.max(alpha, eval.value);
                if (beta <= alpha) {
                    break;
                }
            }
            return new MoveEvaluation(bestMove, maxEval);
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : board.getAllPossibleMoves(isMaximizing)) {
                board.makeMove(move);
                MoveEvaluation eval = minimax(board, depth - 1, alpha, beta, true);
                board.undoMove(move);
                if (eval.value < minEval) {
                    minEval = eval.value;
                    bestMove = move;
                }
                beta = Math.min(beta, eval.value);
                if (beta <= alpha) {
                    break;
                }
            }
            return new MoveEvaluation(bestMove, minEval);
        }
    }

    private int evaluateBoard(Board board, boolean isRed) {
        int score = 0;
        for (Piece piece : board.getPieces()) {
            int pieceValue = getPieceValue(piece);
            score += piece.isRed() == isRed ? pieceValue : -pieceValue;

            // Thêm lợi thế vị trí
            score += getPositionScore(piece);
        }
        return score;
    }

    private int getPositionScore(Piece piece) {
        int[][] positionScores;

        // Đặt các giá trị đánh giá vị trí dựa trên loại quân
        if (piece instanceof King) {
            positionScores = new int[][] {
                    {0, 0, 0, 10, 10, 10, 0, 0, 0},
                    {0, 0, 0, 10, 20, 10, 0, 0, 0},
                    {0, 0, 0, 10, 30, 10, 0, 0, 0},
                    {0, 0, 0, 10, 20, 10, 0, 0, 0},
                    {0, 0, 0, 10, 10, 10, 0, 0, 0}
            };
        } else if (piece instanceof xe) {
            positionScores = new int[][] {
                    {5, 10, 10, 10, 10, 10, 10, 10, 5},
                    {5, 10, 20, 20, 20, 20, 20, 10, 5},
                    {5, 10, 10, 10, 10, 10, 10, 10, 5},
                    {5, 10, 20, 30, 30, 30, 20, 10, 5},
                    {5, 10, 20, 30, 40, 30, 20, 10, 5},
                    {5, 10, 20, 30, 40, 30, 20, 10, 5},
                    {5, 10, 20, 30, 30, 30, 20, 10, 5},
                    {5, 10, 10, 10, 10, 10, 10, 10, 5},
                    {5, 10, 20, 20, 20, 20, 20, 10, 5},
                    {5, 10, 10, 10, 10, 10, 10, 10, 5}
            };
        } else if (piece instanceof ma) {
            positionScores = new int[][] {
                    {0, 4, 8, 12, 16, 12, 8, 4, 0},
                    {4, 8, 12, 16, 20, 16, 12, 8, 4},
                    {8, 12, 16, 20, 24, 20, 16, 12, 8},
                    {12, 16, 20, 24, 28, 24, 20, 16, 12},
                    {16, 20, 24, 28, 32, 28, 24, 20, 16},
                    {16, 20, 24, 28, 32, 28, 24, 20, 16},
                    {12, 16, 20, 24, 28, 24, 20, 16, 12},
                    {8, 12, 16, 20, 24, 20, 16, 12, 8},
                    {4, 8, 12, 16, 20, 16, 12, 8, 4},
                    {0, 4, 8, 12, 16, 12, 8, 4, 0}
            };
        } else if (piece instanceof chot) {
            positionScores = new int[][] {
                    {0, 0, 0, 5, 10, 5, 0, 0, 0},
                    {0, 0, 0, 5, 10, 5, 0, 0, 0},
                    {0, 0, 0, 5, 10, 5, 0, 0, 0},
                    {0, 0, 0, 5, 10, 5, 0, 0, 0},
                    {0, 0, 0, 5, 10, 5, 0, 0, 0},
                    {0, 0, 0, 10, 20, 10, 0, 0, 0},
                    {0, 0, 0, 10, 20, 10, 0, 0, 0},
                    {0, 0, 0, 15, 30, 15, 0, 0, 0},
                    {0, 0, 0, 15, 30, 15, 0, 0, 0},
                    {0, 0, 0, 20, 40, 20, 0, 0, 0}
            };
        } else {
            // Mặc định cho các quân khác hoặc khi chưa xác định
            positionScores = new int[10][9];
        }

        if (piece.getX() >= 0 && piece.getX() < positionScores.length &&
                piece.getY() >= 0 && piece.getY() < positionScores[0].length) {
            return positionScores[piece.getX()][piece.getY()];
        } else {
            // Trường hợp tọa độ không hợp lệ
            return 0; // Hoặc một giá trị mặc định nào đó
        }
    }

    private int getPieceValue(Piece piece) {
        // Đặt giá trị cho từng quân cờ (có thể thay đổi tùy thuộc vào luật chơi)
        if (piece instanceof King) return 1000;
        if (piece instanceof xe) return 50;
        if (piece instanceof ma) return 30;
        if (piece instanceof si) return 30;
        if (piece instanceof tuong) return 20;
        if (piece instanceof phao) return 40;
        if (piece instanceof chot) return 10;
        return 0;
    }

    private class MoveEvaluation {
        Move move;
        int value;

        MoveEvaluation(Move move, int value) {
            this.move = move;
            this.value = value;
        }
    }
}
