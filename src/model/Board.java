package model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public static final int SIZE = 6;
    public static final int EMPTY = 0; // Trống
    public static final int BLACK = 1; // Quân Đen
    public static final int WHITE = 2; // Quân Trắng
    private int[][] grid;
    //
    public Board() {
        this.grid = new int[SIZE][SIZE];
        // dat quan co trang den o
        this.grid[2][2] = WHITE;
        this.grid[3][3] = WHITE;
        this.grid[2][3] = BLACK;
        this.grid[3][2] = BLACK;
    }

    //
    public Board(Board other) {
        this.grid = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(other.grid[i], 0, this.grid[i], 0, SIZE);
        }
    }

    public int[][] getGrid() {
        return grid;
    }
    // kiem tra duong di
    public boolean isValidMove(int player, int r, int c) {
        // 1. Kiểm tra cơ bản: Trong bàn cờ và ô đó phải trống
        if (r < 0 || r >= SIZE || c < 0 || c >= SIZE) return false;
        if (grid[r][c] != EMPTY) return false;

        int opponent = (player == BLACK) ? WHITE : BLACK;

        // 2. Duyệt 8 hướng xung quanh (Ngang, Dọc, Chéo)
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int x = r + dx[i];
            int y = c + dy[i];
            boolean hasOpponentBetween = false;

            // Đi tiếp theo hướng đó
            while (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
                if (grid[x][y] == opponent) {
                    hasOpponentBetween = true; // Gặp quân địch
                } else if (grid[x][y] == player) {
                    // Gặp quân mình -> Nếu ở giữa có quân địch thì HỢP LỆ
                    if (hasOpponentBetween) return true;
                    else break; // Hai quân mình đứng sát nhau -> Không kẹp được gì
                } else {
                    break; // Gặp ô trống -> Hỏng
                }
                x += dx[i];
                y += dy[i];
            }
        }
        return false; // Không kẹp được quân nào ở cả 8 hướng
    }

    // --- 3. THỰC HIỆN NƯỚC ĐI & LẬT CỜ ---
    public void makeMove(int player, int r, int c) {
        // Đặt quân mới xuống
        grid[r][c] = player;

        int opponent = (player == BLACK) ? WHITE : BLACK;
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        // Duyệt lại 8 hướng để lật cờ
        for (int i = 0; i < 8; i++) {
            int x = r + dx[i];
            int y = c + dy[i];
            List<int[]> disksToFlip = new ArrayList<>();

            while (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
                if (grid[x][y] == opponent) {
                    disksToFlip.add(new int[]{x, y}); // Lưu tọa độ quân địch để chuẩn bị lật
                } else if (grid[x][y] == player) {
                    // Gặp quân mình -> LẬT TẤT CẢ các quân địch đã lưu
                    for (int[] pos : disksToFlip) {
                        grid[pos[0]][pos[1]] = player;
                    }
                    break;
                } else {
                    break; // Gặp ô trống
                }
                x += dx[i];
                y += dy[i];
            }
        }
    }

    // --- 4. HỖ TRỢ AI ---
    // Lấy danh sách tất cả các nước đi hợp lệ
    public List<int[]> getAvailableMoves(int player) {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidMove(player, i, j)) {
                    moves.add(new int[]{i, j});
                }
            }
        }
        return moves;
    }

    // --- 5. TRẠNG THÁI GAME ---
    public boolean isGameOver() {
        // Game over khi cả Đen và Trắng đều không còn nước đi
        return getAvailableMoves(BLACK).isEmpty() && getAvailableMoves(WHITE).isEmpty();
    }

    // Đếm điểm (trả về mảng: index 0 là điểm Đen, index 1 là điểm Trắng)
    public int[] getScore() {
        int blackScore = 0;
        int whiteScore = 0;
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell == BLACK) blackScore++;
                else if (cell == WHITE) whiteScore++;
            }
        }
        return new int[]{blackScore, whiteScore};
    }
}