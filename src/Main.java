import view.MenuGUI;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Tạo màn hình Menu
            MenuGUI menu = new MenuGUI();

            // Hiển thị lên
            menu.setVisible(true);
        });
    }
    }


