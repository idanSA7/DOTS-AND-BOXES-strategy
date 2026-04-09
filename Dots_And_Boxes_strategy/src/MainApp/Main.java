package MainApp;

import GraphicsUI.GameFrame; // ייבוא של החלון מהחבילה הגרפית
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // SwingUtilities מבטיח שהגרפיקה תעלה בצורה בטוחה ב-Thread הנכון
        SwingUtilities.invokeLater(() -> {
            new GameFrame();
        });
    }
}
