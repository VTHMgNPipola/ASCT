package com.prinjsystems.asct.renderingengine;

import com.prinjsystems.asct.structures.ActionTile;
import com.prinjsystems.asct.structures.GameMap;
import com.prinjsystems.asct.structures.Insulator;
import com.prinjsystems.asct.structures.Layer;
import com.prinjsystems.asct.structures.ThermalConductor;
import com.prinjsystems.asct.structures.Tile;
import com.prinjsystems.asct.structures.conductors.AluminiumConductor;
import com.prinjsystems.asct.structures.conductors.Clock;
import com.prinjsystems.asct.structures.conductors.ConductorTile;
import com.prinjsystems.asct.structures.conductors.CopperConductor;
import com.prinjsystems.asct.structures.conductors.Spark;
import com.prinjsystems.asct.structures.conductors.coloredwires.BlueWire;
import com.prinjsystems.asct.structures.conductors.coloredwires.BrownWire;
import com.prinjsystems.asct.structures.conductors.coloredwires.CyanWire;
import com.prinjsystems.asct.structures.conductors.coloredwires.DarkBlueWire;
import com.prinjsystems.asct.structures.conductors.coloredwires.DarkGrayWire;
import com.prinjsystems.asct.structures.conductors.coloredwires.DarkGreenWire;
import com.prinjsystems.asct.structures.conductors.coloredwires.DarkRedWire;
import com.prinjsystems.asct.structures.conductors.coloredwires.GrayWire;
import com.prinjsystems.asct.structures.conductors.coloredwires.GreenWire;
import com.prinjsystems.asct.structures.conductors.coloredwires.LightOrangeWire;
import com.prinjsystems.asct.structures.conductors.coloredwires.MagentaWire;
import com.prinjsystems.asct.structures.conductors.coloredwires.OrangeWire;
import com.prinjsystems.asct.structures.conductors.coloredwires.RedWire;
import com.prinjsystems.asct.structures.conductors.coloredwires.WhiteWire;
import com.prinjsystems.asct.structures.conductors.coloredwires.YellowWire;
import com.prinjsystems.asct.structures.conductors.light.RedPixel;
import com.prinjsystems.asct.structures.conductors.semiconductors.NSilicon;
import com.prinjsystems.asct.structures.conductors.semiconductors.PSilicon;
import com.prinjsystems.asct.structures.conductors.semiconductors.PermanentSwitch;
import com.prinjsystems.asct.structures.conductors.semiconductors.ToggleSwitch;
import com.prinjsystems.asct.structures.conductors.semiconductors.Transistor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
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

    private boolean paused = false, wiringMode = false;

    private Tile[] tiles = new Tile[]{new Spark(), new CopperConductor(0, 0), new NSilicon(0, 0),
            new PSilicon(0, 0), new Transistor(0, 0), new ToggleSwitch(0, 0),
            new PermanentSwitch(0, 0), new Clock(0, 0), new RedPixel(0, 0),
            new Insulator(0, 0), new ThermalConductor(0, 0),
            new AluminiumConductor(0, 0)};
    private Tile[] wires = new Tile[]{new BlueWire(0, 0), new BrownWire(0, 0),
            new CyanWire(0, 0), new DarkBlueWire(0, 0), new DarkGrayWire(0, 0),
            new DarkGreenWire(0, 0), new DarkRedWire(0, 0), new GrayWire(0, 0),
            new GreenWire(0, 0), new LightOrangeWire(0, 0), new MagentaWire(0, 0),
            new OrangeWire(0, 0), new RedWire(0, 0), new WhiteWire(0, 0),
            new YellowWire(0, 0)};
    private int currentTile = 0, currentWire = 0;

    private float[] zooms = new float[]{0.25f, 0.5f, 1f, 1.5f, 2.25f, 3.375f, 5.0625f};
    private int currZoom = 0;

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
        // Layers actually work in reverse, so Page Down should increase and Page Up should decrease the layer "pointer"
        keyEvents.put(KeyEvent.VK_PAGE_DOWN, new JKeyEvent(true) {
            @Override
            public void run() {
                map.increaseLayer();
            }
        });
        keyEvents.put(KeyEvent.VK_PAGE_UP, new JKeyEvent(true) {
            @Override
            public void run() {
                map.decreaseLayer();
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
        keyEvents.put(KeyEvent.VK_W, new JKeyEvent(true) {
            @Override
            public void run() {
                wiringMode = !wiringMode;
            }
        });
        keyEvents.put(KeyEvent.VK_Q, new JKeyEvent(true) {
            @Override
            public void run() {
                goToPreviousTileIndex();
            }
        });
        keyEvents.put(KeyEvent.VK_E, new JKeyEvent(true) {
            @Override
            public void run() {
                goToNextTileIndex();
            }
        });
        keyEvents.put(KeyEvent.VK_ADD, new JKeyEvent(true) {
            @Override
            public void run() {
                map.getLayers().add(0, new Layer());
                map.setCurrentLayer(0);
            }
        });
        keyEvents.put(KeyEvent.VK_MINUS, new JKeyEvent(true) {
            @Override
            public void run() {
                map.getLayers().add(new Layer());
                map.setCurrentLayer(map.getLayers().size() - 1);
            }
        });
        keyEvents.put(KeyEvent.VK_F1, new JKeyEvent(true) {
            @Override
            public void run() {
                map.getLayers().remove(map.getCurrentLayer());
                map.decreaseLayer();
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
        keyboardHandler = new KeyboardHandler(keyEvents, Arrays.asList(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT));
        frame.addKeyListener(keyboardHandler);

        Map<Integer, JMouseEvent> mouseEvents = new HashMap<>();
        mouseEvents.put(MouseEvent.BUTTON1, new JMouseEvent(false) {
            @Override
            public void run() {
                int posX = (int) (((getX() - camera.getTranslateX()) / Tile.TILE_SIZE) / camera.getScaleX());
                int posY = (int) (((getY() - camera.getTranslateY()) / Tile.TILE_SIZE) / camera.getScaleY());
                if (posX >= 0 && posX <= Layer.LAYER_SIZE && posY >= 0 && posY <= Layer.LAYER_SIZE) {
                    Layer layer = map.getLayers().get(map.getCurrentLayer());
                    if (getCurrentTile() instanceof Spark) {
                        if (layer.getTile(posX, posY) instanceof ActionTile) {
                            ((ActionTile) layer.getTile(posX, posY)).trySetPowered(true, null);
                        }
                    } else {
                        if (layer.getTile(posX, posY) == null) {
                            try {
                                Tile tile = getCurrentTile().getClass().getDeclaredConstructor(int.class, int.class)
                                        .newInstance(posX, posY);
                                layer.addTile(tile);
                                if (keyboardHandler.isFlagActive(KeyEvent.VK_SHIFT) && tile instanceof ConductorTile) {
                                    int layerIndex = map.getCurrentLayer() != map.getLayers().size() - 1 ?
                                            map.getCurrentLayer() + 1 : -1;
                                    if (layerIndex != -1) {
                                        Tile connectedTile = map.getLayers().get(layerIndex).getTile(posX, posY);
                                        if (connectedTile == null) {
                                            connectedTile = getCurrentTile().getClass()
                                                    .getDeclaredConstructor(int.class, int.class).newInstance(posX, posY);
                                            map.getLayers().get(layerIndex).addTile(connectedTile);
                                        }
                                        if (connectedTile instanceof ActionTile) {
                                            ((ConductorTile) tile).setConnectedTo((ActionTile) connectedTile);
                                        }
                                    }
                                }
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
                if (keyboardHandler.isFlagActive(KeyEvent.VK_CONTROL) && currZoom < zooms.length - 1) {
                    camera.setToScale(zooms[++currZoom], zooms[currZoom]);
                } else if (!keyboardHandler.isFlagActive(KeyEvent.VK_CONTROL)) {
                    goToNextTileIndex();
                }
            }
        });
        wheelEvents.put(MouseHandler.MOUSE_WHEEL_DOWN, new JMouseEvent(false) {
            @Override
            public void run() {
                if (keyboardHandler.isFlagActive(KeyEvent.VK_CONTROL) && currZoom > 0) {
                    camera.setToScale(zooms[--currZoom], zooms[currZoom]);
                } else if (!keyboardHandler.isFlagActive(KeyEvent.VK_CONTROL)) {
                    goToPreviousTileIndex();
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

        // Draw paused string
        graphics.setColor(Color.ORANGE);
        graphics.drawString(paused ? "*PAUSED*" : "", 4, panel.getHeight() - 53);

        // Draw mode string
        graphics.setColor(Color.WHITE);
        graphics.drawString(wiringMode ? "WIRING" : "NORMAL", 4, panel.getHeight() - 40);

        // Draw current tile
        graphics.setColor(getCurrentTile().getColor());
        graphics.fillRect(4, panel.getHeight() - 36, 32, 32);
        graphics.setFont(font);
        graphics.drawString(getCurrentTile().getName(), 38,
                panel.getHeight() - 32 + graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getAscent());

        // Draw next tile
        graphics.setColor(getCurrentTile(getNextTileIndex()).getColor());
        graphics.fillRect(38, panel.getHeight() - 12, 8, 8);
        graphics.drawString("NEXT", 38,
                panel.getHeight() - 18 + graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getAscent());

        // Draw previous tile
        graphics.setColor(getCurrentTile(getPreviousTileIndex()).getColor());
        graphics.fillRect(graphics.getFontMetrics().stringWidth("NEXT") + 40, panel.getHeight() - 12, 8, 8);
        graphics.drawString("PREVIOUS", graphics.getFontMetrics().stringWidth("NEXT") + 40,
                panel.getHeight() - 18 + graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getAscent());

        // Draw layer indication
        for (int i = 0; i < map.getLayers().size(); i++) {
            if (map.getCurrentLayer() == i) {
                graphics.setColor(Color.YELLOW);
            } else {
                graphics.setColor(Color.WHITE);
            }
            graphics.fillRect(panel.getWidth() - 34, 2 + i * 18, 32, 16);
        }

        // Draw "zoom preview"
        int zoomPreviewSize = (int) (Tile.TILE_SIZE * zooms[zooms.length - 1]) * 2;
        graphics.setColor(Color.WHITE);
        graphics.drawString("ZOOM", panel.getWidth() - 5 - zoomPreviewSize, panel.getHeight() - 9 - zoomPreviewSize);
        Stroke sb = graphics.getStroke();
        graphics.setStroke(new BasicStroke(3));
        graphics.drawRect(panel.getWidth() - 5 - zoomPreviewSize, panel.getHeight() - 5 - zoomPreviewSize,
                zoomPreviewSize + 3, zoomPreviewSize + 3);
        graphics.setColor(Color.GRAY);
        graphics.fillRect(panel.getWidth() - 3 - zoomPreviewSize, panel.getHeight() - 3 - zoomPreviewSize,
                (int) (Tile.TILE_SIZE * camera.getScaleX()) * 2, (int) (Tile.TILE_SIZE * camera.getScaleY()) * 2);
        graphics.setStroke(sb);

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

    private int getNextTileIndex() {
        if (wiringMode) {
            if (currentWire + 1 >= wires.length) {
                return 0;
            }
            return currentWire + 1;
        } else {
            if (currentTile + 1 >= tiles.length) {
                return 0;
            }
            return currentTile + 1;
        }
    }

    private int getPreviousTileIndex() {
        if (wiringMode) {
            if (currentWire == 0) {
                return wires.length - 1;
            }
            return currentWire - 1;
        } else {
            if (currentTile == 0) {
                return tiles.length - 1;
            }
            return currentTile - 1;
        }
    }

    private void goToNextTileIndex() {
        if (wiringMode) {
            if (++currentWire >= wires.length) {
                currentWire = 0;
            }
        } else {
            if (++currentTile >= tiles.length) {
                currentTile = 0;
            }
        }
    }

    private void goToPreviousTileIndex() {
        if (wiringMode) {
            if (--currentWire < 0) {
                currentWire = wires.length - 1;
            }
        } else {
            if (--currentTile < 0) {
                currentTile = tiles.length - 1;
            }
        }
    }

    private Tile getCurrentTile() {
        return wiringMode ? wires[currentWire] : tiles[currentTile];
    }

    private Tile getCurrentTile(int index) {
        return wiringMode ? wires[index] : tiles[index];
    }
}
