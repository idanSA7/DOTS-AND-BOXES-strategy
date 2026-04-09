package GraphicsUI;

import Logica.GameLogic;
import Logica.Move;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {
    private static final int VIRTUAL_SIZE = 800;
    private static final int CELL = 60;
    private static final int OFFSET = 100;

    private final GameLogic game;
    private int startRow = -1, startCol = -1;
    private boolean dragging = false;
    private double dragVX = 0, dragVY = 0;

    public interface GameCallbacks {
        void onUpdate();
    }
    private GameCallbacks callbacks;

    public BoardPanel(GameLogic game) {
        this.game = game;
        setBackground(Color.BLACK);

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int[] rc = getDotAtScreen(e.getX(), e.getY());
                if (rc != null) {
                    startRow = rc[0]; startCol = rc[1];
                    dragging = true;
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragging) return;
                double[] v = screenToVirtual(e.getX(), e.getY());
                dragVX = v[0]; dragVY = v[1];
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!dragging) return;
                int[] rc = getDotAtScreen(e.getX(), e.getY());
                if (rc != null) processMove(startRow, startCol, rc[0], rc[1]);
                dragging = false;
                repaint();
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    private void processMove(int r1, int c1, int r2, int c2) {
        Move move = null;
        if (r1 == r2 && Math.abs(c1 - c2) == 1) {
            move = new Move('H', r1, Math.min(c1, c2));
        } else if (c1 == c2 && Math.abs(r1 - r2) == 1) {
            move = new Move('V', Math.min(r1, r2), c1);
        }

        if (move != null && game.playTurn(move)) {
            if (callbacks != null) callbacks.onUpdate();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double scale = Math.min(getWidth(), getHeight()) / (double) VIRTUAL_SIZE;
        g2.translate((getWidth() - VIRTUAL_SIZE * scale) / 2, (getHeight() - VIRTUAL_SIZE * scale) / 2);
        g2.scale(scale, scale);

        // ציור הלוח (Gradient וסגנון)
        g2.setPaint(new GradientPaint(0, 0, new Color(240, 228, 206), 0, VIRTUAL_SIZE, new Color(220, 205, 180)));
        g2.fillRect(0, 0, VIRTUAL_SIZE, VIRTUAL_SIZE);

        // ציור ריבועים סגורים
        char[][] boxes = game.getBoard().getBoxes();
        for (int r = 0; r < game.getBoard().getRows(); r++) {
            for (int c = 0; c < game.getBoard().getCols(); c++) {
                if (boxes[r][c] == ' ') continue;
                g2.setColor(boxes[r][c] == 'X' ? new Color(220, 60, 90, 120) : new Color(60, 140, 220, 120));
                g2.fillRoundRect(OFFSET + c * CELL + 6, OFFSET + r * CELL + 6, CELL - 12, CELL - 12, 12, 12);
            }
        }

        // ציור קווים
        g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(40, 40, 40));
        
        boolean[][] hLines = game.getBoard().getHorizontalLines();
        for(int r=0; r<hLines.length; r++)
            for(int c=0; c<hLines[r].length; c++)
                if(hLines[r][c]) g2.drawLine(OFFSET+c*CELL, OFFSET+r*CELL, OFFSET+(c+1)*CELL, OFFSET+r*CELL);

        boolean[][] vLines = game.getBoard().getVerticalLines();
        for(int r=0; r<vLines.length; r++)
            for(int c=0; c<vLines[r].length; c++)
                if(vLines[r][c]) g2.drawLine(OFFSET+c*CELL, OFFSET+r*CELL, OFFSET+c*CELL, OFFSET+(r+1)*CELL);

        // ציור נקודות
        g2.setColor(Color.WHITE);
        for (int r = 0; r <= game.getBoard().getRows(); r++)
            for (int c = 0; c <= game.getBoard().getCols(); c++)
                g2.fillOval(OFFSET + c * CELL - 6, OFFSET + r * CELL - 6, 12, 12);

        g2.dispose();
    }

    public void setGameCallbacks(GameCallbacks cb) { this.callbacks = cb; }

    private int[] getDotAtScreen(int sx, int sy) {
        double[] v = screenToVirtual(sx, sy);
        for (int r = 0; r <= game.getBoard().getRows(); r++) {
            for (int c = 0; c <= game.getBoard().getCols(); c++) {
                double dx = v[0] - (OFFSET + c * CELL);
                double dy = v[1] - (OFFSET + r * CELL);
                if (dx * dx + dy * dy <= 256) return new int[]{r, c};
            }
        }
        return null;
    }

    private double[] screenToVirtual(int sx, int sy) {
        double scale = Math.min(getWidth(), getHeight()) / (double) VIRTUAL_SIZE;
        double vx = (sx - (getWidth() - VIRTUAL_SIZE * scale) / 2) / scale;
        double vy = (sy - (getHeight() - VIRTUAL_SIZE * scale) / 2) / scale;
        return new double[]{vx, vy};
    }
}