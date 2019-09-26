package com.prinjsystems.asct.renderingengine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class Label extends UIComponent {
    static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 13);
    private String text;
    private Font font;
    private Color color;

    public Label(String text, Font font, Color color, Rectangle2D.Float bounds) {
        super(bounds);
        this.text = text;
        this.font = font;
        this.color = color;
    }

    public Label(String text, Font font, Rectangle2D.Float bounds) {
        this(text, font, Color.WHITE, bounds);
    }

    public Label(String text, Rectangle2D.Float bounds) {
        this(text, DEFAULT_FONT, bounds);
    }

    public Label(Rectangle2D.Float bounds) {
        this("", bounds);
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(color);
        graphics.setFont(font);
        FontMetrics metrics = graphics.getFontMetrics();
        graphics.drawString(text, posX, posY + metrics.getAscent());
    }

    @Override
    public void update(MouseEvent evt, int mode) {
        // No action
    }

    @Override
    public void update(KeyEvent evt, int mode) {
        // No action
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
