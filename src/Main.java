import javax.swing.*;

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        JFrame frame = new JFrame("Моё приложение");
        MainWindow mainWindow = new MainWindow();

        frame.setContentPane(mainWindow.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // Центрируем окно
        frame.setVisible(true);
    });
}
