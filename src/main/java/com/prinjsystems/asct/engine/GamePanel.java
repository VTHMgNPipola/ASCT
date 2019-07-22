package com.prinjsystems.asct.engine;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    public GamePanel() {
        setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(GameData.frameBuffer, 0, 0, getWidth(), getHeight(), null);
    }
}
