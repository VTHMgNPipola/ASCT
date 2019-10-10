package com.prinjsystems.asct.renderingengine.ui;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;

public abstract class UIComponent {
    public static final Stroke BASIC_STROKE = new BasicStroke(1);
    public static final Stroke THICK_STROKE = new BasicStroke(2);

    protected float posX, posY, width, height;
    protected boolean focused = false;

    public UIComponent(Rectangle2D.Float bounds) {
        if (bounds != null) {
            this.posX = (float) bounds.getX();
            this.posY = (float) bounds.getY();
            this.width = (float) bounds.getWidth();
            this.height = (float) bounds.getHeight();
        }
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    /**
     * Will render the component at its current state.
     */
    public abstract void render(Graphics2D graphics);

    /**
     * Will update this component with a mouse event, that may trigger an action in it.
     * This method is supposed to be called only when a valid event happened (the mouse is over the component, for example),
     * since it is not guaranteed that implementations will check for that.
     *
     * @param evt  Event that happened.
     * @param mode If useful for the implementation, may be the current "mode" of the event (e.g. if the mouse was
     *             pressed or released on a button).
     */
    public abstract void update(MouseEvent evt, int mode);

    /**
     * Will update this component with a mouse wheel event, that may trigger an action in it.
     * This method is supposed to be called only when a valid event happened (the mouse wheel moves at the component),
     * since it is not guaranteed that implementations will check for that.
     *
     * @param evt  Event that happened.
     * @param mode If useful for the implementation, may be the current "mode" of the event (e.g. if the mouse wheel
     *             was moved up or down).
     */
    public abstract void update(MouseWheelEvent evt, int mode);

    /**
     * Will update this component with a key event, that may trigger an action in it.
     * This method is supposed to be called only when a valid event happened, since it is not guaranteed that implementations
     * will check for that.
     *
     * @param evt  Event that happened.
     * @param mode If useful for the implementation, may be the current "mode" of the event (e.g. if the key was
     *             pressed, released or typed).
     */
    public abstract void update(KeyEvent evt, int mode);
}
