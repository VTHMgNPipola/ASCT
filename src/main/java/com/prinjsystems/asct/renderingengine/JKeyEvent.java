package com.prinjsystems.asct.renderingengine;

public abstract class JKeyEvent implements Runnable {
    private boolean runWhenReleased; // When false the action should run always when the key is pressed

    public JKeyEvent(boolean runWhenReleased) {
        this.runWhenReleased = runWhenReleased;
    }

    public boolean isRunWhenReleased() {
        return runWhenReleased;
    }

    public void setRunWhenReleased(boolean runWhenReleased) {
        this.runWhenReleased = runWhenReleased;
    }
}
