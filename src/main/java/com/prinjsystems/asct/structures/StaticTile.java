package com.prinjsystems.asct.structures;

import java.awt.Color;
import java.awt.Graphics;

public abstract class StaticTile extends Tile {
    private static final long serialVersionUID = -8394105447224475941L;

    protected StaticTile(int posX, int posY, Color color) {
        super(posX, posY, color);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(color);
        g.fillRect(getPosX() * TILE_SIZE, getPosY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    @Override
    public Color getColor() {
        return color;
    }
}
