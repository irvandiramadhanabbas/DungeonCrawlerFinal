package Inputs;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioInput {
    private static Clip clip;
    private static Clip sfx;
    private static boolean isMuted = false; // Status volume (mute/unmute)

    // Metode untuk memutar musik
    public static void playMusic(String fileName) {
        if (isMuted()) return; // Jika suara dimatikan, tidak memutar musik
        try {
            stopMusic();
            // Membaca file audio
            clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(fileName));
            clip.open(inputStream);
            clip.loop(clip.LOOP_CONTINUOUSLY); // Mengatur musik agar terus diulang
            clip.start(); // Mulai memutar musik
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // Tampilkan pesan error jika terjadi kesalahan
        }
    }

    // Metode untuk memutar efek suara (SFX) satu kali
    public static void soundSFX(String fileName) {
        try {
            // Membaca file audio
            AudioInputStream soundInput = AudioSystem.getAudioInputStream(new File(fileName));
            sfx = AudioSystem.getClip();
            sfx.open(soundInput);
            sfx.start(); // Mulai memutar efek suara
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace(); // Tampilkan pesan error jika terjadi kesalahan
        }
    }

    // Metode untuk menghentikan musik
    public static void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop(); // Hentikan pemutaran musik
            clip.close(); // Menutup sumber audio
        }
        clip = null; // Menghapus referensi untuk memastikan tidak ada kebocoran memori
    }

    // Metode untuk menonaktifkan suara (mute)
    public static void disableSound() {
        isMuted = true; // Mengatur status mute menjadi true
        stopMusic(); // Hentikan musik saat suara dimatikan
    }

    // Metode untuk mengaktifkan suara (unmute)
    public static void enableSound() {
        isMuted = false; // Mengatur status mute menjadi false
        // Jika musik sudah dimulai sebelumnya, lanjutkan pemutaran
        if (clip != null && clip.isRunning()) {
            clip.start();
        }
    }

    // Metode untuk mengecek status mute
    public static boolean isMuted() {
        return isMuted; // Mengembalikan status mute
    }
}