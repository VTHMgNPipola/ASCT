package com.prinjsystems.asct.renderingengine.ui;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.InputEvent;

public abstract class UIComponent<E extends InputEvent> {
    public static final Stroke BASIC_STROKE = new BasicStroke(1);
    public static final Stroke THICK_STROKE = new BasicStroke(2);

    protected float posX, posY, width, height;
    private Class<E> genericsType;

    public UIComponent(Class<E> genericsType) {
        this.genericsType = genericsType;
    }

    public Class<E> getGenericsType() {
        return genericsType;
    }

    public void setGenericsType(Class<E> genericsType) {
        this.genericsType = genericsType;
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

    /**
     * Will render the component at its current state.
     */
    public abstract void render(Graphics2D graphics);

    /**
     * Will update this component with an event of a specific type, that may trigger an action in it.
     * This method is supposed to be called only a valid event happened, since it is not guaranteed that implementations
     * will check for that.
     *
     * @param evt  Event that happened.
     * @param mode If useful for the implementation, may be the current "mode" of the event (e.g. if the mouse was
     *             pressed or released on a button).
     */
    public abstract void update(E evt, int mode);
}
