package Core;

import Entity.Monster;
import Entity.Player;
import Inputs.ImageInput;
import Inputs.TextureInput;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Render extends JPanel {
    private Player player;
    private MonsterCore monsterCore;
    private int[][] dungeonMap;

    // Konstruktor untuk inisialisasi render
    public Render(Player player, int[][] dungeonMap, MonsterCore monsterCore) {
        this.player = player;
        this.dungeonMap = dungeonMap;
        this.monsterCore = monsterCore;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK); // Latar belakang hitam
        renderFloorAndCeiling(g);   // Render lantai dan langit-langit
        View3D(g);                  // Render dinding dalam perspektif 3D
        renderKey(g);               // Render kunci
        renderLadder(g);            // Render tangga
        renderMonsters(g);          // Render monster
    }

    // Metode untuk menggambar dinding dalam perspektif 3D
    private void View3D(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        int halfHeight = height / 2;

        for (int x = 0; x < width; x++) {
            // Hitung posisi kamera pemain
            double cameraX = 2 * x / (double) width - 1;
            double rayDirX = player.getXDir() + player.getXPlane() * cameraX;
            double rayDirY = player.getYDir() + player.getYPlane() * cameraX;

            int mapX = (int) player.getPlayerX();
            int mapY = (int) player.getPlayerY();

            // Hitung jarak antara garis dan sisi dinding
            double sideDistX, sideDistY;
            double deltaDistX = Math.sqrt(1 + (rayDirY * rayDirY) / (rayDirX * rayDirX));
            double deltaDistY = Math.sqrt(1 + (rayDirX * rayDirX) / (rayDirY * rayDirY));
            double perpWallDist;

            int stepX, stepY;
            boolean hit = false;
            int side = 0;

            // Tentukan arah langkah dan hitung jarak awal ke dinding
            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (player.getPlayerX() - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - player.getPlayerX()) * deltaDistX;
            }
            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (player.getPlayerY() - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - player.getPlayerY()) * deltaDistY;
            }

            // Algoritma DDA untuk menghitung dinding yang terkena
            while (!hit) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                // Jika bertemu dinding selain jalan (0), hentikan perulangan
                if (dungeonMap[mapY][mapX] != 0 && dungeonMap[mapY][mapX] != 8 && dungeonMap[mapY][mapX] != 9) hit = true;
            }

            // Hitung jarak ke dinding yang terkena
            if (side == 0)
                perpWallDist = (mapX - player.getPlayerX() + (1 - stepX) / 2) / rayDirX;
            else
                perpWallDist = (mapY - player.getPlayerY() + (1 - stepY) / 2) / rayDirY;

            // Tentukan tinggi garis untuk dinding berdasarkan jarak
            int lineHeight = (int) (height / perpWallDist);
            int drawStart = -lineHeight / 2 + halfHeight;
            int drawEnd = lineHeight / 2 + halfHeight;

            // Pilih tekstur berdasarkan tipe dinding
            TextureInput texture;
            switch (dungeonMap[mapY][mapX]) {
                case 1:
                    texture = TextureInput.wall; // Dinding biasa
                    break;
                case 3:
                    texture = TextureInput.door; // Pintu biasa
                    break;
                case 4:
                    texture = TextureInput.lockDoor; // Pintu terkunci
                    break;
                case 5:
                    texture = TextureInput.wallTorch; // Dinding dengan obor
                    break;
                default:
                    continue;
            }

            // Hitung titik di mana sinar mengenai dinding
            double wallX;
            if (side == 0)
                wallX = player.getPlayerY() + perpWallDist * rayDirY;
            else
                wallX = player.getPlayerX() + perpWallDist * rayDirX;
            wallX -= Math.floor(wallX);

            // Hitung posisi tekstur horizontal
            int texX = (int) (wallX * texture.SIZE);

            // Balik tekstur untuk efek bayangan jika diperlukan
            if (side == 0 && rayDirX > 0) texX = texture.SIZE - texX - 1;
            if (side == 1 && rayDirY < 0) texX = texture.SIZE - texX - 1;

            // Gambar garis vertikal untuk dinding
            for (int y = drawStart; y < drawEnd; y++) {
                int d = (y * 256 - height * 128 + lineHeight * 128);
                int texY = ((d * texture.SIZE) / lineHeight) / 256;
                int color = texture.pixels[texX + texY * texture.SIZE];
                if (side == 1) color = (color >> 1) & 8355711; // Efek bayangan untuk sisi
                g.setColor(new Color(color));
                g.drawLine(x, y, x, y);
            }
        }
    }

    private void renderFloorAndCeiling(Graphics g) {
        int screenWidth = getWidth();  // Ambil lebar layar
        int screenHeight = getHeight();  // Ambil tinggi layar
        int halfHeight = screenHeight / 2;  // Setengah tinggi layar (pemisah lantai dan langit-langit)

        // Load tekstur lantai dan langit-langit dari file
        BufferedImage floor = ImageInput.loadImage("Resource/floor.png");
        BufferedImage ceiling = ImageInput.loadImage("Resource/floor.png");  // Gunakan tekstur yang sama untuk langit-langit

        // Hitung arah sinar untuk rendering perspektif
        double rayDirX0 = player.getXDir() - player.getXPlane();
        double rayDirY0 = player.getYDir() - player.getYPlane();
        double rayDirX1 = player.getXDir() + player.getXPlane();
        double rayDirY1 = player.getYDir() + player.getYPlane();

        // Rendering bagian langit-langit (bagian atas layar)
        for (int y = 0; y < halfHeight; y++) {
            int p = y - halfHeight;  // Hitung jarak relatif dari pusat layar
            double rowDistance = -0.5 * screenHeight / p;  // Jarak baris (negatif karena di atas)

            // Hitung langkah tekstur pada setiap iterasi x
            double ceilingStepX = rowDistance * (rayDirX1 - rayDirX0) / screenWidth;
            double ceilingStepY = rowDistance * (rayDirY1 - rayDirY0) / screenWidth;

            // Posisi awal tekstur
            double ceilingX = player.getPlayerX() + rowDistance * rayDirX0;
            double ceilingY = player.getPlayerY() + rowDistance * rayDirY0;

            // Loop melalui setiap piksel horizontal
            for (int x = 0; x < screenWidth; x++) {
                int cellX = (int) ceilingX;
                int cellY = (int) ceilingY;

                // Hitung koordinat tekstur
                int textureX = (int) (ceiling.getWidth() * (ceilingX - cellX)) & (ceiling.getWidth() - 1);
                int textureY = (int) (ceiling.getHeight() * (ceilingY - cellY)) & (ceiling.getHeight() - 1);

                // Ambil warna dari tekstur
                int color = ceiling.getRGB(textureX, textureY);
                g.setColor(new Color(color));
                g.drawLine(x, y, x, y);  // Gambar satu piksel langit-langit

                // Lanjutkan ke koordinat berikutnya
                ceilingX += ceilingStepX;
                ceilingY += ceilingStepY;
            }
        }

        // Rendering untuk lantai (setengah bawah layar)
        for (int y = halfHeight; y < screenHeight; y++) {
            int p = y - halfHeight; // Posisi relatif terhadap pusat layar
            double rowDistance = 0.5 * screenHeight / p; // Jarak lantai (positif karena di bawah)

            // Hitung langkah per piksel untuk arah X dan Y
            double floorStepX = rowDistance * (rayDirX1 - rayDirX0) / screenWidth;
            double floorStepY = rowDistance * (rayDirY1 - rayDirY0) / screenWidth;

            // Hitung posisi awal di lantai
            double floorX = player.getPlayerX() + rowDistance * rayDirX0;
            double floorY = player.getPlayerY() + rowDistance * rayDirY0;

            for (int x = 0; x < screenWidth; x++) {
                int cellX = (int) floorX;
                int cellY = (int) floorY;

                // Dapatkan koordinat tekstur untuk lantai
                int textureX = (int) (floor.getWidth() * (floorX - cellX)) & (floor.getWidth() - 1);
                int textureY = (int) (floor.getHeight() * (floorY - cellY)) & (floor.getHeight() - 1);

                // Ambil warna dari tekstur
                int color = floor.getRGB(textureX, textureY);
                g.setColor(new Color(color));
                g.drawLine(x, y, x, y); // Gambar satu piksel

                // Lanjutkan ke piksel berikutnya
                floorX += floorStepX;
                floorY += floorStepY;
            }
        }
    }

        // Rendering kunci pada layar
        private void renderKey (Graphics g){
            BufferedImage keySprite = ImageInput.loadImage("Resource/Kunci.png");

            for (int y = 0; y < dungeonMap.length; y++) {
                for (int x = 0; x < dungeonMap[y].length; x++) {
                    if (dungeonMap[y][x] == 8) {  // Kunci ditandai dengan angka 8
                        // Hitung posisi relatif kunci terhadap pemain
                        double spriteX = x + 0.5 - player.getPlayerX();
                        double spriteY = y + 0.5 - player.getPlayerY();

                        // Hitung transformasi untuk tampilan 3D
                        double invDet = 1.0 / (player.getXPlane() * player.getYDir() - player.getXDir() * player.getYPlane());
                        double transformX = invDet * (player.getYDir() * spriteX - player.getXDir() * spriteY);
                        double transformY = invDet * (-player.getYPlane() * spriteX + player.getXPlane() * spriteY);

                        // Cek apakah kunci terlihat (tidak terhalang dinding)
                        if (!hasLineOfSight(player.getPlayerX(), player.getPlayerY(), x, y)) {
                            continue;  // Lewati jika terhalang
                        }

                        int spriteScreenX = (int) ((getWidth() / 2) * (1 + transformX / transformY));

                        // Hitung ukuran kunci di layar (lebih kecil dari tangga)
                        int spriteHeight = Math.abs((int) ((getHeight() / transformY) * 0.4));
                        int drawStartY = -spriteHeight / 2 + getHeight() / 2;
                        int drawEndY = spriteHeight / 2 + getHeight() / 2;

                        int spriteWidth = spriteHeight;  // Rasio 1:1
                        int drawStartX = -spriteWidth / 2 + spriteScreenX;
                        int drawEndX = spriteWidth / 2 + spriteScreenX;

                        // Gambar kunci hanya jika berada dalam tampilan layar
                        if (transformY > 0 && drawStartX >= 0 && drawEndX < getWidth()) {
                            g.drawImage(keySprite, drawStartX, drawStartY, spriteWidth, spriteHeight, null);
                        }
                    }
                }
            }
        }

    private void renderLadder(Graphics g) {
        BufferedImage ladderSprite = ImageInput.loadImage("Resource/ladder.png");

        for (int y = 0; y < dungeonMap.length; y++) {
            for (int x = 0; x < dungeonMap[y].length; x++) {
                if (dungeonMap[y][x] == 9) {  // Tangga ditandai angka 9
                    double spriteX = x + 0.5 - player.getPlayerX();  // Hitung posisi tangga relatif terhadap pemain
                    double spriteY = y + 0.5 - player.getPlayerY();

                    // Hitung transformasi untuk menempatkan sprite dalam ruang 2D
                    double invDet = 1.0 / (player.getXPlane() * player.getYDir() - player.getXDir() * player.getYPlane());
                    double transformX = invDet * (player.getYDir() * spriteX - player.getXDir() * spriteY);
                    double transformY = invDet * (-player.getYPlane() * spriteX + player.getXPlane() * spriteY);

                    // Jika tangga terhalang dinding, jangan dirender
                    if (!hasLineOfSight(player.getPlayerX(), player.getPlayerY(), x, y)) {
                        continue;
                    }

                    int spriteScreenX = (int) ((getWidth() / 2) * (1 + transformX / transformY));

                    // Tangga memiliki rasio 28x128, skala tinggi penuh
                    int spriteHeight = Math.abs((int) ((getHeight() / transformY) * 1));
                    int spriteWidth = (int) (spriteHeight * (28.0 / 128.0));

                    int drawStartY = -spriteHeight / 2 + getHeight() / 2;
                    int drawEndY = spriteHeight / 2 + getHeight() / 2;

                    int drawStartX = -spriteWidth / 2 + spriteScreenX;
                    int drawEndX = spriteWidth / 2 + spriteScreenX;

                    // Render tangga jika terlihat dan dalam batas layar
                    if (transformY > 0 && drawStartX >= 0 && drawEndX < getWidth()) {
                        g.drawImage(ladderSprite, drawStartX, drawStartY, spriteWidth, spriteHeight, null);
                    }
                }
            }
        }
    }

    private void renderMonsters(Graphics g) {
        BufferedImage monsterSprite = ImageInput.loadImage("Resource/monster.png");

        // Urutkan monster berdasarkan jarak dari pemain (terjauh dulu)
        ArrayList<Monster> sortedMonsters = new ArrayList<>(monsterCore.getMonsters());
        sortedMonsters.sort((m1, m2) -> {
            double distance1 = m1.getDistanceToPlayer(player.getPlayerX(), player.getPlayerY());
            double distance2 = m2.getDistanceToPlayer(player.getPlayerX(), player.getPlayerY());
            return Double.compare(distance2, distance1);
        });

        // Render setiap monster
        for (Monster monster : sortedMonsters) {
            double spriteX = monster.getX() + 0.5 - player.getPlayerX();  // Posisi monster relatif terhadap pemain
            double spriteY = monster.getY() + 0.5 - player.getPlayerY();

            // Hitung transformasi untuk sprite monster
            double invDet = 1.0 / (player.getXPlane() * player.getYDir() - player.getXDir() * player.getYPlane());
            double transformX = invDet * (player.getYDir() * spriteX - player.getXDir() * spriteY);
            double transformY = invDet * (-player.getYPlane() * spriteX + player.getXPlane() * spriteY);

            // Jika monster terhalang dinding, jangan dirender
            if (!hasLineOfSight(player.getPlayerX(), player.getPlayerY(), monster.getX(), monster.getY())) {
                continue;
            }

            int spriteScreenX = (int) ((getWidth() / 2) * (1 + transformX / transformY));

            // Skala monster, proporsi 1:1
            int spriteHeight = Math.abs((int) ((getHeight() / transformY) * 0.7));
            int spriteWidth = spriteHeight;

            int drawStartY = -spriteHeight / 2 + getHeight() / 2;
            int drawEndY = spriteHeight / 2 + getHeight() / 2;

            int drawStartX = -spriteWidth / 2 + spriteScreenX;
            int drawEndX = spriteWidth / 2 + spriteScreenX;

            // Render monster jika terlihat dan dalam batas layar
            if (transformY > 0 && drawStartX >= 0 && drawEndX < getWidth()) {
                g.drawImage(monsterSprite, drawStartX, drawStartY, spriteWidth, spriteHeight, null);
            }
        }
    }

    // Fungsi untuk memeriksa apakah target terlihat dari posisi pemain
    private boolean hasLineOfSight(double startX, double startY, int targetX, int targetY) {
        double deltaX = targetX - startX;
        double deltaY = targetY - startY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Iterasi sepanjang garis menuju target untuk memeriksa adanya dinding
        for (double step = 0; step < distance; step += 0.1) {
            int mapX = (int) (startX + (deltaX / distance) * step);
            int mapY = (int) (startY + (deltaY / distance) * step);

            // Jika bertemu dinding (kode 1, 3, 4, atau 5 di peta), target tidak terlihat
            if (dungeonMap[mapY][mapX] == 1 || dungeonMap[mapY][mapX] == 3 || dungeonMap[mapY][mapX] == 4 || dungeonMap[mapY][mapX] == 5) {
                return false;
            }
        }
        return true;
    }
}
