package com.prinjsystems.asct.structures.conductors.light;

import com.prinjsystems.asct.structures.ActionTile;
import com.prinjsystems.asct.structures.Tile;
import com.prinjsystems.asct.structures.conductors.semiconductors.NSilicon;
import com.prinjsystems.asct.structures.conductors.semiconductors.PSilicon;
import java.awt.Color;

// Because of problems with what a pixel should and should not be able to do, making it a ConductorTile is too much
// of a hassle, so I decided to copy the ConductorTile #tick() procedure and just adjust it here.
public abstract class Pixel extends ActionTile {
    private static final long serialVersionUID = 6464774072289540505L;

    private boolean spreading = true;
    private Color offColor;

    protected Pixel(int posX, int posY, Color color, Color offColor) {
        super(posX, posY, color, false);
        this.offColor = offColor;
    }

    public boolean isSpreading() {
        return spreading;
    }

    @Override
    public void trySetPowered(boolean powered, Tile source) {
        boolean oldPowered = isPowered();
        if (source != null) {
            if (source instanceof PSilicon) {
                super.trySetPowered(true, source);
            } else if (source instanceof NSilicon) {
                super.trySetPowered(false, source);
            }
        } else {
            super.trySetPowered(powered, null);
        }
        if (oldPowered != powered) {
            spreading = true;
        }
    }

    @Override
    public void tick() {
        if (isSpreading()) {
            Tile[] tilesAround = from.getTilesAround(posX, posY);
            for (Tile tile : tilesAround) {
                if (tile == null) {
                    continue;
                }
                Tile t = tile;
                if (t instanceof Pixel) {
                    ((Pixel) t).trySetPowered(powered, null);
                }
            }
            spreading = false;
        }
    }

    @Override
    public Color getColor() {
        return powered ? color : offColor;
    }
}
