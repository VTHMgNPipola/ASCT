package com.prinjsystems.asct.structures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Tile implements Serializable {
    public static final int TILE_SIZE = 4;
    private static final long serialVersionUID = -2265316791600841307L;
    private static final Random random = new Random();
    protected int posX, posY;
    protected Color color;
    protected Layer from;
    protected float temp;
    protected float meltingTemp = 200;
    protected float irradiationRatio = 0.025f;
    protected float airIrradiationRatio = 0.015f;
    protected int viscosity = 2, currV = viscosity; // An int value that determines how fast it falls when molten
    private int vTick = 0; // Viscosity tick
    protected String name, shortenedName;

    protected Tile(int posX, int posY, Color color, String name) {
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        this.name = name;
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

    public abstract void render(Graphics2D g);

    public void update() {
        if (temp > meltingTemp) { // Is molten
            if (++vTick == currV) {
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
                vTick = 0;
                currV = ThreadLocalRandom.current().nextInt(viscosity - 2, viscosity + 3);
            }
        }

        // Irradiate heat
        Tile[] tilesAround = from.getTilesAround(posX, posY);
        for (Tile t : tilesAround) {
            // Since the resistances would be so low, I decided to arbitrarily choose a "irradiation ratio" for
            // each material
            if (t == null && temp > 27) { // Let's make so that air cannot heat up and is at 27C
                temp -= temp * airIrradiationRatio;
            } else if (t != null && t.getTemp() < temp && temp > 27) {
                t.setTemp(t.getTemp() + temp * irradiationRatio);
                temp -= temp * irradiationRatio;
            }
        }
    }
}
