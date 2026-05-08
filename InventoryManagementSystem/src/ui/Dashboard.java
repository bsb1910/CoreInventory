package ui;

import javax.swing.*;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Inventory Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new ProductPanel()); // 🔥 add panel here
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Dashboard().setVisible(true);
        });
    }
}