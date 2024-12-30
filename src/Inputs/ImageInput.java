package Inputs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageInput {
    // Metode untuk memuat gambar dari file
    public static BufferedImage loadImage(String filename) {
        try {
            // Membaca file gambar dan mengembalikannya sebagai objek BufferedImage
            return ImageIO.read(new File(filename));
        } catch (IOException e) {
            // Jika terjadi kesalahan saat membaca file, kembalikan null
            return null;
        }
    }
}
