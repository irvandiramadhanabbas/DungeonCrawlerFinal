package Entity;

import Core.*;
import Inputs.AudioInput;

import java.util.Random;

public class Player {
    // Koordinat posisi awal pemain
    private double playerX = 11.5, playerY = 12.5;
    // Sudut awal pemain (menghadap arah tertentu)
    private double playerAngle = 0;
    // Kecepatan rotasi pemain
    private final double ROTATION_SPEED = Math.PI / 2;
    // Arah awal pemain dalam sumbu x dan y
    private double xDir = 1, yDir = 0;
    // Plane untuk menentukan perspektif kamera
    private double xPlane = 0, yPlane = -0.7;
    // Health Points (HP) pemain saat ini
    private int hp = 100;
    // HP maksimal pemain
    private int maxHp = 100;
    // Status apakah pemain sedang dalam mode pertempuran
    private boolean inBattle = false;
    // Monster yang sedang dilawan pemain
    private Monster currentMonster;
    // Jumlah potion yang dimiliki pemain
    private int potionCount = 0;
    // Maksimal jumlah potion yang bisa dibawa pemain
    private static final int MAX_POTIONS = 5;
    // Objek layar judul (title screen)
    private TitleScreen titleScreen;
    // Jendela utama game
    private Window gameWindow;
    // Panel utama game untuk tampilan
    private GamePanel gamePanel;
    // Render untuk menggambar ulang tampilan game
    private Render view;
    // Core monster untuk mengelola monster di dalam game
    private MonsterCore monsterCore;
    // Peta dungeon yang digunakan dalam game
    private int[][] dungeonMap;
    // Status apakah pemain memiliki kunci untuk membuka pintu tertentu
    private boolean hasKey = false;

    // Konstruktor untuk menginisialisasi objek Player
    public Player(Window gameWindow, GamePanel gamePanel, Render view, int[][] dungeonMap, MonsterCore monsterCore) {
        this.gameWindow = gameWindow;
        this.gamePanel = gamePanel;
        this.view = view;
        this.dungeonMap = dungeonMap;
        this.monsterCore = monsterCore;
    }

    // Mendapatkan arah pemain pada sumbu x
    public double getXDir() {
        return xDir;
    }

    // Mendapatkan arah pemain pada sumbu y
    public double getYDir() {
        return yDir;
    }

    // Mendapatkan plane kamera pada sumbu x
    public double getXPlane() {
        return xPlane;
    }

    // Mendapatkan plane kamera pada sumbu y
    public double getYPlane() {
        return yPlane;
    }

    // Mendapatkan posisi pemain pada sumbu x
    public double getPlayerX() {
        return playerX;
    }

    // Mendapatkan posisi pemain pada sumbu y
    public double getPlayerY() {
        return playerY;
    }

    // Mendapatkan status apakah pemain sedang dalam mode pertempuran
    public boolean getInBattle() {
        return inBattle;
    }

    // Mengatur objek Render untuk menggambar ulang tampilan game
    public void setView(Render view) {
        this.view = view;
    }

    // Mengurangi HP pemain akibat serangan atau kerusakan
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0; // Pastikan HP tidak negatif
    }

    // Memulihkan HP pemain
    public void heal(int amount) {
        hp += amount;
        if (hp > maxHp) hp = maxHp; // Pastikan HP tidak melebihi maksimal
        gamePanel.addLog("Player memulihkan HP: " + amount + ". HP sekarang: " + hp);
        gamePanel.updateHP(hp); // Perbarui HP di panel game
    }

    // Mengecek apakah pemain masih hidup
    public boolean isAlive() {
        return hp > 0;
    }

    // Metode untuk menggerakkan pemain maju
    public void moveForward() {
        // Hitung posisi berikutnya berdasarkan arah pemain
        double nextX = (playerX + xDir);
        double nextY = (playerY + yDir);

        // Periksa apakah pemain bertemu monster di posisi berikutnya
        for (Monster monster : monsterCore.getMonsters()) {
            if (monster.getX() == (int) nextX && monster.getY() == (int) nextY) {
                startBattle(monster); // Masuk ke mode pertempuran
                return; // Hentikan gerakan
            }
        }
        // Periksa jika jalur tidak terhalang oleh dinding atau objek
        if (dungeonMap[(int) nextY][(int) nextX] == 0) {
            playerX = nextX;
            playerY = nextY;
        }
        AudioInput.soundSFX("Resource/walking_step.wav");
        // Gambar ulang tampilan game
        view.repaint();
    }

    // Metode untuk memutar pemain ke kiri
// Memutar pemain sebesar 90 derajat ke arah kiri
    public void rotateLeft() {
        rotatePlayer(ROTATION_SPEED);
    }

    // Metode untuk memutar pemain ke kanan
// Memutar pemain sebesar 90 derajat ke arah kanan
    public void rotateRight() {
        rotatePlayer(-ROTATION_SPEED);
    }

    // Metode untuk memutar pemain 180 derajat
// Memutar pemain berbalik arah sebesar 180 derajat
    public void rotateBack() {
        rotatePlayer(Math.PI);
    }

    // Metode untuk memutar pemain dengan sudut tertentu
// Parameter: angle - sudut putaran dalam radian
    private void rotatePlayer(double angle) {
        playerAngle += angle;
        // Normalisasi sudut agar selalu berada dalam rentang 0 hingga 2*PI
        playerAngle = (playerAngle + 2 * Math.PI) % (2 * Math.PI);

        // Hitung nilai cosinus dan sinus dari sudut
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        // Update arah gerak pemain
        double oldxDir = xDir;
        xDir = xDir * cos - yDir * sin;
        yDir = oldxDir * sin + yDir * cos;

        // Update plane kamera untuk perspektif
        double oldxPlane = xPlane;
        xPlane = xPlane * cos - yPlane * sin;
        yPlane = oldxPlane * sin + yPlane * cos;

        // Render ulang tampilan
        view.repaint();
    }

    // Metode untuk berinteraksi dengan objek di depan pemain
    public void interact() {
        // Hitung koordinat di depan pemain berdasarkan arah pandang
        double nextX = (playerX + Math.cos(playerAngle));
        double nextY = (playerY + Math.sin(playerAngle));

        // Jika ada kunci di depan pemain
        if (dungeonMap[(int) nextY][(int) nextX] == 8) {
            hasKey = true; // Pemain mengambil kunci
            dungeonMap[(int) nextY][(int) nextX] = 0; // Hapus kunci dari peta
            AudioInput.soundSFX("Resource/key_get.wav"); // Mainkan efek suara
            gamePanel.addLog("Kunci Berhasil Diambil!");
            view.repaint();
        }

        // Jika ada pintu terkunci di depan pemain
        if (dungeonMap[(int) nextY][(int) nextX] == 4) {
            if (hasKey) {
                // Buka pintu jika pemain memiliki kunci
                dungeonMap[(int) nextY][(int) nextX] = 0;
                AudioInput.soundSFX("Resource/key_lock_insert.wav");
                try {
                    Thread.sleep(500); // Jeda untuk efek suara
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                AudioInput.soundSFX("Resource/door_open.wav");
                gamePanel.addLog("Pintu Dibuka!");
            } else {
                // Beri pesan jika pemain tidak memiliki kunci
                gamePanel.addLog("Pintu Terkunci! Cari Kunci Dulu Sana.");
            }
        } else if (dungeonMap[(int) nextY][(int) nextX] == 3) { // Jika pintu tidak terkunci
            dungeonMap[(int) nextY][(int) nextX] = 0;
            AudioInput.soundSFX("Resource/door_open.wav");
            gamePanel.addLog("Pintu Dibuka!");
        }

        // Jika ada tangga (jalan keluar) akhir permainan
        if (dungeonMap[(int) nextY][(int) nextX] == 9) {
            gameWindow.showEndingCutscene();
        }

        view.repaint();
    }

    // Metode untuk memulai pertempuran
    public void startBattle(Monster monster) {
        inBattle = true; // Set status pemain dalam pertempuran
        currentMonster = monster; // Tentukan monster yang dilawan

        System.out.println("Battle Dimulai!");
        gamePanel.addLog("Battle Dimulai!");
    }

    // Metode untuk mengakhiri pertempuran
// Parameter: won - true jika pemain menang, false jika pemain kabur
    public void endBattle(boolean won) {
        inBattle = false; // Set status pemain keluar dari pertempuran
        currentMonster = null; // Reset monster yang dilawan
        if (won) {
            System.out.println("Monster Telah Dikalahkan!");
            gamePanel.addLog("Monster Telah Dikalahkan!");

            // Drop potion secara acak jika menang
            if (potionCount < MAX_POTIONS && new Random().nextBoolean()) {
                potionCount++;
                System.out.println("Monster Drop Sebuah Potion! Total Potion: " + potionCount);
                gamePanel.addLog("Monster Drop Sebuah Potion! Total Potion: " + potionCount);
            }
        } else {
            System.out.println("Anda Kabur, Dasar Pecundang!");
            gamePanel.addLog("Anda Kabur, Dasar Pecundang!");
        }
    }

    // Metode untuk aksi menyerang dalam pertempuran
    public void attack() {
        if (inBattle && currentMonster != null) {
            // Hitung damage yang diberikan pemain
            int playerDamage = new Random().nextInt(16) + 10; // Random 10-25
            currentMonster.takeDamage(playerDamage);
            AudioInput.soundSFX("Resource/player_attack.wav"); // Mainkan efek suara
            System.out.println("Player Memberikan " + playerDamage + " Damage. Monster HP: " + currentMonster.getHp());
            gamePanel.addLog("Player Memberikan " + playerDamage + " Damage. Monster HP: " + currentMonster.getHp());

            // Cek apakah monster masih hidup
            if (!currentMonster.isAlive()) {
                monsterCore.removeDeadMonsters(); // Hapus monster mati
                monsterCore.checkAndRespawn(); // Cek respawn monster baru
                endBattle(true); // Akhiri battle dengan kemenangan
                view.repaint(); // Render ulang
            } else {
                try {
                    Thread.sleep(500); // Jeda untuk efek suara
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                // Monster menyerang balik
                int monsterDamage = new Random().nextInt(11) + 10; // Random 10-20
                takeDamage(monsterDamage);
                AudioInput.soundSFX("Resource/player_taking_damage.wav"); // Mainkan efek suara
                System.out.println("Monster Memberikan " + monsterDamage + " Damage. Player HP: " + hp);
                gamePanel.addLog("Monster Memberikan " + monsterDamage + " Damage. Player HP: " + hp);
                gamePanel.updateHP(hp); // Perbarui tampilan HP pemain

                // Jika pemain kalah
                if (!isAlive()) {
                    System.out.println("Anda Kalah!");
                    gamePanel.addLog("Anda Kalah!");
                    gameWindow.showTitleScreen(); // Kembali ke layar judul
                }
            }
        }
    }

    // Metode untuk aksi healing dalam pertempuran
    public void heal() {
        if (inBattle && potionCount > 0) {
            heal(25); // Tambahkan HP sebanyak 25
            potionCount--; // Kurangi jumlah potion
            AudioInput.soundSFX("Resource/drinking_potion.wav");
            System.out.println("Anda Menggunakan Potion! Potion Tersisa: " + potionCount);
            gamePanel.addLog("Anda Menggunakan Potion! Potion Tersisa:: " + potionCount);

        } else if (potionCount == 0) {
            System.out.println("Potion Anda Habis!");
            gamePanel.addLog("Potion Anda Habis!");
        }
    }

    // Metode untuk aksi kabur dari pertempuran
    public void run() {
        if (inBattle) {
            endBattle(false); // Akhiri pertempuran dengan status kabur
        }
    }
}
