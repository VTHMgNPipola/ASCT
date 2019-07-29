package com.prinjsystems.asct.renderingengine;

import com.prinjsystems.asct.structures.ActionTile;
import com.prinjsystems.asct.structures.GameMap;
import com.prinjsystems.asct.structures.Insulator;
import com.prinjsystems.asct.structures.Layer;
import com.prinjsystems.asct.structures.ThermalConductor;
import com.prinjsystems.asct.structures.Tile;
import com.prinjsystems.asct.structures.conductors.AluminiumConductor;
import com.prinjsystems.asct.structures.conductors.Clock;
import com.prinjsystems.asct.structures.conductors.CopperConductor;
import com.prinjsystems.asct.structures.conductors.Spark;
import com.prinjsystems.asct.structures.conductors.light.RedPixel;
import com.prinjsystems.asct.structures.conductors.semiconductors.NSilicon;
import com.prinjsystems.asct.structures.conductors.semiconductors.PSilicon;
import com.prinjsystems.asct.structures.conductors.semiconductors.PermanentSwitch;
import com.prinjsystems.asct.structures.conductors.semiconductors.ToggleSwitch;
import com.prinjsystems.asct.structures.conductors.semiconductors.Transistor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

public class GameDisplay {
    private JFrame frame;
    private GamePanel panel;
    private Graphics2D graphics;
    private AffineTransform camera, identityTransform;
    private Font font;
    private KeyboardHandler keyboardHandler;

    private GameMap map;

    private boolean paused = false;

    private Tile[] tiles = new Tile[]{new Spark(), new CopperConductor(0, 0), new NSilicon(0, 0),
            new PSilicon(0, 0), new Transistor(0, 0), new ToggleSwitch(0, 0),
            new PermanentSwitch(0, 0), new Clock(0, 0), new RedPixel(0, 0),
            new Insulator(0, 0), new ThermalConductor(0, 0),
            new AluminiumConductor(0, 0)};
    private int currentTile = 0;

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
        font = new Font("Monospaced", Font.PLAIN, 13);
        camera = new AffineTransform();
        identityTransform = new AffineTransform();
        identityTransform.setToIdentity();

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
        keyEvents.put(KeyEvent.VK_SPACE, new JKeyEvent(true) {
            @Override
            public void run() {
                paused = !paused;
            }
        });
        keyEvents.put(KeyEvent.VK_Z, new JKeyEvent(true) {
            @Override
            public void run() {
                if (paused) {
                    tick();
                }
            }
        });
        // RIGHT and DOWN commands will decrease the translateX/translateY, so LEFT and UP need to check if
        // translateX/translateY are lower than 0.
        keyEvents.put(KeyEvent.VK_LEFT, new JKeyEvent(false) {
            @Override
            public void run() {
                if (camera.getTranslateX() < 0) {
                    camera.translate(32 / camera.getScaleX(), 0);
                }
            }
        });
        keyEvents.put(KeyEvent.VK_RIGHT, new JKeyEvent(false) {
            @Override
            public void run() {
                if ((camera.getTranslateX() + 32) / 32 < Layer.LAYER_SIZE) {
                    camera.translate(-32 / camera.getScaleX(), 0);
                }
            }
        });
        keyEvents.put(KeyEvent.VK_UP, new JKeyEvent(false) {
            @Override
            public void run() {
                if (camera.getTranslateY() < 0) {
                    camera.translate(0, 32 / camera.getScaleY());
                }
            }
        });
        keyEvents.put(KeyEvent.VK_DOWN, new JKeyEvent(false) {
            @Override
            public void run() {
                if ((camera.getTranslateY() + 32) / 32 < Layer.LAYER_SIZE) {
                    camera.translate(0, -32 / camera.getScaleY());
                }
            }
        });
        keyboardHandler = new KeyboardHandler(keyEvents, Collections.singletonList(KeyEvent.VK_CONTROL));
        frame.addKeyListener(keyboardHandler);

        Map<Integer, JMouseEvent> mouseEvents = new HashMap<>();
        mouseEvents.put(MouseEvent.BUTTON1, new JMouseEvent(false) {
            @Override
            public void run() {
                int posX = (int) (((getX() - camera.getTranslateX()) / Tile.TILE_SIZE) / camera.getScaleX());
                int posY = (int) (((getY() - camera.getTranslateY()) / Tile.TILE_SIZE) / camera.getScaleY());
                if (posX >= 0 && posX <= Layer.LAYER_SIZE && posY >= 0 && posY <= Layer.LAYER_SIZE) {
                    Layer layer = map.getLayers().get(map.getCurrentLayer());
                    if (tiles[currentTile] instanceof Spark) {
                        if (layer.getTile(posX, posY) instanceof ActionTile) {
                            ((ActionTile) layer.getTile(posX, posY)).trySetPowered(true, null);
                        }
                    } else {
                        if (layer.getTile(posX, posY) == null) {
                            try {
                                layer.addTile(tiles[currentTile].getClass().getDeclaredConstructor(int.class, int.class)
                                        .newInstance(posX, posY));
                            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException
                                    | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        mouseEvents.put(MouseEvent.BUTTON3, new JMouseEvent(false) {
            @Override
            public void run() {
                int posX = (int) (((getX() - camera.getTranslateX()) / Tile.TILE_SIZE) / camera.getScaleX());
                int posY = (int) (((getY() - camera.getTranslateY()) / Tile.TILE_SIZE) / camera.getScaleY());
                if (posX >= 0 && posX <= Layer.LAYER_SIZE && posY >= 0 && posY <= Layer.LAYER_SIZE) {
                    map.getLayers().get(map.getCurrentLayer()).removeTile(posX, posY);
                }
            }
        });
        HashMap<Integer, JMouseEvent> wheelEvents = new HashMap<>();
        wheelEvents.put(MouseHandler.MOUSE_WHEEL_UP, new JMouseEvent(false) {
            @Override
            public void run() {
                if (keyboardHandler.isFlagActive(KeyEvent.VK_CONTROL)) {
                    camera.scale(1.5f, 1.5f);
                } else {
                    currentTile = nextTileIndex();
                }
            }
        });
        wheelEvents.put(MouseHandler.MOUSE_WHEEL_DOWN, new JMouseEvent(false) {
            @Override
            public void run() {
                if (keyboardHandler.isFlagActive(KeyEvent.VK_CONTROL)) {
                    camera.scale(0.5f, 0.5f);
                } else {
                    currentTile = previousTileIndex();
                }
            }
        });
        MouseHandler mouseHandler = new MouseHandler(mouseEvents, wheelEvents);
        frame.addMouseListener(mouseHandler);
        panel.addMouseMotionListener(mouseHandler);
        panel.addMouseWheelListener(mouseHandler);
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
        graphics.setTransform(identityTransform);
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, panel.getWidth(), panel.getHeight());

        // Draw current tile
        graphics.setColor(tiles[currentTile].getColor());
        graphics.fillRect(4, panel.getHeight() - 36, 32, 32);
        graphics.setFont(font);
        graphics.drawString(tiles[currentTile].getName(), 38,
                panel.getHeight() - 32 + graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getAscent());

        // Draw next tile
        graphics.setColor(tiles[nextTileIndex()].getColor());
        graphics.fillRect(38, panel.getHeight() - 12, 8, 8);
        graphics.drawString("NEXT", 38,
                panel.getHeight() - 18 + graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getAscent());

        // Draw previous tile
        graphics.setColor(tiles[previousTileIndex()].getColor());
        graphics.fillRect(graphics.getFontMetrics().stringWidth("NEXT") + 40, panel.getHeight() - 12, 8, 8);
        graphics.drawString("PREVIOUS", graphics.getFontMetrics().stringWidth("NEXT") + 40,
                panel.getHeight() - 18 + graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getAscent());

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

    public boolean isPaused() {
        return paused;
    }

    private int nextTileIndex() {
        if (currentTile + 1 >= tiles.length) {
            return 0;
        }
        return currentTile + 1;
    }

    private int previousTileIndex() {
        if (currentTile == 0) {
            return tiles.length - 1;
        }
        return currentTile - 1;
    }
}
