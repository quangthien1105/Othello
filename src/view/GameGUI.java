package view;

import model.Board;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.awt.event.MouseListener;

public class GameGUI extends JFrame {
    private BoardGUI[][] cells;
    private JLabel statusLabel; // Hiển thị "Lượt của Đen..."
    private JLabel scoreLabel;  // Hiển thị "Đen: 2 - Trắng: 2"
    private JButton btnMenu;

    public GameGUI() {
        setTitle("Othello 6x6 - In Game");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(new Color(44, 62, 80)); // Màu nền tối
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        statusLabel = new JLabel("Lượt của: ĐEN");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        scoreLabel = new JLabel("Đen: 2 - Trắng: 2");
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        scoreLabel.setForeground(Color.LIGHT_GRAY);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        topPanel.add(statusLabel);
        topPanel.add(scoreLabel);

        add(topPanel, BorderLayout.NORTH);

        // ---
        JPanel boardPanel = new JPanel(new GridLayout(6, 6)); // Lưới 6x6
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        cells = new BoardGUI[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {

                cells[i][j] = new BoardGUI(i,j);
                boardPanel.add(cells[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        //
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(44, 62, 80));

        btnMenu = new JButton("Quay về Menu");
        btnMenu.setFocusPainted(false);
        bottomPanel.add(btnMenu);

        add(bottomPanel, BorderLayout.SOUTH);
    }


    public void updateBoard(int[][] grid, int[] scores) {
        // Cập nhật từng ô
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                cells[i][j].setPiece(grid[i][j]);
            }
        }
        // Cập nhật điểm
        if (scores != null) {
            scoreLabel.setText("Đen: " + scores[0] + " - Trắng: " + scores[1]);
        }
    }

    public void highlightPossibleMoves(List<int[]> moves) {
        for (int[] move : moves) {
            int r = move[0];
            int c = move[1];
            cells[r][c].setHint();
        }
    }
    public void setStatus(String message) {
        statusLabel.setText(message);
    }
    public void addGameListener(MouseListener listener) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                cells[i][j].addMouseListener(listener);
            }
        }
    }
    public void addMenuListener(java.awt.event.ActionListener listener) {
        btnMenu.addActionListener(listener);
    }

    public int showGameOverDialog(String message) {
        return JOptionPane.showConfirmDialog(this,
                message + "\nBạn có muốn về Menu chính không?",
                "Kết thúc game",
                JOptionPane.YES_NO_OPTION);
    }
}
