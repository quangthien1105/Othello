package model;

public class Board {
    public static final int SIZE = 6;
    public static final int EMPTY = 0; // Trống
    public static final int BLACK = 1; // Quân Đen
    public static final int WHITE = 2; // Quân Trắng
    private int[][] grid;

    public Board() {
        this.grid = new int[SIZE][SIZE];
        // Đặt 4 quân cờ ban đầu ở tâm: 2 Trắng, 2 Đen
        this.grid[2][2] = WHITE;
        this.grid[3][3] = WHITE;
        this.grid[2][3] = BLACK;
        this.grid[3][2] = BLACK;
    }
    public int[][] getGrid() {
        return grid;
    }
    public Board(Board other) {
        this.grid = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(other.grid[i], 0, this.grid[i], 0, SIZE);
        }
}
}
