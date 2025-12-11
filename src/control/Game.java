package control;

import model.AI;
import model.Board;
import model.Human;
import model.Player;
import view.BoardGUI;
import view.GameGUI;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class Game extends MouseAdapter {
    private Board model;
    private GameGUI view;
    private JFrame menuFrame; // Để quay lại menu chính

    private Player p1; // Luôn là Human (Đen)
    private Player p2; // Có thể là Human hoặc AI (Trắng)
    private Player currentPlayer;

    private boolean isAiMode;
    private boolean isAiThinking;

    // Constructor: Nhận chế độ chơi (PvP/PvAI) và Menu gốc
    public Game(String mode, JFrame menuFrame) {
        super();
        this.menuFrame = menuFrame;
        this.isAiMode = mode.equals("PvAI");
        this.isAiThinking = false;

        // 1. Khởi tạo Model
        model = new Board();
        p1 = new Human("Bạn (Đen)", Board.BLACK);

        if (isAiMode) {
            p2 = new AI("Máy (Trắng)", Board.WHITE, 6); // Độ khó 6
        } else {
            p2 = new Human("Người chơi 2 (Trắng)", Board.WHITE);
        }
        currentPlayer = p1;
        view = new GameGUI();
        view.addGameListener((MouseListener) this);
        view.addMenuListener(e -> returnToMenu());
        updateView();
        view.setVisible(true);
    }

    //
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (model.isGameOver() || isAiThinking) return;
        // Chỉ xử lý nếu người click vào ô cờ
        Object source = e.getSource();
        if (source instanceof BoardGUI) {
            BoardGUI cell = (BoardGUI) source;
            int r = cell.getRow();
            int c = cell.getCol();

            if (currentPlayer instanceof Human) {
                // 1. Kiểm tra nước đi hợp lệ
                if (model.isValidMove(currentPlayer.getColor(), r, c)) {

                    // 2. Thực hiện nước đi
                    model.makeMove(currentPlayer.getColor(), r, c);

                    // 3. Cập nhật giao diện
                    updateView();
                    switchPlayer();
                } else {
                    // Báo lỗi nếu đi sai
                    System.out.println("Nước đi không hợp lệ!");
                }
            }
        }
    }

    //
    private void switchPlayer() {
        if (model.isGameOver()) {
            handleGameOver();
            return;
        }
        currentPlayer = (currentPlayer == p1) ? p2 : p1;
        if (model.getAvailableMoves(currentPlayer.getColor()).isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    currentPlayer.getName() + " không có nước đi! Phải bỏ lượt.");
            currentPlayer = (currentPlayer == p1) ? p2 : p1;
            if (model.getAvailableMoves(currentPlayer.getColor()).isEmpty()) {
                handleGameOver();
                return;
            }
        }
        updateView();
        if (currentPlayer instanceof AI) {
            triggerAITurn();
        }
    }

    // --- LOGIC AI ---
    private void triggerAITurn() {
        isAiThinking = true;
        view.setStatus("Máy đang suy nghĩ...");

        // Dùng Timer để tạo độ trễ (giả vờ máy đang nghĩ) để giao diện không bị giật
        Timer timer = new Timer(700, evt -> {
            // Máy tính toán nước đi
            int[] move = currentPlayer.makeMove(model);

            if (move[0] != -1) {
                model.makeMove(currentPlayer.getColor(), move[0], move[1]);
            }

            isAiThinking = false;
            updateView();
            switchPlayer();
        });
        timer.setRepeats(false);
        timer.start();
    }


    private void updateView() {

        view.updateBoard(model.getGrid(), model.getScore());

        if (!model.isGameOver()) {
            String status = "Lượt của: " + currentPlayer.getName();
            view.setStatus(status);


            if (currentPlayer instanceof Human) {
                List<int[]> moves = model.getAvailableMoves(currentPlayer.getColor());
                view.highlightPossibleMoves(moves);
            }
        }
    }

    // --- XỬ LÝ KẾT THÚC GAME ---
    private void handleGameOver() {
        updateView();
        int[] scores = model.getScore();
        String msg;
        if (scores[0] > scores[1]) msg = "ĐEN THẮNG!";
        else if (scores[0] < scores[1]) msg = "TRẮNG THẮNG!";
        else msg = "HÒA!";

        view.setStatus(msg); // Hiện kết quả lên thanh trạng thái

        int choice = view.showGameOverDialog(msg);
        if (choice == JOptionPane.YES_OPTION) {
            returnToMenu();
        } else {
            System.exit(0);
        }
    }

    // Quay về Menu chính
    private void returnToMenu() {
        view.dispose(); // Tắt cửa sổ game
        if (menuFrame != null) {
            menuFrame.setVisible(true); // Hiện lại menu cũ
        }
    }
}
