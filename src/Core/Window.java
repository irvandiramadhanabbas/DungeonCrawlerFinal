package Core;

import Entity.Player;
import Inputs.AudioInput;
import Inputs.KeyboardInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame {
    // Peta dungeon:
    // 0 = jalur yang dapat dilewati, 1 = dinding, 2 = kosong, 3 = pintu normal, 4 = pintu yang butuh kunci,
    // 5 = dinding dengan obor, 8 = posisi kunci, 9 = posisi boss
    private int[][] dungeonMap = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 1, 1, 1, 1, 1, 5, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1},
            {5, 0, 9, 0, 4, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1},
            {1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 5, 2, 2, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 1, 1, 0, 1, 2, 2, 1, 0, 1, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 2, 2, 2, 5, 0, 1, 2, 2, 5, 0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 1},
            {1, 2, 2, 2, 1, 1, 0, 1, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 5, 1, 1},
            {1, 2, 1, 1, 1, 0, 0, 0, 1, 5, 1, 0, 1, 1, 5, 1, 1, 0, 1, 1, 0, 0, 0, 1},
            {1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 1, 0, 0, 3, 0, 0, 0, 1},
            {1, 2, 1, 0, 1, 0, 0, 0, 1, 1, 1, 0, 1, 2, 2, 2, 5, 0, 1, 1, 0, 0, 0, 1},
            {1, 2, 5, 0, 1, 1, 5, 1, 1, 1, 1, 0, 1, 1, 2, 2, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 2, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 5, 1, 0, 1, 2, 2, 2, 2, 1},
            {1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 3, 1, 1, 5, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 2, 2, 2, 2, 1},
            {1, 2, 1, 0, 0, 0, 1, 2, 2, 1, 1, 0, 1, 1, 2, 2, 1, 0, 1, 1, 1, 1, 1, 1},
            {1, 2, 5, 0, 0, 0, 1, 1, 5, 1, 1, 0, 1, 2, 2, 2, 1, 0, 1, 1, 0, 0, 0, 1},
            {1, 2, 1, 0, 0, 0, 1, 1, 0, 1, 1, 0, 1, 2, 2, 2, 1, 0, 5, 1, 0, 8, 0, 5},
            {1, 2, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 5, 2, 2, 2, 1, 0, 1, 1, 0, 0, 0, 1},
            {1, 2, 2, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 2, 2, 2, 1, 0, 1, 1, 1, 3, 1, 1},
            {1, 2, 2, 5, 0, 0, 0, 3, 0, 1, 1, 0, 1, 2, 2, 1, 1, 0, 1, 1, 1, 0, 1, 1},
            {1, 2, 2, 1, 0, 0, 0, 1, 0, 1, 1, 0, 1, 5, 1, 1, 0, 0, 0, 1, 1, 0, 1, 1},
            {1, 2, 2, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 1},
            {1, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 1, 1, 1, 1, 1, 1, 1, 5, 1, 1, 1, 1, 1, 1}
    };

    // Deklarasi variabel untuk komponen inti game
    private Render view;
    private TitleScreen titleScreen;
    private GamePanel gamePanel;
    private MonsterCore monsterCore;
    private Player player;
    private KeyboardInput keyboardInput;

    // Konstruktor utama Window
    public Window() {
        setTitle("Fogos"); // Menetapkan judul jendela
        setSize(800, 700); // Mengatur ukuran jendela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Mengatur aksi keluar aplikasi saat ditutup
        setLayout(new BorderLayout()); // Menggunakan layout BorderLayout

        showTitleScreen(); // Menampilkan layar judul pertama kali

        setVisible(true); // Menjadikan jendela terlihat
    }

    // Menampilkan layar judul game
    public void showTitleScreen() {
        TitleScreen titleScreen = new TitleScreen(this);
        setContentPane(titleScreen);
        revalidate(); // Merevalidasi layout
    }

    // Menampilkan cutscene akhir permainan
    public void showEndingCutscene() {
        // Menghentikan musik sebelum cutscene dimulai
        AudioInput.stopMusic();

        JPanel cutscenePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Menampilkan gambar akhir
                ImageIcon icon = new ImageIcon("Resource/EndingImage.jpg");
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        cutscenePanel.setLayout(null);

        // Label untuk menampilkan teks kredit
        JLabel label = new JLabel(
                "<html><div style='text-align: center;'>" +
                        "<h1>Foggos</h1>" +
                        "<p><b>Selamat kamu telah menamatkan game ini!</b><br>" +
                        "Credits:<br>" +
                        "Manager: Rijan Ananta<br>" +
                        "Analyst: Irvandi Ramadhan Abbas<br>" +
                        "Designer: M Qodratul Qudus<br>" +
                        "28/12/2024<br>...</p>" +
                        "</div></html>"
        );
        label.setFont(new Font("Times New Roman", Font.BOLD, 25));
        label.setForeground(Color.WHITE);
        label.setVerticalAlignment(SwingConstants.BOTTOM);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(0, cutscenePanel.getHeight(), cutscenePanel.getWidth(), 600); // Posisi label awal di luar layar
        cutscenePanel.add(label);

        // Timer untuk animasi scroll teks kredit
        Timer scrollTimer = new Timer(20, new ActionListener() {
            private int yPos = cutscenePanel.getHeight(); // Mulai dari bawah layar

            @Override
            public void actionPerformed(ActionEvent e) {
                if (yPos > -label.getHeight()) {
                    yPos -= 2; // Menggeser teks ke atas
                    label.setBounds(0, yPos, getWidth(), 600); // Memperbarui posisi label
                } else {
                    ((Timer) e.getSource()).stop(); // Hentikan timer setelah kredit selesai

                    // Setelah selesai, kembali ke layar utama
                    Timer transitionTimer = new Timer(2000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            AudioInput.stopMusic();
                            showTitleScreen(); // Kembali ke layar judul
                        }
                    });
                    transitionTimer.setRepeats(false); // Timer tidak mengulang
                    transitionTimer.start();
                }
            }
        });
        scrollTimer.start();

        setContentPane(cutscenePanel);
        revalidate();
        repaint();

        // Menambahkan musik latar kemenangan
        AudioInput.playMusic("Resource/EndingMusic.wav");
    }

    // Memulai permainan utama
    public void startGame() {
        AudioInput.stopMusic(); // Menghentikan musik latar
        gamePanel = new GamePanel(); // Inisialisasi panel game
        JPanel gamePanelContainer = new JPanel(new BorderLayout());

        monsterCore = new MonsterCore(dungeonMap); // Inisialisasi logika monster
        monsterCore.initialSpawn(); // Spawn monster pertama

        player = new Player(this, gamePanel, view, dungeonMap, monsterCore); // Inisialisasi pemain
        view = new Render(player, dungeonMap, monsterCore); // Inisialisasi render game
        player.setView(view); // Hubungkan pemain dengan view

        keyboardInput = new KeyboardInput(player); // Inisialisasi input keyboard untuk pemain
        gamePanelContainer.add(view, BorderLayout.CENTER); // Tambahkan view ke panel
        gamePanelContainer.add(gamePanel, BorderLayout.SOUTH); // Tambahkan panel kontrol di bawahnya

        addKeyListener(keyboardInput); // Tambahkan listener keyboard
        setFocusable(true);
        requestFocusInWindow(); // Fokuskan jendela untuk menerima input

        setContentPane(gamePanelContainer);
        revalidate(); // Refresh tampilan
        repaint();
    }
}
