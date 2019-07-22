package com.prinjsystems.asct.renderingengine;

import com.prinjsystems.asct.structures.GameMap;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

public class GameDisplay {
    private JFrame frame;
    private GamePanel panel;
    private Graphics2D graphics;

    private GameMap map;

    public GameDisplay(Dimension resolution) {
        frame = new JFrame("Advanced Structure Creation Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        panel = new GamePanel();
        panel.setMinimumSize(resolution);
        panel.setPreferredSize(resolution);
        panel.setMaximumSize(resolution);
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);

        Map<Integer, Runnable> keyEvents = new HashMap<>();
        keyEvents.put(KeyEvent.VK_PERIOD, () -> map.decreaseLayer());
        keyEvents.put(KeyEvent.VK_COMMA, () -> map.increaseLayer());
        KeyboardHandler keyboardHandler = new KeyboardHandler(keyEvents);
        frame.addKeyListener(keyboardHandler);
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
        if (frame.isVisible()) { // If frame is really visible
            GameData.frameBuffer
                    = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
            graphics = GameData.frameBuffer.createGraphics();
        }
    }

    public void render() {
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        map.render(graphics);
        panel.repaint();
    }

    public void tick() {
        map.tick();
    }

    public void destroy() {
        frame.dispose();
    }
}
