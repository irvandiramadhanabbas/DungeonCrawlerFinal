package Core;

import Inputs.AudioInput;
import Inputs.ImageInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class TitleScreen extends JPanel {
    private Window gameWindow; // Objek window utama untuk game
    private boolean isVolumeOn = true; // Status volume (ON/OFF)

    // Konstruktor untuk mengatur elemen pada title screen
    public TitleScreen(Window gameWindow) {
        this.gameWindow = gameWindow;
        setLayout(new GridBagLayout()); // Menggunakan GridBagLayout untuk menyusun komponen secara terpusat

        // Konfigurasi tata letak dengan GridBagConstraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Posisi kolom
        gbc.gridy = 0; // Posisi baris
        gbc.insets = new Insets(10, 10, 10, 10); // Memberi padding di sekitar komponen
        gbc.anchor = GridBagConstraints.CENTER; // Menyusun komponen ke tengah

        // Tambahkan label judul game
        JLabel titleLabel = new JLabel("FOGGOS", SwingConstants.CENTER);
        titleLabel.setIcon(new ImageIcon("Resource/TitleImage.png")); // Menambahkan ikon/gambar judul
        add(titleLabel, gbc);

        // Tambahkan tombol "Start Game"
        gbc.gridy++; // Geser posisi ke baris berikutnya
        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 20)); // Atur font tombol
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame(); // Panggil metode untuk memulai game
            }
        });
        add(startButton, gbc);

        // Tambahkan tombol untuk mengatur volume (ON/OFF)
        gbc.gridy++;
        JButton toggleVolumeButton = new JButton(isVolumeOn ? "Music : OFF" : "Music : ON"); // Teks tombol sesuai status volume
        toggleVolumeButton.setFont(new Font("Arial", Font.BOLD, 16));
        toggleVolumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isVolumeOn = !isVolumeOn; // Ubah status volume
                toggleVolumeButton.setText(isVolumeOn ? "Music : OFF" : "Music : ON"); // Perbarui teks tombol

                // Mengubah status volume di kelas AudioInput
                if (isVolumeOn) {
                    AudioInput.enableSound(); // Mengaktifkan suara
                } else {
                    AudioInput.disableSound(); // Menonaktifkan suara
                }
            }
        });
        add(toggleVolumeButton, gbc);

        // Tambahkan tombol "About" untuk menampilkan informasi tentang game
        gbc.gridy++;
        JButton aboutButton = new JButton("About");
        aboutButton.setFont(new Font("Arial", Font.BOLD, 16));
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAboutDialog(); // Panggil metode untuk menampilkan dialog "About"
            }
        });
        add(aboutButton, gbc);

        // Atur latar belakang panel menjadi putih secara default
        setBackground(Color.WHITE);
    }

    // Metode untuk memulai game
    public void startGame() {
        gameWindow.startGame(); // Ganti konten panel JFrame dengan game

        // Mulai memutar musik jika volume diaktifkan
        if (isVolumeOn) {
            AudioInput.playMusic("Resource/game_music.wav"); // Path file musik
        }
    }

    // Menampilkan dialog "About" dengan informasi game
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(
                this,
                "Foggos berasal dari kata Fog (kabut, yang memberi nuansa misterius)  \n" +
                        "\"Fogos\" terdengar seperti sesuatu yang terlupakan dan penuh misteri, cocok untuk menggambarkan penjara yang dilupakan dan penuh penderitaan.\n" +
                        "Foggos dibuat oleh 3 orang : \n" +
                        "1. Irvandi Ramadhan Abbas \n" +
                        "2. M Qodratul Qudus \n" +
                        "3. Rijan Ananta \n" +
                        "Game ini bertujuan untuk keluar dari dungeon dengan cara mencari jalan keluar namun banyak monster yang menghalangi. \n" +
                        "Keypad : \n" +
                        "Maju = W \n" +
                        "Putar kiri = A \n" +
                        "Putar Kanan = D \n" +
                        "Putar Belakang = S \n" +
                        "Interaksi = E \n" +
                        "Attack = 1 \n" +
                        "Heal = 2 \n" +
                        "Run = 3 \n",
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Menggambar komponen pada panel, termasuk latar belakang
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Panggil metode superclass
        BufferedImage backgroundImage = ImageInput.loadImage("Resource/bg.png"); // Muat gambar latar belakang

        // Menggambar gambar latar belakang jika ada
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Sesuaikan ukuran gambar dengan panel
        }
    }
}
