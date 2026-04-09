package GraphicsUI;

import javax.swing.*;
import java.awt.*;

public class StartPanel extends JPanel {

    public StartPanel(GameFrame frame) {
        setLayout(null);

        JLabel title = new JLabel("Dots & Boxes", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 56));
        add(title);

        RoundedButton play = new RoundedButton("Play Game");
        RoundedButton exit = new RoundedButton("Exit");

        play.addActionListener(e -> frame.showOptions());
        exit.addActionListener(e -> frame.exitApp());

        add(play);
        add(exit);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentResized(java.awt.event.ComponentEvent e) {
                int w = getWidth();
                int h = getHeight();

                title.setBounds(0, (int)(h*0.12), w, 70);

                int bw = Math.min(420, (int)(w*0.55));
                int bh = 80;
                int x = (w - bw) / 2;

                play.setBounds(x, (int)(h*0.45), bw, bh);
                exit.setBounds(x, (int)(h*0.60), bw, bh);
            }
        });
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
