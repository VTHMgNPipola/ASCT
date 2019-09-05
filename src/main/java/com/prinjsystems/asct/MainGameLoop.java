package com.prinjsystems.asct;

import com.prinjsystems.asct.renderingengine.GameDisplay;
import java.awt.Dimension;
import java.util.Timer;
import java.util.TimerTask;

public class MainGameLoop {
    private static final float TARGET_FPS = 30f;
    private static final float TARGET_CLOCK_FREQUENCY = 30f; // Frequency in Hertz

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
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!display.isPaused()) {
                    display.tick();
                }
            }
        }, 2000, (int) (1000 / TARGET_CLOCK_FREQUENCY));

        display.setVisible(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                display.render();
                display.updateInputs();
            }
        }, 0, (int) (1000 / TARGET_FPS));
    }
}
