package com.prinjsystems.asct;

import com.prinjsystems.asct.renderingengine.GameDisplay;
import com.prinjsystems.asctlib.ASCTMod;
import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.Tile;
import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import org.reflections.Reflections;

public class MainGameLoop extends ASCTMod {
    private static final float TARGET_FPS = 30f;
    private static float targetClockFrequency = 30f; // Frequency in Hertz

    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Reflections reflections = new Reflections("");
        for (Class<? extends ASCTMod> mod : reflections.getSubTypesOf(ASCTMod.class)) {
            mod.getDeclaredConstructor().newInstance().startup();
        }

        Set<Class<?>> annotatedTiles = reflections.getTypesAnnotatedWith(PlaceableTile.class);
        Tile[] tiles = new Tile[annotatedTiles.size()];
        Iterator<Class<?>> annotatedTilesIterator = annotatedTiles.iterator();
        for (int i = 0; i < tiles.length; i++) {
            Class<?> annotatedTile = annotatedTilesIterator.next();
            try {
                tiles[i] = (Tile) annotatedTile.getDeclaredConstructor(int.class, int.class).newInstance(0, 0);
            } catch (NoSuchMethodException e) {
                tiles[i] = (Tile) annotatedTile.getDeclaredConstructor().newInstance();
            }
        }

        Dimension resolution = new Dimension(1280, 768);
        for (String arg : args) {
            if (arg.matches("--res=\\d+x\\d+")) {
                String[] parts = arg.split("x");
                int width = Integer.parseInt(parts[0].substring(6));
                int height = Integer.parseInt(parts[1]);
                resolution = new Dimension(width, height);
            }
        }

        GameDisplay display = new GameDisplay(resolution, tiles);

        Timer timer = new Timer();

        display.setVisible(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                display.render();
                display.updateInputs();
            }
        }, 0, (int) (1000 / TARGET_FPS));

        Thread tickThread = new Thread(() -> {
            try {
                while (!display.isWindowClosing()) {
                    display.tick();
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

    @Override
    public void startup() {

    }
}
