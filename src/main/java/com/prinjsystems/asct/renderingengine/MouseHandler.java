package com.prinjsystems.asct.renderingengine;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Map;
import javax.swing.SwingUtilities;

public class MouseHandler extends MouseAdapter {
    private Map<Integer, JMouseEvent> events;
    public static final int MOUSE_WHEEL_UP = 1;
    public static final int MOUSE_WHEEL_DOWN = 0;
    private Map<Integer, JMouseEvent> wheelEvents;

    public MouseHandler(Map<Integer, JMouseEvent> events, Map<Integer, JMouseEvent> wheelEvents) {
        this.events = events;
        this.wheelEvents = wheelEvents;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        JMouseEvent event = events.get(e.getButton());
        if (event != null) {
            event.setX(e.getX());
            event.setY(e.getY());
            event.setClickCount(e.getClickCount());
            event.run();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int button = 0;
        // I could copy the code from SwingUtilities#checkMouseButton() and find a way to discover what down mask to
        // use for each button but I'm way too lazy to do that now.
        if (SwingUtilities.isLeftMouseButton(e)) {
            button = MouseEvent.BUTTON1;
        } else if (SwingUtilities.isMiddleMouseButton(e)) {
            button = MouseEvent.BUTTON2;
        } else if (SwingUtilities.isRightMouseButton(e)) {
            button = MouseEvent.BUTTON3;
        }
        JMouseEvent event = events.get(button);
        if (event != null && !event.isRunWhenReleased()) {
            event.setX(e.getX());
            event.setY(e.getY());
            event.setClickCount(e.getClickCount());
            event.run();
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        JMouseEvent event = wheelEvents.get(e.getWheelRotation() < 0 ? MOUSE_WHEEL_UP : MOUSE_WHEEL_DOWN);
        if (event != null) {
            event.setX(e.getX());
            event.setY(e.getY());
            event.setClickCount(e.getClickCount());
            event.run();
        }
    }
}
