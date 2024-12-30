package Core;

import javax.swing.*;

public class MainRun {
    public static void main(String[] args) {
        // Menjalankan aplikasi dengan menggunakan event dispatch thread
        SwingUtilities.invokeLater(Window::new);
    }
}
