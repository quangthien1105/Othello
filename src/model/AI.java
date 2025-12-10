package model;

import java.util.List;

public class AI extends Player {
    private int depth;
    // tao ma tran trong so
    private static final int[][] WEIGHTS = {
            {100, -20, 10, 10, -20, 100}, // Dòng 0
            {-20, -50, -2, -2, -50, -20}, // Dòng 1
            {10, -2, -1, -1, -2, 10}, // Dòng 2
            {10, -2, -1, -1, -2, 10}, // Dòng 3
            {-20, -50, -2, -2, -50, -20}, // Dòng 4
            {100, -20, 10, 10, -20, 100}  // Dòng 5
    };

    @Override
    public int[] getMove(Board board) {
        int[] result = minimax(board, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
        return new int[]{result[1], result[2]};
    }

    private int[] minimax(Board board, int depth, int minValue, int maxValue, boolean b) {
    // thuat toan
        List<Integer[]> validMoves= board.geta
    }

    private int findBestScore(Board board) {
/// 100  -20   10   10  -20  100
///  -20  -50   -2   -2  -50  -20
///   10   -2   -1   -1   -2   10
///   10   -2   -1   -1   -2   10
///  -20  -50   -2   -2  -50  -20
///  100  -20   10   10  -20  100
        return 0;
    }
}
