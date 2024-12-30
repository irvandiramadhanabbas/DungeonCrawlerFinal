package Inputs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TextureInput {
    // Array untuk menyimpan nilai pixel dari tekstur
    public int[] pixels;
    // Ukuran (panjang/sisi) dari tekstur (diasumsikan persegi)
    public final int SIZE;

    // Konstruktor untuk memuat tekstur dari lokasi tertentu
    public TextureInput(String location, int size) {
        this.SIZE = size; // Mengatur ukuran tekstur
        this.pixels = new int[SIZE * SIZE]; // Menginisialisasi array pixel
        load(location); // Memuat tekstur dari lokasi file
    }

    // Metode untuk memuat tekstur ke dalam array pixel
    private void load(String location) {
        try {
            // Membaca file gambar sebagai BufferedImage
            BufferedImage image = ImageIO.read(new File(location));
            int w = image.getWidth(); // Mendapatkan lebar gambar
            int h = image.getHeight(); // Mendapatkan tinggi gambar
            // Menyalin nilai RGB dari gambar ke array pixels
            image.getRGB(0, 0, w, h, pixels, 0, w);
        } catch (IOException e) {
            // Menangani kesalahan jika file tidak ditemukan atau tidak dapat dibaca
            e.printStackTrace();
        }
    }

    public static TextureInput door = new TextureInput("Resource/door.png", 64);
    public static TextureInput lockDoor = new TextureInput("Resource/lock_door.png", 64);
    public static TextureInput wall = new TextureInput("Resource/wall.png", 64);
    public static TextureInput wallTorch = new TextureInput("Resource/wallTorch.png", 64);
}
