package com.prinjsystems.asct.renderingengine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

public class KeyboardHandler implements KeyListener {
    private Map<Integer, Runnable> events;

    public KeyboardHandler(Map<Integer, Runnable> events) {
        this.events = events;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Runnable event = events.get(e.getKeyCode());
        if (event != null) {
            event.run();
        }
    }
}
