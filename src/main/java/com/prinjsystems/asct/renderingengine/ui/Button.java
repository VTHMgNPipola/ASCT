package com.prinjsystems.asct.renderingengine.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class Button extends UIComponent<MouseEvent> {
    private String text;
    private Color color, borderColor;

    private boolean pressed;
    private Runnable action;

    public Button(String text, Rectangle2D bounds, Color color, Color borderColor) {
        super(MouseEvent.class);
        this.text = text;
        posX = (float) bounds.getX();
        posY = (float) bounds.getY();
        width = (float) bounds.getWidth();
        height = (float) bounds.getHeight();
        this.color = color;
        this.borderColor = borderColor;
    }

    public Button(String text, Rectangle2D bounds) {
        this(text, bounds, Color.LIGHT_GRAY, Color.DARK_GRAY);
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
        graphics.setStroke(new BasicStroke(2));
        graphics.setColor(borderColor);
        graphics.drawRect(posXDef, posYDef, widthDef, heightDef);

        // Draw text
        graphics.setFont(Label.DEFAULT_FONT);
        graphics.setColor(borderColor);
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

    public Runnable getAction() {
        return action;
    }

    public void setAction(Runnable action) {
        this.action = action;
    }
}
