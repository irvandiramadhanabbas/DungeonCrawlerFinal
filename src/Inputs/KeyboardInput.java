package Inputs;

import Entity.Player;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyboardInput extends KeyAdapter {
    private Player player;

    public KeyboardInput(Player player) {
        this.player = player;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (player.getInBattle()) {
            // Kontrol mode battle
            switch (key) {
                case KeyEvent.VK_1 -> player.attack(); // Tombol attack
                case KeyEvent.VK_2 -> player.heal();   // Tombol healing
                case KeyEvent.VK_3 -> player.run();    // Tombol run
            }
        } else {
            // Kontrol Navigasi
            switch (key) {
                case KeyEvent.VK_W -> player.moveForward(); // Maju
                case KeyEvent.VK_A -> player.rotateLeft();  // Putar kiri
                case KeyEvent.VK_D -> player.rotateRight(); // Putar kanan
                case KeyEvent.VK_S -> player.rotateBack();  // Putar belakang // Putar kanan
                case KeyEvent.VK_E -> player.interact(); // Interaksi
            }
        }
    }
}
