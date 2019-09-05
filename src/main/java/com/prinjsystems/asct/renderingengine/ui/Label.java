package com.prinjsystems.asct.renderingengine.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class Label extends UIComponent<MouseEvent> {
    static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 13);
    private String text;
    private Font font;
    private Color color;

    public Label(String text, Font font, Color color) {
        super(MouseEvent.class);
        this.text = text;
        this.font = font;
        this.color = color;
    }

    public Label(String text, Font font) {
        this(text, font, Color.WHITE);
    }

    public Label(String text) {
        this(text, DEFAULT_FONT);
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(color);
        graphics.setFont(font);
        graphics.drawString(text, posX, posY);
    }

    @Override
    public void update(MouseEvent evt, int mode) {
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
