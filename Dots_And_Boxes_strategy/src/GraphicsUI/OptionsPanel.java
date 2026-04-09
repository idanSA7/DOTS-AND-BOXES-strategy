package GraphicsUI;

import javax.swing.*;
import java.awt.*;

public class OptionsPanel extends JPanel {

    private final GameSettings settings = new GameSettings();

    private final int[] playersOptions = {1, 2};
    private int playersIdx = 1; // default 2 players

    private final String[] diffOptions = {"Easy", "Medium", "Hard"};
    private int diffIdx = 1;

    private final String[] boardOptions = {"3x2", "5x4", "8x6", "11x9"};
    private final int[] boardCols = {3, 5, 8, 11}; 
    private final int[] boardRows = {2, 4, 6, 9};  
    private int boardIdx = 3; // default 11x9

    private final String[] themeOptions = {"Default"};
    private int themeIdx = 0;

    private JLabel playersValue, diffValue, boardValue, themeValue;

    public OptionsPanel(GameFrame frame) {
        setLayout(null);

        JLabel title = new JLabel("OPTIONS", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 52));
        add(title);

        JButton back = smallButton("← Back");
        back.addActionListener(e -> frame.showStart());
        add(back);

        JLabel choosePlayers = sectionLabel("Choose Players");
        playersValue = valueLabel(playersOptions[playersIdx] + " Players");
        JButton pL = arrowButton("<");
        JButton pR = arrowButton(">");
        add(choosePlayers); add(playersValue); add(pL); add(pR);

        JLabel difficulty = sectionLabel("Computer Difficulty");
        diffValue = valueLabel(diffOptions[diffIdx]);
        JButton dL = arrowButton("<");
        JButton dR = arrowButton(">");
        add(difficulty); add(diffValue); add(dL); add(dR);

        JLabel boardSize = sectionLabel("Board Size");
        boardValue = valueLabel(boardOptions[boardIdx]);
        JButton bL = arrowButton("<");
        JButton bR = arrowButton(">");
        add(boardSize); add(boardValue); add(bL); add(bR);

        JLabel theme = sectionLabel("Theme");
        themeValue = valueLabel(themeOptions[themeIdx]);
        JButton tL = arrowButton("<");
        JButton tR = arrowButton(">");
        add(theme); add(themeValue); add(tL); add(tR);

        RoundedButton start = new RoundedButton("Start");
        start.addActionListener(e -> {
            settings.players = playersOptions[playersIdx];
            settings.difficulty = diffOptions[diffIdx];
            settings.theme = themeOptions[themeIdx];

            settings.cols = boardCols[boardIdx];
            settings.rows = boardRows[boardIdx];

            frame.startGame(settings);
        });
        add(start);

        // actions
        pL.addActionListener(e -> {
            playersIdx = (playersIdx - 1 + playersOptions.length) % playersOptions.length;
            playersValue.setText(playersOptions[playersIdx] + " Players");
        });
        pR.addActionListener(e -> {
            playersIdx = (playersIdx + 1) % playersOptions.length;
            playersValue.setText(playersOptions[playersIdx] + " Players");
        });

        dL.addActionListener(e -> {
            diffIdx = (diffIdx - 1 + diffOptions.length) % diffOptions.length;
            diffValue.setText(diffOptions[diffIdx]);
        });
        dR.addActionListener(e -> {
            diffIdx = (diffIdx + 1) % diffOptions.length;
            diffValue.setText(diffOptions[diffIdx]);
        });

        bL.addActionListener(e -> {
            boardIdx = (boardIdx - 1 + boardOptions.length) % boardOptions.length;
            boardValue.setText(boardOptions[boardIdx]);
        });
        bR.addActionListener(e -> {
            boardIdx = (boardIdx + 1) % boardOptions.length;
            boardValue.setText(boardOptions[boardIdx]);
        });

        tL.addActionListener(e -> {
            themeIdx = (themeIdx - 1 + themeOptions.length) % themeOptions.length;
            themeValue.setText(themeOptions[themeIdx]);
        });
        tR.addActionListener(e -> {
            themeIdx = (themeIdx + 1) % themeOptions.length;
            themeValue.setText(themeOptions[themeIdx]);
        });

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentResized(java.awt.event.ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();

                title.setBounds(0, (int)(h*0.05), w, 70);
                back.setBounds(30, 30, 120, 40);

                layoutRow(choosePlayers, playersValue, pL, pR, (int)(h*0.18), w);
                layoutRow(difficulty,   diffValue,    dL, dR, (int)(h*0.38), w);
                layoutRow(boardSize,    boardValue,   bL, bR, (int)(h*0.58), w);
                layoutRow(theme,        themeValue,   tL, tR, (int)(h*0.74), w);

                int bw = Math.min(420, (int)(w*0.55));
                start.setBounds((w - bw)/2, (int)(h*0.86), bw, 80);
            }
        });
    }

    private void layoutRow(JLabel section, JLabel value, JButton left, JButton right, int topY, int w) {
        section.setBounds(0, topY, w, 40);
        value.setBounds(0, topY + 45, w, 50);

        int leftX = (w/2) - 260;
        int rightX = (w/2) + 200;
        left.setBounds(leftX, topY + 40, 60, 60);
        right.setBounds(rightX, topY + 40, 60, 60);
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Arial", Font.BOLD, 30));
        return l;
    }

    private JLabel valueLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Arial", Font.BOLD, 36));
        return l;
    }

    private JButton arrowButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 22));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(0, 165, 165));
        return b;
    }

    private JButton smallButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 14));
        b.setFocusPainted(false);
        return b;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gp = new GradientPaint(0, 0,
                new Color(240, 228, 206),
                0, getHeight(),
                new Color(220, 205, 180));
        g2.setPaint(gp);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(255, 255, 255, 25));
        for (int y = 0; y < getHeight(); y += 18) {
            g2.fillRect(0, y, getWidth(), 6);
        }

        g2.dispose();
    }
}
