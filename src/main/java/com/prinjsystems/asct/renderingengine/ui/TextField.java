package com.prinjsystems.asct.renderingengine.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class TextField extends UIComponent {
    private String acceptsOnly;
    private StringBuilder text;
    private Color color;

    public TextField(String defaultText, Color color, Rectangle2D.Float bounds) {
        super(bounds);
        this.text = new StringBuilder(defaultText);
        this.color = color;
    }

    public TextField(String defaultText, Rectangle2D.Float bounds) {
        this(defaultText, Color.DARK_GRAY, bounds);
    }

    public TextField(Rectangle2D.Float bounds) {
        this("", bounds);
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setFont(Label.DEFAULT_FONT);
        graphics.setColor(color);
        graphics.setStroke(THICK_STROKE);
        graphics.drawRect((int) posX, (int) posY, (int) width, (int) height);
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.drawString(text.toString(), posX + 3, posY + graphics.getFontMetrics().getAscent());
    }

    @Override
    public void update(MouseEvent evt, int mode) {
        // No action
    }

    @Override
    public void update(KeyEvent evt, int mode) {
        if (evt.getKeyChar() != 8) {
            if (acceptsOnly != null && !String.valueOf(evt.getKeyChar()).matches(acceptsOnly)) {
                return;
            }
            text.append(evt.getKeyChar());
        } else if (text.length() > 0) {
            text.deleteCharAt(text.length() - 1);
        }
    }

    public void setAcceptsOnly(String acceptsOnly) {
        this.acceptsOnly = acceptsOnly;
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
