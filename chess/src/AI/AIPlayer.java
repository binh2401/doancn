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
    private int maxDepth = 3; // Mặc định là trung bình

    public void setDifficultyLevel(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy":
                maxDepth = 1; // Dễ
                break;
            case "medium":
                maxDepth = 3; // Trung bình
                break;
            case "hard":
                maxDepth = 5; // Khó
                break;
            default:
                maxDepth = 3; // Mặc định là trung bình
        }
    }  // Đặt độ sâu tối đa là 5


    public Move getBestMove(Board board, boolean isRed) {
        return minimax(board, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, isRed).move;
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
        int[][] positionScores = new int[10][9];

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
        } else if (piece instanceof si) {
            positionScores = new int[][] {
                    {0, 5, 10, 15, 20, 15, 10, 5, 0},
                    {5, 10, 15, 20, 30, 20, 15, 10, 5},
                    {10, 15, 20, 25, 35, 25, 20, 15, 10},
                    {15, 20, 25, 30, 40, 30, 25, 20, 15},
                    {20, 30, 35, 40, 50, 40, 35, 30, 20},
                    {20, 30, 35, 40, 50, 40, 35, 30, 20},
                    {15, 20, 25, 30, 40, 30, 25, 20, 15},
                    {10, 15, 20, 25, 35, 25, 20, 15, 10},
                    {5, 10, 15, 20, 30, 20, 15, 10, 5},
                    {0, 5, 10, 15, 20, 15, 10, 5, 0}
            };
        } else if (piece instanceof phao) {
            positionScores = new int[][] {
                    {10, 20, 20, 30, 40, 30, 20, 20, 10},
                    {20, 30, 30, 40, 50, 40, 30, 30, 20},
                    {30, 40, 40, 50, 60, 50, 40, 40, 30},
                    {40, 50, 50, 60, 70, 60, 50, 50, 40},
                    {50, 60, 60, 70, 80, 70, 60, 60, 50},
                    {50, 60, 60, 70, 80, 70, 60, 60, 50},
                    {40, 50, 50, 60, 70, 60, 50, 50, 40},
                    {30, 40, 40, 50, 60, 50, 40, 40, 30},
                    {20, 30, 30, 40, 50, 40, 30, 30, 20},
                    {10, 20, 20, 30, 40, 30, 20, 20, 10}
            };
        }

        int x = piece.getX();
        int y = piece.getY();

        if (x >= 0 && x < positionScores.length && y >= 0 && y < positionScores[0].length) {
            return positionScores[x][y];
        }

        return 0;
    }

    private int getPieceValue(Piece piece) {
        if (piece instanceof King) {
            return 1000;
        } else if (piece instanceof xe) {
            return 500;
        } else if (piece instanceof ma) {
            return 300;
        } else if (piece instanceof si) {
            return 150;
        } else if (piece instanceof phao) {
            return 300;
        } else if (piece instanceof chot) {
            return 200;
        } else {
            return 0;
        }
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
