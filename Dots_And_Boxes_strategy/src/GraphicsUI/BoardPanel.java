package GraphicsUI;

import Logica.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoardPanel extends JPanel {
    private static final int VIRTUAL_SIZE = 800;
    private static final int CELL = 60;
    private static final int OFFSET = 100;

    private final GameLogic game;
    private int startR = -1, startC = -1;
    private boolean dragging = false;
    private double currentDragX, currentDragY;

    public interface GameCallbacks { void onUpdate(); }
    private GameCallbacks callbacks;

    public BoardPanel(GameLogic game) {
        this.game = game;
        setBackground(Color.BLACK);
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int[] rc = getDot(e.getX(), e.getY());
                if (rc != null) { 
                    startR = rc[0]; startC = rc[1]; dragging = true;
                    double[] v = toVirt(e.getX(), e.getY());
                    currentDragX = v[0]; currentDragY = v[1];
                    repaint(); 
                }
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragging) return;
                double[] v = toVirt(e.getX(), e.getY());
                currentDragX = v[0]; currentDragY = v[1];
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!dragging) return;
                int[] rc = getDot(e.getX(), e.getY());
                if (rc != null) handleMove(startR, startC, rc[0], rc[1]);
                dragging = false; 
                repaint();
            }
        };
        addMouseListener(ma); addMouseMotionListener(ma);
    }

    private void handleMove(int r1, int c1, int r2, int c2) {
        Move m = null;
        if (r1 == r2 && Math.abs(c1 - c2) == 1) m = new Move('H', r1, Math.min(c1, c2));
        else if (c1 == c2 && Math.abs(r1 - r2) == 1) m = new Move('V', Math.min(r1, r2), c1);
        if (m != null && game.playTurn(m) && callbacks != null) callbacks.onUpdate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        double s = Math.min(getWidth(), getHeight()) / (double) VIRTUAL_SIZE;
        g2.translate((getWidth() - VIRTUAL_SIZE * s) / 2, (getHeight() - VIRTUAL_SIZE * s) / 2);
        g2.scale(s, s);

        g2.setColor(new Color(240, 228, 206)); 
        g2.fillRect(0, 0, VIRTUAL_SIZE, VIRTUAL_SIZE);

        char[][] b = game.getBoard().getBoxes();
        for (int r = 0; r < game.getBoard().getRows(); r++)
            for (int c = 0; c < game.getBoard().getCols(); c++) {
                if (b[r][c] == ' ') continue;
                g2.setColor(b[r][c] == 'X' ? new Color(220, 60, 90, 150) : new Color(60, 140, 220, 150));
                g2.fillRoundRect(OFFSET + c * CELL + 5, OFFSET + r * CELL + 5, 50, 50, 10, 10);
            }

        Move lastMove = game.getBoard().getLastMove();
        boolean[][] hLines = game.getBoard().getHorizontalLines();
        for (int r = 0; r < hLines.length; r++) {
            for (int c = 0; c < hLines[r].length; c++) {
                if (hLines[r][c]) {
                    if (lastMove != null && lastMove.type == 'H' && lastMove.row == r && lastMove.col == c) {
                        g2.setColor(new Color(255, 215, 0));
                        g2.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    } else {
                        g2.setColor(Color.DARK_GRAY);
                        g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    }
                    g2.drawLine(OFFSET + c * CELL, OFFSET + r * CELL, OFFSET + (c + 1) * CELL, OFFSET + r * CELL);
                }
            }
        }

        boolean[][] vLines = game.getBoard().getVerticalLines();
        for (int r = 0; r < vLines.length; r++) {
            for (int c = 0; c < vLines[r].length; c++) {
                if (vLines[r][c]) {
                    if (lastMove != null && lastMove.type == 'V' && lastMove.row == r && lastMove.col == c) {
                        g2.setColor(new Color(255, 215, 0));
                        g2.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    } else {
                        g2.setColor(Color.DARK_GRAY);
                        g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    }
                    g2.drawLine(OFFSET + c * CELL, OFFSET + r * CELL, OFFSET + c * CELL, OFFSET + (r + 1) * CELL);
                }
            }
        }

        if (dragging) {
            g2.setColor(new Color(0, 150, 255, 180));
            g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(OFFSET + startC * CELL, OFFSET + startR * CELL, (int)currentDragX, (int)currentDragY);
        }

        g2.setColor(Color.WHITE);
        for (int r = 0; r <= game.getBoard().getRows(); r++)
            for (int c = 0; c <= game.getBoard().getCols(); c++)
                g2.fillOval(OFFSET + c * CELL - 6, OFFSET + r * CELL - 6, 12, 12);
            
        g2.dispose();
    }

    public void setCallbacks(GameCallbacks cb) { this.callbacks = cb; }
    private int[] getDot(int x, int y) {
        double[] v = toVirt(x, y);
        for (int r = 0; r <= game.getBoard().getRows(); r++)
            for (int c = 0; c <= game.getBoard().getCols(); c++)
                if (Math.pow(v[0] - (OFFSET + c * CELL), 2) + Math.pow(v[1] - (OFFSET + r * CELL), 2) < 256) return new int[]{r, c};
        return null;
    }
    private double[] toVirt(int x, int y) {
        double s = Math.min(getWidth(), getHeight()) / (double) VIRTUAL_SIZE;
        return new double[]{(x - (getWidth() - VIRTUAL_SIZE * s) / 2) / s, (y - (getHeight() - VIRTUAL_SIZE * s) / 2) / s};
    }
}