package Core;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private JTextArea logArea; // Area teks untuk menampilkan log permainan
    private JProgressBar hpBar; // Bar progres untuk menampilkan HP pemain

    public GamePanel() {
        // Mengatur layout panel
        setLayout(new BorderLayout());

        // Menambahkan panel untuk HP dan log permainan
        add(createHPPanel(), BorderLayout.WEST);
        add(createLogPanel(), BorderLayout.CENTER);
    }

    // Membuat panel untuk menampilkan log permainan
    private JPanel createLogPanel() {
        JPanel logPanel = new JPanel(new BorderLayout());

        logArea = new JTextArea(10, 30); // Area teks dengan ukuran awal 10x30
        logArea.setEditable(false); // Tidak dapat diedit oleh pengguna
        logArea.setFocusable(false); // Tidak dapat difokuskan oleh pengguna

        JScrollPane scrollPane = new JScrollPane(logArea); // Menambahkan scroll jika log terlalu panjang
        logPanel.add(scrollPane, BorderLayout.CENTER);
        return logPanel;
    }

    // Metode untuk menambahkan pesan ke area log
    public void addLog(String message) {
        logArea.append(message + "\n"); // Menambahkan pesan baru dengan baris baru
        logArea.setCaretPosition(logArea.getDocument().getLength()); // Otomatis scroll ke bagian bawah
    }

    // Membuat panel untuk menampilkan HP pemain
    private JPanel createHPPanel() {
        JPanel hpPanel = new JPanel();
        hpPanel.setLayout(new BorderLayout());

        hpBar = new JProgressBar(0, 100); // Bar HP dengan rentang 0 hingga 100
        hpBar.setValue(100); // Nilai awal HP
        hpBar.setStringPainted(true); // Menampilkan angka di atas bar
        hpBar.setForeground(Color.RED); // Warna bar HP merah
        hpBar.setPreferredSize(new Dimension(250, 25)); // Ukuran bar

        JLabel text = new JLabel("<html>E = Interaksi<br>WASD = Jalan<br>1 = Attack<br>2 = Heal<br>3 = Run</html>", SwingConstants.CENTER);
        text.setHorizontalAlignment(SwingConstants.CENTER); // Pusatkan teks
        text.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 14)); // Font dan ukuran teks
        text.setForeground(Color.BLACK); // Warna teks

        hpPanel.add(hpBar, BorderLayout.NORTH); // Menambahkan HP bar di bagian atas
        hpPanel.add(text, BorderLayout.CENTER); // Menambahkan petunjuk kontrol di bagian tengah
        return hpPanel;
    }

    // Memperbarui nilai HP di bar HP
    public void updateHP(int hp) {
        hpBar.setValue(hp); // Menyetel nilai HP baru
    }
}