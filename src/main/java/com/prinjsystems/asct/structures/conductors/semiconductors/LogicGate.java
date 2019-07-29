package com.prinjsystems.asct.structures.conductors.semiconductors;

import java.awt.Color;
import java.awt.Graphics2D;

public class LogicGate extends Transistor {
    private static final long serialVersionUID = -6760888350939398358L;

    private Color insideColor;

    protected LogicGate(int posX, int posY, Color color, String name) {
        super(posX, posY);
        this.name = name;
        this.insideColor = color;
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        g.setColor(insideColor);
        g.fillRect(posX + 1, posY + 1, TILE_SIZE - 1, TILE_SIZE - 1);
    }
}
