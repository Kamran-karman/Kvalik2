import javax.swing.*;
import java.awt.*;

public class MainWindow {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private MainPage mainPage;

    public MainWindow() {
        mainPage = new MainPage();

        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        mainPanel.add(mainPage.getPanel(), "MainPage");

        cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.show(mainPanel, "MainPage");
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
