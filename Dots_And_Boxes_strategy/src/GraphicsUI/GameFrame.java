package GraphicsUI;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private final CardLayout cl = new CardLayout();
    private final JPanel root = new JPanel(cl);

    private final StartPanel startPanel;
    private final OptionsPanel optionsPanel;

    // יתחלף כל משחק מחדש
    private GameScreenPanel gameScreen;

    public GameFrame() {
        super("Dots & Boxes");

        startPanel = new StartPanel(this);
        optionsPanel = new OptionsPanel(this);

        root.add(startPanel, "START");
        root.add(optionsPanel, "OPTIONS");

        setContentPane(root);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);
        setVisible(true);

        showStart();
    }

    public void showStart() {
        cl.show(root, "START");
    }

    public void showOptions() {
        cl.show(root, "OPTIONS");
    }

    public void startGame(GameSettings settings) {
        if (gameScreen != null) {
            root.remove(gameScreen);
        }
        gameScreen = new GameScreenPanel(this, settings);
        root.add(gameScreen, "GAME");
        cl.show(root, "GAME");
        root.revalidate();
        root.repaint();
    }

    public void exitApp() {
        dispose();
        System.exit(0);
    }

    // אופציה: להתחיל משחק מחדש עם אותן הגדרות
    public void restart(GameSettings settings) {
        startGame(settings);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}
