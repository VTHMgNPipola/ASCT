package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.ActionTile;
import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.LogicGate;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.NSilicon;
import java.awt.Color;

@PlaceableTile
public class XORGate extends LogicGate {
    private static final long serialVersionUID = -6241453379926218849L;

    public XORGate(int posX, int posY) {
        super(posX, posY, Color.CYAN, "XOR Gate");
    }

    @Override
    public void trySetPowered(boolean powered, Tile source) {
        if (source != null && canReceivePower) {
            if (source instanceof NSilicon && isAllOffExcept(((NSilicon) source))) {
                super.trySetPowered(true, null);
            }
        }
    }

    private boolean isAllOffExcept(ActionTile tile) {
        Tile[] tilesAround = from.getTilesAround(posX, posY);
        for (Tile t : tilesAround) {
            if ((t instanceof NSilicon && ((NSilicon) t).getUnpoweredFor() != 0) && t.getPosX() != tile.getPosX()
                    && t.getPosY() != tile.getPosY()) {
                return false;
            }
        }
        return tile.isPowered();
    }
}
