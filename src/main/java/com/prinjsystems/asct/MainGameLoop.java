package com.prinjsystems.asct;

import com.prinjsystems.asct.renderingengine.GameDisplay;
import com.prinjsystems.asctlib.ASCTMod;
import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.TileCategory;
import com.prinjsystems.asctlib.TileCategoryHolder;
import com.prinjsystems.asctlib.structures.Tile;
import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import javax.swing.JOptionPane;
import org.reflections.Reflections;

public class MainGameLoop extends ASCTMod {
    private static final float TARGET_FPS = 30f;
    private static float targetClockFrequency = 30f; // Frequency in Hertz

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException, IOException, ClassNotFoundException {
        Path modFolder = Paths.get("./mods/");
        if (!Files.exists(modFolder)) {
            Files.createDirectories(modFolder);
        }
        List<URL> jarUrlList = walkOnFolder(modFolder);
        URLClassLoader modClassLoader = new URLClassLoader(jarUrlList.toArray(new URL[0]),
                MainGameLoop.class.getClassLoader());
        List<Class<?>> loadedTiles = loadModClasses(modClassLoader, jarUrlList);

        // Startup mods
        Reflections reflections = new Reflections("com");
        for (Class<? extends ASCTMod> mod : reflections.getSubTypesOf(ASCTMod.class)) {
            mod.getDeclaredConstructor().newInstance().startup();
        }

        // Get tiles
        Set<Class<?>> annotatedTiles = reflections.getTypesAnnotatedWith(PlaceableTile.class);
        List<TileCategory> categoryList = TileCategoryHolder.getInstance().getCategories();
        Map<String, TileCategory> categories = new TreeMap<>();
        annotatedTiles.addAll(loadedTiles);
        for (TileCategory category : categoryList) {
            categories.put(category.getName(), category);
        }
        for (Class<?> annotatedTile : annotatedTiles) {
            // If a subclass of a placeable tile isn't itself placeable
            if (annotatedTile.getAnnotation(PlaceableTile.class) == null) {
                continue;
            }
            Tile instance;
            try {
                instance = (Tile) annotatedTile.getDeclaredConstructor(int.class, int.class).newInstance(0, 0);
            } catch (NoSuchMethodException e) {
                instance = (Tile) annotatedTile.getDeclaredConstructor().newInstance();
            }
            categories.get(annotatedTile.getAnnotation(PlaceableTile.class).value()).getTiles().add(instance);
        }

        for (TileCategory category : categories.values()) {
            Collections.sort(category.getTiles());
        }

        // Get resolution (default 1280x768)
        Dimension resolution = new Dimension(1280, 768);
        for (String arg : args) {
            if (arg.matches("--res=\\d+x\\d+")) {
                String[] parts = arg.split("x");
                int width = Integer.parseInt(parts[0].substring(6));
                int height = Integer.parseInt(parts[1]);
                resolution = new Dimension(width, height);
            }
        }

        GameDisplay display = new GameDisplay(resolution, categories);

        Timer timer = new Timer();

        display.setVisible(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    display.render();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                display.updateInputs();
            }
        }, 0, (int) (1000 / TARGET_FPS));

        Thread tickThread = new Thread(() -> {
            try {
                while (!display.isWindowClosing()) {
                    if (!display.isPaused()) {
                        display.tick();
                    }
                    Thread.sleep((int) (1000 / targetClockFrequency));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error waiting for tick!");
            }
        });

        tickThread.start();
    }

    public static float getTargetClockFrequency() {
        return targetClockFrequency;
    }

    public static void setTargetClockFrequency(float targetClockFrequency) {
        MainGameLoop.targetClockFrequency = targetClockFrequency;
    }

    private static List<URL> walkOnFolder(Path path) throws IOException {
        List<URL> urlList = new ArrayList<>();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!Files.isDirectory(file)) { // There may be folders, and I don't want to include those
                    urlList.add(file.toUri().toURL());
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return urlList;
    }

    private static List<Class<?>> loadModClasses(ClassLoader classLoader, List<URL> urls) throws IOException,
            ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
        List<Class<?>> placeableTiles = new ArrayList<>();
        for (URL url : urls) {
            JarInputStream jarInputStream = new JarInputStream(new FileInputStream(url.getFile()));
            JarEntry jarEntry;
            while ((jarEntry = jarInputStream.getNextJarEntry()) != null) {
                if (jarEntry.getName().endsWith(".class")) {
                    Class<?> clazz = classLoader.loadClass(jarEntry.getName().replaceAll("/", "\\.")
                            .replace(".class", ""));
                    if (clazz.getSuperclass() == ASCTMod.class) {
                        ((ASCTMod) clazz.getDeclaredConstructor().newInstance()).startup();
                    }
                    if (clazz.isAnnotationPresent(PlaceableTile.class)) {
                        placeableTiles.add(clazz);
                    }
                }
            }
        }
        return placeableTiles;
    }

    @Override
    public void startup() {
        TileCategoryHolder.getInstance().registerCategory("conductors");
        TileCategoryHolder.getInstance().registerCategory("lighting");
        TileCategoryHolder.getInstance().registerCategory("wiring");
    }
}
