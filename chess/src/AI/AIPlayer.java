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
        // Thực hiện đánh giá bàn cờ cho AI
        int score = 0;

        // Đánh giá dựa trên giá trị quân cờ
        for (Piece piece : board.getPieces()) {
            if (piece.isRed() == isRed) {
                score += getPieceValue(piece);
            } else {
                score -= getPieceValue(piece);
            }
        }
        return score;
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
