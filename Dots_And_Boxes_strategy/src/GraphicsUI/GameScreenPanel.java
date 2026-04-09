package GraphicsUI;

import Logica.*;
import javax.swing.*;
import java.awt.*;

public class GameScreenPanel extends JPanel {
    private final GameFrame frame;
    private final GameSettings settings;
    private final GameLogic gameLogic;
    private final BoardPanel board;
    private final JLabel infoLabel = new JLabel("", SwingConstants.CENTER);

    public GameScreenPanel(GameFrame frame, GameSettings settings) {
        this.frame = frame;
        this.settings = settings;
        
        boolean vsBot = (settings.players == 1);
        this.gameLogic = new GameLogic(settings.rows, settings.cols, vsBot, settings.difficulty);
        this.board = new BoardPanel(gameLogic);

        setLayout(new BorderLayout());
        infoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(infoLabel, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);

        board.setCallbacks(() -> {
            refreshHUD();
            checkBot();
        });

        refreshHUD();
    }

    private void refreshHUD() {
        infoLabel.setText("P1: " + gameLogic.getP1Score() + " | P2: " + gameLogic.getP2Score() + 
                         " | Turn: " + gameLogic.getCurrentPlayer().getSymbol());
        repaint();
    }

    private void checkBot() {
        if (gameLogic.isGameOver()) { 
            JOptionPane.showMessageDialog(this, "Game Over!"); 
            return; 
        }
        
        if (gameLogic.isVsComputer() && gameLogic.getCurrentPlayer() == gameLogic.getP2()) {
            Timer t = new Timer(600, e -> {
                BotLogic bot = new BotLogic(gameLogic.getBoard(), gameLogic.getDifficulty());
                Move m = bot.makeMove();
                if (m != null) {
                    gameLogic.playTurn(m);
                    refreshHUD();
                    checkBot();
                }
            });
            t.setRepeats(false);
            t.start();
        }
    }
}