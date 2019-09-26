package com.prinjsystems.asct.renderingengine.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Panel extends UIComponent {
    private List<UIComponent> components;
    private Color backgroundColor, borderColor;
    private boolean visible;

    public Panel(Rectangle2D.Float bounds) {
        super(bounds);
        components = new ArrayList<>();

        // Default colors
        backgroundColor = Color.GRAY;
        borderColor = Color.DARK_GRAY;
        visible = true;
    }

    public void addComponent(UIComponent component) {
        components.add(component);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void render(Graphics2D graphics) {
        AffineTransform oldTransform = graphics.getTransform();
        AffineTransform newTransform = (AffineTransform) oldTransform.clone();
        newTransform.setToScale(1, 1);
        newTransform.setToTranslation(posX, posY);
        graphics.setTransform(newTransform);
        // Since the graphics are translated to posX and posY, values X and Y of background and border rendering should be 0
        // Draw background
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, (int) width, (int) height);

        // Draw border
        graphics.setColor(borderColor);
        graphics.drawRect(0, 0, (int) width, (int) height);

        // Draw components
        for (UIComponent component : components) {
            component.render(graphics);
        }
        graphics.setTransform(oldTransform);
    }

    @Override
    public void update(MouseEvent evt, int mode) {
        for (UIComponent component : components) {
            int eventX = (int) (evt.getX() - posX);
            int eventY = (int) (evt.getY() - posY);
            if (eventX > component.getPosX() && eventX < component.getPosX() + component.getWidth()
                    && eventY > component.getPosY() && eventY < component.getPosY() + component.getHeight()) {
                component.setFocused(true);
                component.update(evt, mode);
            } else {
                component.setFocused(false);
            }
        }
    }

    @Override
    public void update(KeyEvent evt, int mode) {
        for (UIComponent component : components) {
            if (component.isFocused()) {
                component.update(evt, mode);
            }
        }
    }
}
