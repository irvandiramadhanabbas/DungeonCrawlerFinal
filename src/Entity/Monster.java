package Entity;

public class Monster {
    // Posisi X dan Y dari monster
    public int x, y;

    // Jumlah kesehatan (HP) monster
    private int hp;

    // Konstruktor untuk membuat monster di koordinat tertentu dengan HP awal 50
    public Monster(int x, int y) {
        this.x = x;
        this.y = y;
        this.hp = 50;
    }

    // Getter untuk posisi X
    public int getX() {
        return x;
    }

    // Getter untuk posisi Y
    public int getY() {
        return y;
    }

    // Getter untuk nilai HP monster
    public int getHp() {
        return hp;
    }

    // Mengurangi HP monster berdasarkan jumlah damage yang diterima
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0; // Pastikan HP tidak kurang dari 0
    }

    // Mengecek apakah monster masih hidup (HP > 0)
    public boolean isAlive() {
        return hp > 0;
    }

    // Menghitung jarak Euclidean dari monster ke pemain
    public double getDistanceToPlayer(double playerX, double playerY) {
        double dx = x + 0.5 - playerX; // Tambahkan 0.5 untuk menyesuaikan posisi tengah tile
        double dy = y + 0.5 - playerY;
        return Math.sqrt(dx * dx + dy * dy); // Rumus jarak Euclidean
    }
}
