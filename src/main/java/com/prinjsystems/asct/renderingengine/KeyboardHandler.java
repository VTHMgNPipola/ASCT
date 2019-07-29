package com.prinjsystems.asct.renderingengine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyboardHandler implements KeyListener {
    private Map<Integer, JKeyEvent> events;
    private Map<Integer, JKeyEvent> runningEvents;
    private Map<Integer, Boolean> flags;

    public KeyboardHandler(Map<Integer, JKeyEvent> events, List<Integer> flags) {
        this.events = events;
        runningEvents = new ConcurrentHashMap<>();

        this.flags = new ConcurrentHashMap<>();
        for (Integer flagKeyCode : flags) {
            this.flags.put(flagKeyCode, false);
        }
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

        if (flags.get(e.getKeyCode()) != null) {
            flags.put(e.getKeyCode(), true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        runningEvents.remove(e.getKeyCode());

        if (flags.get(e.getKeyCode()) != null) {
            flags.put(e.getKeyCode(), false);
        }
    }

    public void tick() {
        runningEvents.forEach((k, e) -> {
            e.run();
            if (e.isRunWhenReleased()) {
                runningEvents.remove(k);
            }
        });
    }

    public boolean isFlagActive(int keyCode) {
        return flags.get(keyCode);
    }
}
