package model;

import java.util.List;

public class AI extends Player {
    private int depth;

    private static final int[][] WEIGHTS = {
            {100, -20, 10, 10, -20, 100},
            {-20, -50, -2, -2, -50, -20},
            {10, -2, -1, -1, -2, 10},
            {10, -2, -1, -1, -2, 10},
            {-20, -50, -2, -2, -50, -20},
            {100, -20, 10, 10, -20, 100}
    };

    public AI(String name, int color, int depth) {
        super(name, color);
        this.depth = depth;
    }

    @Override
    public int[] makeMove(Board board) {
        int[] result = minimax(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        return new int[]{result[1], result[2]};
    }

    private int[] minimax(Board board, int currentDepth, int alpha, int beta, boolean maximizingPlayer) {

        // 1. Điều kiện dừng: Hết độ sâu hoặc Game Over
        List<int[]> validMoves = board.getAvailableMoves(maximizingPlayer ? this.color : getOpponentColor());

        if (currentDepth == 0 || board.isGameOver()) {
            // Gọi hàm heuristic để tính điểm
            return new int[]{heuristic(board), -1, -1};
        }

        // Nếu đến lượt mình nhưng không có nước đi (Pass)
        if (validMoves.isEmpty()) {
            // Gọi tiếp đệ quy nhưng giữ nguyên lượt của đối thủ
            return minimax(board, currentDepth - 1, alpha, beta, !maximizingPlayer);
        }

        int bestRow = -1;
        int bestCol = -1;

        if (maximizingPlayer) {
            // ---luot AI---
            int maxEval = Integer.MIN_VALUE;

            for (int[] move : validMoves) {
                Board cloneBoard = new Board(board);
                cloneBoard.makeMove(this.color, move[0], move[1]);

                int eval = minimax(cloneBoard, currentDepth - 1, alpha, beta, false)[0];

                if (eval > maxEval) {
                    maxEval = eval;
                    bestRow = move[0];
                    bestCol = move[1];
                }

                // Cắt tỉa Alpha-Beta
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // Beta Cut-off
                }
            }
            return new int[]{maxEval, bestRow, bestCol};

        } else {
            // ---player---
            int minEval = Integer.MAX_VALUE;
            int oppColor = getOpponentColor();

            for (int[] move : validMoves) {
                Board cloneBoard = new Board(board);
                cloneBoard.makeMove(oppColor, move[0], move[1]);

                int eval = minimax(cloneBoard, currentDepth - 1, alpha, beta, true)[0];

                if (eval < minEval) {
                    minEval = eval;
                    bestRow = move[0];
                    bestCol = move[1];
                }

                // Cắt tỉa Alpha-Beta
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break; // Alpha Cut-off
                }
            }
            return new int[]{minEval, bestRow, bestCol};
        }
    }

    private int heuristic(Board board) {
        int myScore = 0;
        int oppScore = 0;
        int oppColor = getOpponentColor();
        int[][] grid = board.getGrid();

        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                if (grid[i][j] == this.color) {
                    myScore += WEIGHTS[i][j];
                } else if (grid[i][j] == oppColor) {
                    oppScore += WEIGHTS[i][j];
                }
            }
        }

        int myMobility = board.getAvailableMoves(this.color).size();
        int oppMobility = board.getAvailableMoves(oppColor).size();


        myScore += myMobility * 10;
        oppScore += oppMobility * 10;

        return myScore - oppScore;
    }

    private int getOpponentColor() {
        return (this.color == Board.BLACK) ? Board.WHITE : Board.BLACK;
    }
}