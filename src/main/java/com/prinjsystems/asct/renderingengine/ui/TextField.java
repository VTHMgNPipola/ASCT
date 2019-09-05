package com.prinjsystems.asct.renderingengine.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

public class TextField extends UIComponent<KeyEvent> {
    private StringBuilder text;
    private Color color;

    public TextField(String defaultText, Color color, Rectangle2D bounds) {
        super(KeyEvent.class);
        this.text = new StringBuilder(defaultText);
        this.color = color;
        this.posX = (float) bounds.getX();
        this.posY = (float) bounds.getY();
        this.width = (float) bounds.getWidth();
        this.height = (float) bounds.getHeight();
    }

    public TextField(String defaultText, Rectangle2D bounds) {
        this(defaultText, Color.DARK_GRAY, bounds);
    }

    public TextField(Rectangle2D bounds) {
        this("", bounds);
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(color);
        graphics.setStroke(THICK_STROKE);
        graphics.drawRect((int) posX, (int) posY, (int) width, (int) height);
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.drawString(text.toString(), posX, posY);
    }

    @Override
    public void update(KeyEvent evt, int mode) {
        if (evt.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
            text.append(evt.getKeyChar());
        } else {
            text.deleteCharAt(text.length() - 1);
        }
    }

    public String getText() {
        return text.toString();
    }

    public void setText(String text) {
        this.text = new StringBuilder(text);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return text.toString();
    }
}
