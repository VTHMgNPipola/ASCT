package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asct.structures.ActionTile;
import com.prinjsystems.asct.structures.Tile;
import java.awt.Color;

public class XORGate extends LogicGate {
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
