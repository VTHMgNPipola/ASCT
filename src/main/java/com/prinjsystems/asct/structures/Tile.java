package com.prinjsystems.asct.structures;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.Random;

public abstract class Tile implements Serializable {
    protected static final int TILE_SIZE = 4;
    private static final long serialVersionUID = -2265316791600841307L;
    private static final Random random = new Random();
    protected int posX, posY;
    protected Color color;
    protected Layer from;
    protected float temp;
    protected float meltingTemp = 200;
    private String name, shortenedName;

    protected Tile(int posX, int posY, Color color) {
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        temp = 27; // 27 Celsius
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortenedName() {
        return shortenedName;
    }

    public void setShortenedName(String shortenedName) {
        this.shortenedName = shortenedName;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public abstract Color getColor();

    public Color getActualColor() {
        return color;
    }

    public Layer getLayer() {
        return from;
    }

    public void setLayer(Layer from) {
        this.from = from;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public abstract void render(Graphics g);

    public void update() {
        if (temp > meltingTemp) { // Is molten
            if (from.getTile(posX, posY + 1) == null) { // Y + 1 is one below
                from.swapTiles(posX, posY, posX, posY + 1);
            } else {
                boolean leftFirst = random.nextBoolean();
                if (leftFirst) {
                    if (from.getTile(posX - 1, posY + 1) == null) {
                        from.swapTiles(posX, posY, posX - 1, posY + 1);
                    } else if (from.getTile(posX + 1, posY + 1) == null) {
                        from.swapTiles(posX, posY, posX + 1, posY + 1);
                    }
                } else {
                    if (from.getTile(posX + 1, posY + 1) == null) {
                        from.swapTiles(posX, posY, posX + 1, posY + 1);
                    } else if (from.getTile(posX - 1, posY + 1) == null) {
                        from.swapTiles(posX, posY, posX - 1, posY + 1);
                    }
                }
            }
        }
    }
}
