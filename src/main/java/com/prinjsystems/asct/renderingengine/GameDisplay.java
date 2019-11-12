package com.prinjsystems.asct.renderingengine;

import com.prinjsystems.asct.MainGameLoop;
import com.prinjsystems.asct.renderingengine.ui.Button;
import com.prinjsystems.asct.renderingengine.ui.ButtonList;
import com.prinjsystems.asct.renderingengine.ui.Label;
import com.prinjsystems.asct.renderingengine.ui.Panel;
import com.prinjsystems.asct.renderingengine.ui.TextField;
import com.prinjsystems.asct.renderingengine.ui.UIComponent;
import com.prinjsystems.asct.structures.conductors.Spark;
import com.prinjsystems.asctlib.TileCategory;
import com.prinjsystems.asctlib.structures.ActionTile;
import com.prinjsystems.asctlib.structures.GameMap;
import com.prinjsystems.asctlib.structures.Layer;
import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.ConductorTile;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameDisplay {
    private JFrame frame;
    private GamePanel panel;
    private Graphics2D graphics;
    private AffineTransform camera, identityTransform;
    private Font font;
    private KeyboardHandler keyboardHandler;

    private GameMap map;
    private Map<String, TileCategory> categories;
    private List<UIComponent> uiComponents;

    private String currentCategory;

    private boolean paused = false;
    private boolean windowClosing = false;

    private float[] zooms = new float[]{0.25f, 0.5f, 1f, 1.5f, 2.25f, 3.375f, 5.0625f};
    private int currZoom = 0;

    private List<Tile> copiedTiles;
    private int boxX, boxY;
    private int mouseX, mouseY;
    private boolean copying, pasting;

    /* UI Elements */
    private ButtonList tileList;

    public GameDisplay(Dimension resolution, Map<String, TileCategory> categories) {
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

        this.categories = categories;
        currentCategory = categories.keySet().iterator().next();

        List<Layer> layers = new CopyOnWriteArrayList<>();
        layers.add(new Layer());
        map = new GameMap(layers);

        File savesFolder = new File("./saves/");
        if (!savesFolder.exists() && !savesFolder.mkdirs()) {
            throw new IOError(new IOException("Unable to create 'saves' directory!"));
        }

        uiComponents = new ArrayList<>();

        /* FREQUENCY SELECTOR START */
        String frequencyLabelText = "Current frequency: %sHz";
        Label frequencyLabel = new Label(String.format(frequencyLabelText, MainGameLoop.getTargetClockFrequency()),
                new Rectangle2D.Float(panel.getWidth() - 230, 5, 150, 15));
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
            String frequencyText;
            if (frequency / 1000 < 1) {
                frequencyText = frequency + "";
            } else if (frequency / 1000 >= 1 && frequency / 1000000 < 1) {
                frequencyText = (frequency / 1000) + "k";
            } else {
                frequencyText = (frequency / 1000000) + "M";
            }
            frequencyLabel.setText(String.format(frequencyLabelText, frequencyText));
        });
        uiComponents.add(setFrequencyButton);
        /* FREQUENCY SELECTOR END */

        /* SAVE PANEL START */
        int savePanelWidth = 160, savePanelHeight = 90;
        @SuppressWarnings("IntegerDivisionInFloatingPointContext")
        Panel savePanel = new Panel(new Rectangle2D.Float((panel.getWidth() / 2) - (savePanelWidth / 2),
                (panel.getHeight() / 2) - (savePanelHeight / 2), savePanelWidth, savePanelHeight)) {
            @Override
            public void update(KeyEvent evt, int mode) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setVisible(false);
                    setFocused(false);
                }
                super.update(evt, mode);
            }
        };
        savePanel.addComponent(new Label("Save A.S.C.T. Simulation",
                new Rectangle2D.Float(5, 5, 0, 0)));

        Label filenameLabel = new Label("Filename", new Rectangle2D.Float(5, 25, 0, 0));
        savePanel.addComponent(filenameLabel);
        TextField filename = new TextField(new Rectangle2D.Float(5, filenameLabel.getPosY() + 20, 150, 20));
        savePanel.addComponent(filename);

        Button save = new Button("Save", new Rectangle2D.Float(5, filename.getPosY() + 25, 50, 15));
        save.setAction(() -> {
            String filenameWithExtension = filename.getText() + (filename.getText().endsWith(".ssf") ? "" : ".ssf");
            File outputFile = new File("./saves/" + filenameWithExtension);
            try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile)))) {
                oos.writeObject(map);
            } catch (IOException e) {
                e.printStackTrace();
            }
            savePanel.setVisible(false);
        });
        savePanel.addComponent(save);

        int cancelSavePosX = (int) (save.getPosX() + save.getWidth() + 5);
        Button cancelSave = new Button("Cancel", new Rectangle2D.Float(cancelSavePosX, save.getPosY(),
                savePanelWidth - cancelSavePosX - 5, 15));
        cancelSave.setAction(() -> {
            savePanel.setFocused(false);
            savePanel.setVisible(false);
        });
        savePanel.addComponent(cancelSave);

        savePanel.setVisible(false);
        savePanel.setFocused(false);
        uiComponents.add(savePanel);

        Button saveButton = new Button("Save", new Rectangle2D.Float(panel.getWidth() - 96,
                panel.getHeight() - 20, 43, 15));
        saveButton.setAction(() -> {
            savePanel.setVisible(true);
            savePanel.setFocused(true);
        });
        uiComponents.add(saveButton);
        /* SAVE PANEL END */

        /* LOAD PANEL START */
        int loadPanelWidth = 160, loadPanelHeight = 200;
        @SuppressWarnings("IntegerDivisionInFloatingPointContext")
        Panel loadPanel = new Panel(new Rectangle2D.Float((panel.getWidth() / 2) - (loadPanelWidth / 2),
                (panel.getHeight() / 2) - (loadPanelHeight / 2), loadPanelWidth, loadPanelHeight)) {
            @Override
            public void update(KeyEvent evt, int mode) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setVisible(false);
                    setFocused(false);
                }
                super.update(evt, mode);
            }
        };

        loadPanel.addComponent(new Label("Load A.S.C.T. Simulation", new Rectangle2D.Float(5, 5, 0, 0)));

        AtomicReference<String> chosenFile = new AtomicReference<>();

        Label fileLabel = new Label("File", new Rectangle2D.Float(5, 25, 0, 0));
        loadPanel.addComponent(fileLabel);

        // List of files in saves folder
        ButtonList files = new ButtonList(new Rectangle2D.Float(5, fileLabel.getPosY() + 20,
                loadPanelWidth - 10, 130), new Color(160, 160, 160), Button.DEFAULT_BORDER_COLOR);
        files.setTicksPerWheelUnit(20);
        // TODO: Optimize
        List<Button> filesButtons = new ArrayList<>();
        for (File file : Objects.requireNonNull(savesFolder.listFiles())) {
            if (!file.isFile() || !file.getName().endsWith(".ssf")) {
                continue;
            }
            Button fileButton = new Button(file.getName(), new Rectangle2D.Float(0, 0,
                    files.getWidth() - 10, 15));
            fileButton.setAction(() -> {
                chosenFile.set(file.getName());
                for (Button btn : filesButtons) {
                    btn.setBorderColor(Button.DEFAULT_BORDER_COLOR);
                }
                fileButton.setBorderColor(Color.WHITE);
            });
            filesButtons.add(fileButton);
        }
        files.setComponents(filesButtons);
        loadPanel.addComponent(files);

        // Button that loads the file
        Button load = new Button("Load", new Rectangle2D.Float(5, files.getPosY() + files.getHeight() + 5,
                50, 15));
        load.setAction(() -> {
            File inputFile = new File("./saves/" + chosenFile);
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(inputFile)))) {
                map = (GameMap) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            loadPanel.setVisible(false);
        });
        loadPanel.addComponent(load);

        int cancelLoadPosX = (int) (load.getPosX() + load.getWidth() + 5);
        Button cancelLoad = new Button("Cancel", new Rectangle2D.Float(cancelLoadPosX, load.getPosY(),
                savePanelWidth - cancelLoadPosX - 5, 15));
        cancelLoad.setAction(() -> {
            loadPanel.setFocused(false);
            loadPanel.setVisible(false);
        });
        loadPanel.addComponent(cancelLoad);

        loadPanel.setVisible(false);
        loadPanel.setFocused(false);
        uiComponents.add(loadPanel);

        Button loadButton = new Button("Load", new Rectangle2D.Float(panel.getWidth() - 96,
                saveButton.getPosY() - 20, 43, 15));
        loadButton.setAction(() -> {
            // Update list of files
            filesButtons.clear();
            for (File file : Objects.requireNonNull(savesFolder.listFiles())) {
                if (!file.isFile() || !file.getName().endsWith(".ssf")) {
                    continue;
                }
                Button fileButton = new Button(file.getName(), new Rectangle2D.Float(0, 0,
                        files.getWidth() - 10, 15));
                fileButton.setAction(() -> {
                    chosenFile.set(file.getName());
                    for (Button btn : filesButtons) {
                        btn.setBorderColor(Button.DEFAULT_BORDER_COLOR);
                    }
                    fileButton.setBorderColor(Color.WHITE);
                });
                filesButtons.add(fileButton);
            }
            files.setComponents(filesButtons);

            loadPanel.setVisible(true);
            loadPanel.setFocused(true);
        });
        uiComponents.add(loadButton);
        /* LOAD PANEL END */

        FontRenderContext frc = new FontRenderContext(null, false, false);
        /* CATEGORY LISTING START */
        ButtonList categoryList = new ButtonList(new Rectangle2D.Float(0, panel.getHeight() - 70,
                panel.getWidth() - ((Tile.TILE_SIZE * zooms[zooms.length - 1]) * 2) - 58, 35));
        categoryList.setTicksPerWheelUnit(10);
        categoryList.setHorizontal(true);
        for (String categoryName : categories.keySet()) {
            TextLayout textLayout = new TextLayout(categoryName, font, frc);
            Button categoryButton = new Button(categoryName.toUpperCase(), 0, 0,
                    (int) textLayout.getBounds().getWidth() + 10, 25);
            categoryButton.setAction(() -> {
                currentCategory = categoryName;
                updateTileList(tileList);
            });
            categoryList.addComponent(categoryButton);
        }
        uiComponents.add(categoryList);
        /* CATEGORY LISTING END */

        /* TILE LISTING START */
        tileList = new ButtonList(new Rectangle2D.Float(0, categoryList.getPosY() + categoryList.getHeight(),
                categoryList.getWidth(), categoryList.getHeight()));
        tileList.setTicksPerWheelUnit(10);
        tileList.setHorizontal(true);
        updateTileList(tileList);
        uiComponents.add(tileList);
        /* TILE LISTING END */

        Map<Integer, JKeyEvent> keyEvents = new HashMap<>();
        keyEvents.put(KeyEvent.VK_C, new JKeyEvent(true) {
            @Override
            public void run() {
                if (keyboardHandler.isFlagActive(KeyEvent.VK_CONTROL)) {
                    copying = true;
                    boxX = -1;
                    boxY = -1;
                }
            }
        });
        keyEvents.put(KeyEvent.VK_V, new JKeyEvent(true) {
            @Override
            public void run() {
                if (keyboardHandler.isFlagActive(KeyEvent.VK_CONTROL)) {
                    pasting = true;
                }
            }
        });
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
                    loadPanel.setVisible(true);
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
        keyboardHandler = new KeyboardHandler(keyEvents, Arrays.asList(KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT,
                KeyEvent.VK_ALT));

        KeyAdapter uiKeyListener = new KeyAdapter() {
            @Override
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
                if (copying || pasting) {
                    return;
                }

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
                                        || keyboardHandler.isFlagActive(KeyEvent.VK_ALT)
                                        && tile instanceof ConductorTile) {
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
                    double scaleFactor = zooms[++currZoom] / camera.getScaleX();
                    camera.scale(scaleFactor, scaleFactor);

                    double newTranslateX = camera.getTranslateX() * scaleFactor;
                    double newTranslateY = camera.getTranslateY() * scaleFactor;
                    camera.translate(newTranslateX - camera.getTranslateX(), newTranslateY - camera.getTranslateY());
                }
            }
        });
        wheelEvents.put(MouseHandler.MOUSE_WHEEL_DOWN, new JMouseEvent(false) {
            @Override
            public void run() {
                if (keyboardHandler.isFlagActive(KeyEvent.VK_CONTROL) && currZoom > 0) {
                    double scaleFactor = zooms[--currZoom] / camera.getScaleX();
                    camera.scale(scaleFactor, scaleFactor);
                }
            }
        });

        MouseHandler mouseHandler = new MouseHandler(mouseEvents, wheelEvents);
        MouseAdapter uiMouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                boolean activatedComponent = updateComponents(e, MouseEvent.MOUSE_PRESSED);

                if (!activatedComponent) {
                    if (copying && boxX == -1) {
                        int gridSize = (int) (Tile.TILE_SIZE * camera.getScaleX());
                        boxX = (int) ((e.getX() - camera.getTranslateX()) / gridSize);
                        boxY = (int) ((e.getY() - camera.getTranslateX()) / gridSize);
                        return;
                    }

                    if (e.getButton() == MouseEvent.BUTTON1) {
                        placeTile.setX(e.getX());
                        placeTile.setY(e.getY());
                        placeTile.run();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (copying) {
                    copiedTiles = new ArrayList<>();
                    int gridSize = (int) (Tile.TILE_SIZE * camera.getScaleX());
                    int lastX = (int) ((e.getX() - camera.getTranslateX()) / gridSize);
                    int lastY = (int) ((e.getY() - camera.getTranslateX()) / gridSize);
                    System.out.println("Copying from (" + boxX + ";" + boxY + ") to (" + lastX + ";" + lastY + ")");
                    for (int yy = boxY; yy <= lastY; yy++) {
                        for (int xx = boxX; xx <= lastX; xx++) {
                            copiedTiles.add(map.getLayers().get(map.getCurrentLayer()).getTile(xx, yy));
                        }
                    }
                    copying = false;
                } else if (pasting) {
                    // All tiles will be in a order, so incrementing a X and Y counter will not have unexpected behaviour
                    int gridSize = (int) (Tile.TILE_SIZE * camera.getScaleX());
                    int initialX = 0, initialY = 0;
                    for (int i = 0; i < copiedTiles.size(); i++) {
                        if (copiedTiles.get(i) != null) {
                            initialX = copiedTiles.get(i).getPosX();
                            initialY = copiedTiles.get(i).getPosY();
                            break;
                        }
                    }
                    int xOffset = (int) ((e.getX() - camera.getTranslateX()) / gridSize) - initialX,
                            yOffset = (int) ((e.getY() - camera.getTranslateX()) / gridSize) - initialY;
                    try {
                        for (Tile tile : copiedTiles) {
                            if (tile == null) {
                                continue;
                            }

                            Tile clonedTile = (Tile) tile.clone();
                            clonedTile.setPosX(tile.getPosX() + xOffset);
                            clonedTile.setPosY(tile.getPosY() + yOffset);
                            map.getLayers().get(map.getCurrentLayer()).addTile(clonedTile);
                        }
                    } catch (CloneNotSupportedException exc) {
                        exc.printStackTrace();
                    }
                    pasting = false;
                } else {
                    updateComponents(e, MouseEvent.MOUSE_RELEASED);
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                updateComponents(e,
                        e.getWheelRotation() < 0 ? MouseHandler.MOUSE_WHEEL_UP : MouseHandler.MOUSE_WHEEL_DOWN);
            }
        };
        frame.addMouseListener(mouseHandler);
        panel.addMouseListener(uiMouseListener);
        panel.addMouseWheelListener(uiMouseListener);
        panel.addMouseMotionListener(mouseHandler);
        panel.addMouseMotionListener(uiMouseListener);
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

    public void render() throws CloneNotSupportedException {
        graphics.setFont(font);
        graphics.setTransform(identityTransform);
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, panel.getWidth(), panel.getHeight());

        // Draw paused string
        graphics.setColor(Color.ORANGE);
        graphics.drawString(paused ? "*PAUSED*" : "", 4,
                panel.getHeight() - graphics.getFontMetrics().getHeight() - 60);

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
        int rectX = panel.getWidth() - 8 - zoomPreviewSize;
        int rectY = panel.getHeight() - 8 - zoomPreviewSize;
        graphics.setColor(Color.WHITE);
        graphics.drawString("ZOOM", rectX, rectY - 5);
        graphics.setStroke(UIComponent.THICK_STROKE);
        graphics.drawRect(rectX, rectY, zoomPreviewSize + 3, zoomPreviewSize + 3);
        graphics.setColor(Color.GRAY);
        graphics.fillRect(rectX + 1, rectY + 1,
                ((int) (Tile.TILE_SIZE * camera.getScaleX()) * 2) + 1,
                ((int) (Tile.TILE_SIZE * camera.getScaleY()) * 2) + 1);

        graphics.setStroke(UIComponent.BASIC_STROKE);
        graphics.setTransform(camera);
        map.render(graphics);

        TileCategory category = categories.get(currentCategory);
        for (int i = 0; i < tileList.getComponents().size(); i++) {
            if (i == category.getCurrentTileIndex()) {
                ((Button) tileList.getComponent(i)).setBorderColor(Color.WHITE);
            } else {
                ((Button) tileList.getComponent(i)).setBorderColor(Button.DEFAULT_BORDER_COLOR);
            }
        }

        // Draw tile preview
        Composite oldComposite = graphics.getComposite();
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        graphics.setComposite(alphaComposite);

        double gridSize = Tile.TILE_SIZE * camera.getScaleX();
        Tile currentTile = (Tile) getCurrentTile().clone();
        currentTile.setPosX((int) ((mouseX - camera.getTranslateX()) / gridSize));
        currentTile.setPosY((int) ((mouseY - camera.getTranslateY()) / gridSize));
        currentTile.render(graphics);

        graphics.setComposite(oldComposite);

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

    public boolean isWindowClosing() {
        return windowClosing;
    }

    public boolean isPaused() {
        return paused;
    }

    private Tile getCurrentTile() {
        return categories.get(currentCategory).getCurrentTile();
    }

    private boolean updateComponents(MouseEvent e, int mode) {
        boolean activatedComponent = false;
        for (int i = uiComponents.size() - 1; i > 0; i--) {
            UIComponent uiComponent = uiComponents.get(i);
            if (!activatedComponent
                    && e.getX() > uiComponent.getPosX()
                    && e.getX() < uiComponent.getPosX() + uiComponent.getWidth()
                    && e.getY() > uiComponent.getPosY()
                    && e.getY() < uiComponent.getPosY() + uiComponent.getHeight()) {
                if (!(uiComponent instanceof Panel) || ((Panel) uiComponent).isVisible()) {
                    uiComponent.setFocused(true);
                    uiComponent.update(e, mode);
                    activatedComponent = true;
                } else {
                    uiComponent.setFocused(true);
                }
            } else {
                uiComponent.setFocused(false);
            }
        }
        return activatedComponent;
    }

    private boolean updateComponents(MouseWheelEvent e, int mode) {
        boolean activatedComponent = false;
        for (int i = uiComponents.size() - 1; i > 0; i--) {
            UIComponent uiComponent = uiComponents.get(i);
            if (!activatedComponent
                    && e.getX() > uiComponent.getPosX()
                    && e.getX() < uiComponent.getPosX() + uiComponent.getWidth()
                    && e.getY() > uiComponent.getPosY()
                    && e.getY() < uiComponent.getPosY() + uiComponent.getHeight()) {
                uiComponent.setFocused(true);
                uiComponent.update(e, mode);
                activatedComponent = true;
            } else {
                uiComponent.setFocused(false);
            }
        }
        return activatedComponent;
    }

    private void updateTileList(ButtonList tileList) {
        tileList.setComponents(new CopyOnWriteArrayList<>());
        FontRenderContext frc = new FontRenderContext(null, false, false);
        for (int i = 0; i < categories.get(currentCategory).getTiles().size(); i++) {
            TileCategory category = categories.get(currentCategory);
            Tile tile = category.getTiles().get(i);
            TextLayout textLayout = new TextLayout(tile.getShortenedName(), font, frc);
            Button tileButton = new Button(tile.getShortenedName(), 0, 0,
                    (int) textLayout.getBounds().getWidth() + 10, 25);
            Color tileColor = tile.getActualColor();
            tileButton.setColor(tileColor);
            if (Color.RGBtoHSB(tileColor.getRed(), tileColor.getGreen(), tileColor.getBlue(), null)[2] < 0.69) {
                tileButton.setTextColor(Color.WHITE);
            }
            int finalI = i;
            tileButton.setAction(() -> category.setCurrentTileIndex(finalI));
            tileList.addComponent(tileButton);
        }
    }
}
