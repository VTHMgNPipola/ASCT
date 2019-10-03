package com.prinjsystems.asct.renderingengine.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;

public class Button extends UIComponent {
    private String text;
    public static final Color DEFAULT_BACKGROUND_COLOR = Color.LIGHT_GRAY;

    private boolean pressed;
    private Runnable action;
    public static final Color DEFAULT_BORDER_COLOR = Color.DARK_GRAY;
    private Color color, borderColor, textColor;

    public Button(String text, Rectangle2D.Float bounds, Color color, Color borderColor) {
        super(bounds);
        this.text = text;
        this.color = color;
        this.borderColor = borderColor;
    }

    public Button(String text, Rectangle2D.Float bounds) {
        this(text, bounds, DEFAULT_BACKGROUND_COLOR, DEFAULT_BORDER_COLOR);
    }

    public Button(String text, float posX, float posY, float width, float height) {
        this(text, new Rectangle2D.Float(posX, posY, width, height));
    }

    @Override
    public void render(Graphics2D graphics) {
        // 'Def' for definitive
        int posXDef = (int) (pressed ? posX + 1 : posX);
        int posYDef = (int) (pressed ? posY + 1 : posY);
        int widthDef = (int) (pressed ? width - 2 : width);
        int heightDef = (int) (pressed ? height - 2 : height);

        // Draw background
        graphics.setColor(color);
        graphics.fillRect(posXDef, posYDef, widthDef, heightDef);

        // Draw border
        graphics.setStroke(THICK_STROKE);
        graphics.setColor(borderColor);
        graphics.drawRect(posXDef, posYDef, widthDef, heightDef);

        // Draw text
        graphics.setFont(Label.DEFAULT_FONT);
        graphics.setColor(textColor != null ? textColor : DEFAULT_BORDER_COLOR);
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int x = (int) (((widthDef - fontMetrics.stringWidth(text)) / 2) + posX);
        int y = (int) (((heightDef - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent() + posY);
        graphics.drawString(text, x, y);
    }

    @Override
    public void update(MouseEvent evt, int mode) {
        pressed = (mode == MouseEvent.MOUSE_PRESSED);
        if (!pressed && action != null) {
            action.run();
        }
    }

    @Override
    public void update(MouseWheelEvent evt, int mode) {
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Runnable getAction() {
        return action;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }
}
