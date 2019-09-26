package com.prinjsystems.asct.renderingengine;

import com.prinjsystems.asct.MainGameLoop;
import com.prinjsystems.asct.renderingengine.ui.Button;
import com.prinjsystems.asct.renderingengine.ui.Label;
import com.prinjsystems.asct.renderingengine.ui.Panel;
import com.prinjsystems.asct.renderingengine.ui.TextField;
import com.prinjsystems.asct.renderingengine.ui.UIComponent;
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
import com.prinjsystems.asct.structures.conductors.light.BluePixel;
import com.prinjsystems.asct.structures.conductors.light.CyanPixel;
import com.prinjsystems.asct.structures.conductors.light.GreenPixel;
import com.prinjsystems.asct.structures.conductors.light.RedPixel;
import com.prinjsystems.asct.structures.conductors.light.WhitePixel;
import com.prinjsystems.asct.structures.conductors.light.YellowPixel;
import com.prinjsystems.asct.structures.conductors.semiconductors.ANDGate;
import com.prinjsystems.asct.structures.conductors.semiconductors.NORGate;
import com.prinjsystems.asct.structures.conductors.semiconductors.NOTGate;
import com.prinjsystems.asct.structures.conductors.semiconductors.NSilicon;
import com.prinjsystems.asct.structures.conductors.semiconductors.ORGate;
import com.prinjsystems.asct.structures.conductors.semiconductors.PSilicon;
import com.prinjsystems.asct.structures.conductors.semiconductors.PermanentSwitch;
import com.prinjsystems.asct.structures.conductors.semiconductors.ToggleSwitch;
import com.prinjsystems.asct.structures.conductors.semiconductors.Transistor;
import com.prinjsystems.asct.structures.conductors.semiconductors.XORGate;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameDisplay {
    private JFrame frame;
    private GamePanel panel;
    private Graphics2D graphics;
    private AffineTransform camera, identityTransform;
    private Font font;
    private KeyboardHandler keyboardHandler;
    private MouseHandler mouseHandler;

    private GameMap map;
    private List<UIComponent> uiComponents;

    private boolean paused = false, wiringMode = false;
    private boolean windowClosing = false;

    private Tile[] tiles = new Tile[]{new Spark(), new CopperConductor(0, 0), new NSilicon(0, 0),
            new PSilicon(0, 0), new Transistor(0, 0), new ANDGate(0, 0),
            new NOTGate(0, 0), new ORGate(0, 0), new NORGate(0, 0),
            new XORGate(0, 0), new ToggleSwitch(0, 0), new PermanentSwitch(0, 0),
            new Clock(0, 0), new Insulator(0, 0), new ThermalConductor(0, 0),
            new AluminiumConductor(0, 0), new RedPixel(0, 0), new GreenPixel(0, 0),
            new BluePixel(0, 0), new YellowPixel(0, 0), new CyanPixel(0, 0),
            new WhitePixel(0, 0)};
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

        List<Layer> layers = new ArrayList<>();
        layers.add(new Layer());
        map = new GameMap(layers);

        uiComponents = new ArrayList<>();

        String frequencyLabelText = "Current frequency: %sHz";
        Label frequencyLabel = new Label(String.format(frequencyLabelText, MainGameLoop.getTargetClockFrequency()),
                new Rectangle2D.Float(frame.getWidth() - 209, 5, 150, 15));
        uiComponents.add(frequencyLabel);

        TextField frequencyField = new TextField(new Rectangle2D.Float(frequencyLabel.getPosX(),
                frequencyLabel.getPosY() + frequencyLabel.getHeight() + 10,
                frequencyLabel.getWidth(), 25));
        frequencyField.setAcceptsOnly("[0-9]");
        uiComponents.add(frequencyField);

        Button setFrequencyButton = new Button("Set Frequency", new Rectangle2D.Float(frequencyField.getPosX(),
                frequencyField.getPosY() + frequencyField.getHeight() + 10, frequencyField.getWidth(),
                frequencyField.getHeight()));
        setFrequencyButton.setAction(() -> {
            float frequency;
            try {
                frequency = Float.parseFloat(frequencyField.getText().replace(",", "."));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "The frequency has to be a number!");
                return;
            }
            MainGameLoop.setTargetClockFrequency(frequency);
            frequencyLabel.setText(String.format(frequencyLabelText, MainGameLoop.getTargetClockFrequency()));
        });
        uiComponents.add(setFrequencyButton);

        int savePanelWidth = 160, savePanelHeight = 90;
        @SuppressWarnings("IntegerDivisionInFloatingPointContext")
        Panel savePanel = new Panel(new Rectangle2D.Float((frame.getWidth() / 2) - (savePanelWidth / 2),
                (frame.getHeight() / 2) - (savePanelHeight / 2), savePanelWidth, savePanelHeight)) {
            @Override
            public void update(KeyEvent evt, int mode) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setVisible(false);
                    setFocused(false);
                }
                super.update(evt, mode);
            }
        };
        savePanel.addComponent(new Label("Save A.S.C.T. Simulation", new Rectangle2D.Float(5, 5, 0, 0)));

        Label filenameLabel = new Label("Filename", new Rectangle2D.Float(5, 25, 0, 0));
        savePanel.addComponent(filenameLabel);
        TextField filename = new TextField(new Rectangle2D.Float(5, filenameLabel.getPosY() + 20, 150, 20));
        savePanel.addComponent(filename);

        Button save = new Button("Save", new Rectangle2D.Float(5, filename.getPosY() + 25, 50, 15));
        save.setAction(() -> {
            String filenameWithExtension = filename.getText() + (filename.getText().endsWith(".ssf") ? "" : ".ssf");
            File outputFile = new File("/saves/" + filenameWithExtension);
            if (!outputFile.mkdirs()) {
                throw new IOError(new IOException("Unable to create 'saves' directory!"));
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
                oos.writeObject(map);
            } catch (IOException e) {
                e.printStackTrace();
            }
            savePanel.setVisible(false);
        });
        savePanel.addComponent(save);

        savePanel.setVisible(false);
        uiComponents.add(savePanel);

        // Those are some pretty precise and weird numbers, but they're the only that works
        Button saveButton = new Button("Save", new Rectangle2D.Float(frame.getWidth() - 61,
                frame.getHeight() - 56, 43, 15));
        saveButton.setAction(() -> {
            savePanel.setVisible(true);
            savePanel.setFocused(true);
        });
        uiComponents.add(saveButton);

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
                map.getLayers().add(new Layer());
            }
        });
        keyEvents.put(KeyEvent.VK_SUBTRACT, new JKeyEvent(true) {
            @Override
            public void run() {
                map.getLayers().add(0, new Layer());
                map.increaseLayer();
            }
        });
        keyEvents.put(KeyEvent.VK_DELETE, new JKeyEvent(true) {
            @Override
            public void run() {
                map.getLayers().remove(map.getCurrentLayer());
                map.decreaseLayer();
            }
        });
        keyEvents.put(KeyEvent.VK_S, new JKeyEvent(true) {
            @Override
            public void run() {
                if (keyboardHandler.isFlagActive(KeyEvent.VK_CONTROL)) {
                    savePanel.setVisible(true);
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
        keyboardHandler = new KeyboardHandler(keyEvents, Arrays.asList(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_ALT));

        KeyAdapter uiKeyListener = new KeyAdapter() {
            @Override
            @SuppressWarnings("unchecked")
            public void keyTyped(KeyEvent e) {
                for (UIComponent uiComponent : uiComponents) {
                    if (uiComponent.isFocused()) {
                        uiComponent.update(e, KeyEvent.KEY_TYPED);
                    }
                }
            }
        };

        frame.addKeyListener(keyboardHandler);
        frame.addKeyListener(uiKeyListener);

        Map<Integer, JMouseEvent> mouseEvents = new HashMap<>();
        JMouseEvent placeTile = new JMouseEvent(false) {
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
                                if (keyboardHandler.isFlagActive(KeyEvent.VK_SHIFT)
                                        || keyboardHandler.isFlagActive(KeyEvent.VK_ALT) && tile instanceof ConductorTile) {
                                    int layerIndex = -1;
                                    if (keyboardHandler.isFlagActive(KeyEvent.VK_SHIFT)) {
                                        layerIndex = map.getCurrentLayer() != map.getLayers().size() - 1 ?
                                                map.getCurrentLayer() + 1 : -1;
                                    } else if (keyboardHandler.isFlagActive(KeyEvent.VK_ALT)) {
                                        layerIndex = map.getCurrentLayer() != 0 ? map.getCurrentLayer() - 1 : -1;
                                    }
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
        };
        mouseEvents.put(MouseEvent.BUTTON1, placeTile);
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
        mouseHandler = new MouseHandler(mouseEvents, wheelEvents);
        MouseAdapter uiMouseListener = new MouseAdapter() {
            @Override
            @SuppressWarnings("unchecked")
            public void mousePressed(MouseEvent e) {
                placeTile.setX(e.getX());
                placeTile.setY(e.getY());
                placeTile.run();
                for (UIComponent uiComponent : uiComponents) {
                    if (e.getX() > uiComponent.getPosX() && e.getX() < uiComponent.getPosX() + uiComponent.getWidth()
                            && e.getY() > uiComponent.getPosY() && e.getY() < uiComponent.getPosY() + uiComponent.getHeight()) {
                        uiComponent.setFocused(true);
                        uiComponent.update(e, MouseEvent.MOUSE_PRESSED);
                    } else {
                        uiComponent.setFocused(false);
                    }
                }
            }

            @Override
            @SuppressWarnings("unchecked")
            public void mouseReleased(MouseEvent e) {
                for (UIComponent uiComponent : uiComponents) {
                    if (e.getX() > uiComponent.getPosX() && e.getX() < uiComponent.getPosX() + uiComponent.getWidth()
                            && e.getY() > uiComponent.getPosY() && e.getY() < uiComponent.getPosY() + uiComponent.getHeight()) {
                        uiComponent.setFocused(true);
                        uiComponent.update(e, MouseEvent.MOUSE_RELEASED);
                    } else {
                        uiComponent.setFocused(false);
                    }
                }
            }
        };
        frame.addMouseListener(mouseHandler);
        panel.addMouseListener(uiMouseListener);
        panel.addMouseMotionListener(mouseHandler);
        panel.addMouseWheelListener(mouseHandler);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                windowClosing = true;
            }
        });
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

        // Draw tile ghost
        double tileSize = Tile.TILE_SIZE * camera.getScaleX();
        Color tileColor = getCurrentTile().getColor();
        graphics.setColor(new Color(tileColor.getRed(), tileColor.getGreen(), tileColor.getBlue(), 127));
        graphics.fillRect((int) (mouseHandler.getMouseX() - (mouseHandler.getMouseX() % tileSize)),
                (int) (mouseHandler.getMouseY() - (mouseHandler.getMouseY() % tileSize)), (int) tileSize, (int) tileSize);

        // Draw paused string
        graphics.setColor(Color.ORANGE);
        graphics.drawString(paused ? "*PAUSED*" : "", 4, panel.getHeight() - 53);

        // Draw mode string
        graphics.setColor(Color.WHITE);
        graphics.drawString(wiringMode ? "WIRING" : "NORMAL", 4, panel.getHeight() - 40);

        // Draw current tile
        graphics.setColor(tileColor);
        graphics.fillRect(4, panel.getHeight() - 36, 32, 32);
        graphics.setFont(font);
        graphics.drawString(getCurrentTile().getName(), 38,
                panel.getHeight() - 32 + graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getAscent());

        // Draw next tile
        graphics.setColor(getTile(getNextTileIndex()).getColor());
        graphics.fillRect(38, panel.getHeight() - 12, 8, 8);
        graphics.drawString("NEXT", 38,
                panel.getHeight() - 18 + graphics.getFontMetrics().getHeight() - graphics.getFontMetrics().getAscent());

        // Draw previous tile
        graphics.setColor(getTile(getPreviousTileIndex()).getColor());
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
        graphics.drawString("ZOOM", panel.getWidth() - 5 - zoomPreviewSize, panel.getHeight() - 29 - zoomPreviewSize);
        graphics.setStroke(UIComponent.THICK_STROKE);
        graphics.drawRect(panel.getWidth() - 5 - zoomPreviewSize, panel.getHeight() - 25 - zoomPreviewSize,
                zoomPreviewSize + 3, zoomPreviewSize + 3);
        graphics.setColor(Color.GRAY);
        graphics.fillRect(panel.getWidth() - 4 - zoomPreviewSize, panel.getHeight() - 24 - zoomPreviewSize,
                ((int) (Tile.TILE_SIZE * camera.getScaleX()) * 2) + 1,
                ((int) (Tile.TILE_SIZE * camera.getScaleY()) * 2) + 1);

        graphics.setStroke(UIComponent.BASIC_STROKE);
        graphics.setTransform(camera);
        map.render(graphics);

        // Draw UI components
        graphics.setTransform(identityTransform);
        for (UIComponent component : uiComponents) {
            if (!(component instanceof Panel) || ((Panel) component).isVisible()) {
                component.render(graphics);
            }
        }

        panel.repaint();
    }

    public void tick() {
        map.tick();
    }

    public void updateInputs() {
        keyboardHandler.tick();
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean isWindowClosing() {
        return windowClosing;
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

    private Tile getTile(int index) {
        return wiringMode ? wires[index] : tiles[index];
    }
}
