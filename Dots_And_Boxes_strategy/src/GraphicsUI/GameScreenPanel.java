package GraphicsUI;

import Logica.GameLogic;
import javax.swing.*;
import java.awt.*;

public class GameScreenPanel extends JPanel {
    private final GameFrame frame;
    private final GameSettings settings;
    private final GameLogic gameLogic;
    private final BoardPanel board;

    private final JLabel turnLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel p1ScoreLabel = new JLabel("P1: 0");
    private final JLabel p2ScoreLabel = new JLabel("P2: 0");

    public GameScreenPanel(GameFrame frame, GameSettings settings) {
        this.frame = frame;
        this.settings = settings;
        this.gameLogic = new GameLogic(settings.rows, settings.cols);
        this.board = new BoardPanel(gameLogic);

        setLayout(new BorderLayout());

        // Top Panel - Turn Label
        JPanel top = new JPanel(new BorderLayout());
        top.setPreferredSize(new Dimension(1, 70));
        turnLabel.setFont(new Font("Arial", Font.BOLD, 30));
        top.add(turnLabel, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);

        // Center - The Board
        add(board, BorderLayout.CENTER);

        // Bottom Panel - Scores
        JPanel bottom = new JPanel(new GridLayout(1, 2));
        bottom.setPreferredSize(new Dimension(1, 80));
        styleLabel(p1ScoreLabel);
        styleLabel(p2ScoreLabel);
        bottom.add(p1ScoreLabel);
        bottom.add(p2ScoreLabel);
        add(bottom, BorderLayout.SOUTH);

        // חיבור ה-Callbacks מהלוח לעדכון התצוגה כאן
        board.setGameCallbacks(() -> {
            updateHud();
            if (gameLogic.isGameOver()) {
                handleGameOver();
            }
        });

        updateHud();
    }

    private void updateHud() {
        p1ScoreLabel.setText("Player 1 (X): " + gameLogic.getP1().getScore());
        p2ScoreLabel.setText("Player 2 (O): " + gameLogic.getP2().getScore());
        turnLabel.setText("Current Turn: " + gameLogic.getCurrentPlayer().getSymbol());
        repaint();
    }

    private void handleGameOver() {
        String winner;
        if (gameLogic.getP1().getScore() > gameLogic.getP2().getScore()) winner = "Player 1 Wins!";
        else if (gameLogic.getP2().getScore() > gameLogic.getP1().getScore()) winner = "Player 2 Wins!";
        else winner = "It's a Tie!";
        
        JOptionPane.showMessageDialog(this, "Game Over! " + winner);
        frame.showStart();
    }

    private void styleLabel(JLabel l) {
        l.setFont(new Font("Arial", Font.BOLD, 22));
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
}