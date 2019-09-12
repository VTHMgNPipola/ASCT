package com.prinjsystems.asct;

import com.prinjsystems.asct.renderingengine.GameDisplay;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;

public class MainGameLoop {
    private static final float TARGET_FPS = 30f;
    private static float targetClockFrequency = 30f; // Frequency in Hertz

    public static void main(String[] args) {
        Dimension resolution = new Dimension(1280, 768);
        for (String arg : args) {
            if (arg.matches("--res=\\d+x\\d+")) {
                String[] parts = arg.split("x");
                int width = Integer.parseInt(parts[0].substring(6));
                int height = Integer.parseInt(parts[1]);
                resolution = new Dimension(width, height);
            }
        }

        GameDisplay display = new GameDisplay(resolution);

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
}
