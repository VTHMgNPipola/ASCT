package com.prinjsystems.asct.renderingengine.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ButtonList extends UIComponent {
    private List<Button> buttons;
    private Color color, borderColor;
    private boolean horizontal;
    private int scroll;
    private int ticksPerWheelUnit;

    public ButtonList(Rectangle2D.Float bounds, List<Button> buttons, Color color, Color borderColor) {
        super(bounds);
        this.buttons = buttons;
        this.color = color;
        this.borderColor = borderColor;
        ticksPerWheelUnit = 1;
    }

    public ButtonList(Rectangle2D.Float bounds, Color color, Color borderColor) {
        this(bounds, new CopyOnWriteArrayList<>(), color, borderColor);
    }

    public ButtonList(Rectangle2D.Float bounds) {
        this(bounds, Color.GRAY, Color.DARK_GRAY);
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public void setTicksPerWheelUnit(int ticksPerWheelUnit) {
        this.ticksPerWheelUnit = ticksPerWheelUnit;
    }

    public void addComponent(Button button) {
        buttons.add(button);
    }

    public void removeComponent(int index) {
        buttons.remove(index);
    }

    public UIComponent getComponent(int index) {
        return buttons.get(index);
    }

    public List<Button> getComponents() {
        return buttons;
    }

    public void setComponents(List<Button> buttons) {
        this.buttons = buttons;
    }

    @Override
    public void render(Graphics2D graphics) {
        Graphics2D g = (Graphics2D) graphics.create();

        g.translate(posX, posY);
        g.setClip(0, 0, (int) width, (int) height);

        g.setColor(color);
        g.fillRect(0, 0, (int) width, (int) height);
        g.setColor(borderColor);
        g.drawRect(0, 0, (int) width, (int) height);

        int posOffset = 5 - scroll;
        for (Button button : buttons) {
            if (horizontal) {
                button.setPosX(posOffset);
                button.setPosY(5);
                posOffset += button.getWidth() + 5;
            } else {
                button.setPosX(5);
                button.setPosY(posOffset);
                posOffset += button.getHeight() + 5;
            }

            button.render(g);
        }

        g.dispose();
    }

    @Override
    public void update(MouseEvent evt, int mode) {
        for (UIComponent component : buttons) {
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
    public void update(MouseWheelEvent evt, int mode) {
        scroll += evt.getUnitsToScroll() * ticksPerWheelUnit;
        int maximumScroll = getMaximumScroll();
        if (scroll < 0) {
            scroll = 0;
        } else if (scroll > maximumScroll) {
            scroll = maximumScroll;
        }
    }

    @Override
    public void update(KeyEvent evt, int mode) {
        // No action
    }

    private int getMaximumScroll() {
        int maximumScroll = 5;
        for (Button button : buttons) {
            if (horizontal) {
                maximumScroll += button.getWidth() + 5;
            } else {
                maximumScroll += button.getHeight() + 5;
            }
        }
        maximumScroll -= width;
        if (maximumScroll < 0) {
            maximumScroll = 0;
        }
        return maximumScroll;
    }
}
