package com.prinjsystems.asct.renderingengine;

import com.prinjsystems.asct.structures.GameMap;
import com.prinjsystems.asct.structures.Layer;
import com.prinjsystems.asct.structures.Tile;
import com.prinjsystems.asct.structures.conductors.CopperConductor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

public class GameDisplay {
    private JFrame frame;
    private GamePanel panel;
    private Graphics2D graphics;
    private AffineTransform camera;
    private KeyboardHandler keyboardHandler;

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
        camera = new AffineTransform();

        Map<Integer, JKeyEvent> keyEvents = new HashMap<>();
        keyEvents.put(KeyEvent.VK_PERIOD, new JKeyEvent(true) {
            @Override
            public void run() {
                map.decreaseLayer();
            }
        });
        keyEvents.put(KeyEvent.VK_COMMA, new JKeyEvent(true) {
            @Override
            public void run() {
                map.increaseLayer();
            }
        });
        // RIGHT and DOWN commands will decrease the translateX/translateY, so LEFT and UP need to check if
        // translateX/translateY are lower than 0.
        keyEvents.put(KeyEvent.VK_LEFT, new JKeyEvent(false) {
            @Override
            public void run() {
                if (camera.getTranslateX() < 0) {
                    camera.translate(32, 0);
                }
            }
        });
        keyEvents.put(KeyEvent.VK_RIGHT, new JKeyEvent(false) {
            @Override
            public void run() {
                if ((camera.getTranslateX() + 32) / 32 < Layer.LAYER_SIZE) {
                    camera.translate(-32, 0);
                }
            }
        });
        keyEvents.put(KeyEvent.VK_UP, new JKeyEvent(false) {
            @Override
            public void run() {
                if (camera.getTranslateY() < 0) {
                    camera.translate(0, 32);
                }
            }
        });
        keyEvents.put(KeyEvent.VK_DOWN, new JKeyEvent(false) {
            @Override
            public void run() {
                if ((camera.getTranslateY() + 32) / 32 < Layer.LAYER_SIZE) {
                    camera.translate(0, -32);
                }
            }
        });
        keyboardHandler = new KeyboardHandler(keyEvents);
        frame.addKeyListener(keyboardHandler);

        Map<Integer, JMouseEvent> mouseEvents = new HashMap<>();
        mouseEvents.put(MouseEvent.BUTTON1, new JMouseEvent(false) {
            @Override
            public void run() {
                int posX = (int) (getX() - camera.getTranslateX()) / Tile.TILE_SIZE;
                int posY = (int) (getY() - camera.getTranslateY()) / Tile.TILE_SIZE;
                Layer layer = map.getLayers().get(map.getCurrentLayer());
                if (layer.getTile(posX, posY) == null) {
                    layer.addTile(new CopperConductor(posX, posY));
                }
            }
        });
        mouseEvents.put(MouseEvent.BUTTON3, new JMouseEvent(false) {
            @Override
            public void run() {
                map.getLayers().get(map.getCurrentLayer()).removeTile(
                        (int) (getX() - camera.getTranslateX()) / Tile.TILE_SIZE,
                        (int) (getY() - camera.getTranslateY()) / Tile.TILE_SIZE);
            }
        });
        MouseHandler mouseHandler = new MouseHandler(mouseEvents);
        frame.addMouseListener(mouseHandler);
        panel.addMouseMotionListener(mouseHandler);
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
        graphics.setTransform(camera);
        map.render(graphics);
        panel.repaint();
    }

    public void tick() {
        map.tick();
    }

    public void updateInputs() {
        keyboardHandler.tick();
    }

    public void destroy() {
        graphics.dispose();
        frame.dispose();
    }
}
