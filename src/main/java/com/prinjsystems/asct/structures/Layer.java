package com.prinjsystems.asct.structures;

import com.prinjsystems.asct.structures.conductors.light.Pixel;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Layer implements Serializable {
    private static final long serialVersionUID = 2019849710583642798L;

    public static final int LAYER_SIZE = 1024; // Technically, the largest possible number is 32768, since the
    // largest int number, and so the largest array index, is 2 ^ 31 - 1, 65536 * 65536 is just big enough to not fit.
    // However, there are limitations to how many memory the JVM can allocate, and since the computer I'm using to
    // develop this "game" is basically a potato, there's not much to use.

    private Tile[] tiles; // Having a List would make it "infinite", but it will have an end anyway, and it is so
    // much faster to find tiles over an array than a List.

    public Layer(List<Tile> tiles) {
        for (Tile t : tiles) {
            t.setLayer(this);
        }
        this.tiles = new Tile[LAYER_SIZE * LAYER_SIZE];
        for (Tile t : tiles) {
            this.tiles[t.getPosX() + t.getPosY() * LAYER_SIZE] = t;
            t.from = this;
        }
    }

    void render(Graphics g) {
        for (Tile t : tiles) {
            if (t != null) {
                t.render(g);
            }
        }
    }

    void tick() {
        // FIXME: Pixels that were just turned off need to tick
        // TODO: Optimize
        List<Tile> poweredTiles = new ArrayList<>();
        List<Tile> notNullTiles = new ArrayList<>();
        for (Tile tile : tiles) {
            if (tile != null) {
                if (tile instanceof ActionTile) {
                    if (((ActionTile) tile).isPowered() ||
                            (tile instanceof Pixel && ((Pixel) tile).isSpreading())) {
                        poweredTiles.add(tile);
                    }
                }
                notNullTiles.add(tile);
            }
        }
        poweredTiles.forEach(t -> ((ActionTile) t).tick()); // This is needed because otherwise tiles that are being
        // powered vertically, from top to bottom, would all work in the same tick.
        notNullTiles.forEach(Tile::update);
    }

    public void addTile(Tile tile) {
        tiles[tile.getPosX() + tile.getPosY() * LAYER_SIZE] = tile;
        tile.from = this;
    }

    public Tile[] getTilesAround(int x, int y) {
        Tile[] result = new Tile[4];

        // All this looks horrible but removing a lot of logic and two for loops increases performance
        Tile t;
        if (x > 0 && (t = getTile(x - 1, y)) != null) {
            result[0] = t;
        }
        if (x < LAYER_SIZE - 1 && (t = getTile(x + 1, y)) != null) {
            result[1] = t;
        }
        if (y > 0 && (t = getTile(x, y - 1)) != null) {
            result[2] = t;
        }
        if (y < LAYER_SIZE - 1 && (t = getTile(x, y + 1)) != null) {
            result[3] = t;
        }

        return result;
    }

    Tile getTile(int x, int y) {
        return tiles[x + y * LAYER_SIZE];
    }

    void swapTiles(int x1, int y1, int x2, int y2) {
        Tile t1 = getTile(x1, y1);
        if (t1 != null) {
            t1.setPosX(x2);
            t1.setPosY(y2);
        }
        Tile t2 = getTile(x2, y2);
        if (t2 != null) {
            t2.setPosX(x1);
            t2.setPosY(y1);
        }
        tiles[x2 + y2 * LAYER_SIZE] = t1;
        tiles[x1 + y1 * LAYER_SIZE] = t2;
    }
}
