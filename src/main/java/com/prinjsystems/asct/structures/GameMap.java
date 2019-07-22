package com.prinjsystems.asct.structures;

import java.awt.Graphics;
import java.io.Serializable;
import java.util.List;

public class GameMap implements Serializable {
    private static final long serialVersionUID = -5445523919009569824L;

    private List<Layer> layers; // A Deque would be great, but it is impossible to access n-th element in it
    private int currentLayer;

    public GameMap(List<Layer> layers) {
        this.layers = layers;
    }

    public void render(Graphics g) {
        layers.get(currentLayer).render(g);
    }

    public void tick() {
        for (Layer l : layers) {
            l.tick();
        }
    }

    public void setCurrentLayer(int currentLayer) {
        this.currentLayer = currentLayer;
    }

    public void increaseLayer() {
        if (currentLayer < layers.size() - 1) {
            currentLayer++;
        } else {
            currentLayer = 0;
        }
    }

    public void decreaseLayer() {
        if (currentLayer > 0) {
            currentLayer--;
        } else {
            currentLayer = layers.size() - 1;
        }
    }
}
