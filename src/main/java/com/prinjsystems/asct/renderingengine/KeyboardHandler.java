package com.prinjsystems.asct.renderingengine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyboardHandler implements KeyListener {
    private Map<Integer, JKeyEvent> events;
    private Map<Integer, JKeyEvent> runningEvents;

    public KeyboardHandler(Map<Integer, JKeyEvent> events) {
        this.events = events;
        runningEvents = new ConcurrentHashMap<>();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        JKeyEvent event = events.get(e.getKeyCode());
        if (event != null) {
            runningEvents.put(e.getKeyCode(), event);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        runningEvents.remove(e.getKeyCode());
    }

    public void tick() {
        runningEvents.forEach((k, e) -> {
            e.run();
            if (e.isRunWhenReleased()) {
                runningEvents.remove(k);
            }
        });
    }
}
