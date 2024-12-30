package Core;

import Entity.Monster;
import java.util.ArrayList;
import java.util.Random;

public class MonsterCore {
    private ArrayList<Monster> monsters; // Daftar monster dalam permainan
    private int[][] dungeonMap; // Peta dungeon tempat monster berada
    private Random random; // Generator angka acak untuk spawn monster
    private final int MAX_MONSTER = 6; // Jumlah maksimal monster
    private final int SPAWN_COUNT = 2; // Jumlah monster yang muncul sekaligus

    public MonsterCore(int[][] dungeonMap) {
        this.dungeonMap = dungeonMap;
        this.monsters = new ArrayList<>();
        this.random = new Random();
    }

    // Metode untuk spawn monster saat permainan dimulai
    public void initialSpawn() {
        spawnMonsters(SPAWN_COUNT);
    }

    // Mengecek dan menambah monster jika jumlah kurang dari maksimum
    public void checkAndRespawn() {
        int currentMonsterCount = monsters.size();
        if (currentMonsterCount < MAX_MONSTER) {
            int monstersToSpawn = Math.min(SPAWN_COUNT, MAX_MONSTER - currentMonsterCount);
            spawnMonsters(monstersToSpawn);
        }
    }

    // Metode utama untuk menambahkan monster ke dungeon
    private void spawnMonsters(int amount) {
        int spawnCount = 0;
        while (spawnCount < amount) {
            int x = random.nextInt(dungeonMap[0].length); // Posisi X acak
            int y = random.nextInt(dungeonMap.length); // Posisi Y acak

            // Hanya spawn jika lokasi kosong dan unik
            if (dungeonMap[y][x] == 0 && isUniqueSpawn(x, y)) {
                monsters.add(new Monster(x, y));
                spawnCount++;
            }
        }
    }

    // Mengecek apakah lokasi spawn unik (tidak ada monster lain di lokasi tersebut)
    private boolean isUniqueSpawn(int x, int y) {
        for (Monster monster : monsters) {
            if (monster.getX() == x && monster.getY() == y) {
                return false;
            }
        }
        return true;
    }

    // Menghapus monster yang sudah mati dari daftar
    public void removeDeadMonsters() {
        monsters.removeIf(monster -> !monster.isAlive());
    }

    // Mengembalikan daftar semua monster
    public ArrayList<Monster> getMonsters() {
        return monsters;
    }
}
