package com.prinjsystems.asct.renderingengine;

public abstract class JMouseEvent implements Runnable {
    private int x, y, clickCount;
    private boolean runWhenReleased;

    public JMouseEvent(boolean runWhenReleased) {
        this.runWhenReleased = runWhenReleased;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public boolean isRunWhenReleased() {
        return runWhenReleased;
    }

    public void setRunWhenReleased(boolean runWhenReleased) {
        this.runWhenReleased = runWhenReleased;
    }
}
