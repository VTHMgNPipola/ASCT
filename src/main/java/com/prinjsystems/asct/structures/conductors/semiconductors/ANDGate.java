package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asct.structures.Tile;
import java.awt.Color;

public class ANDGate extends LogicGate {
    private static final long serialVersionUID = -326227518085991510L;

    public ANDGate(int posX, int posY) {
        super(posX, posY, Color.white, "AND Gate");
    }

    @Override
    public void trySetPowered(boolean powered, Tile source) {
        if (source != null && canReceivePower) {
            if (source instanceof NSilicon && isAllConducting()) {
                super.trySetPowered(true, null);
            }
        }
    }

    private boolean isAllConducting() {
        Tile[] tilesAround = from.getTilesAround(posX, posY);
        for (Tile t : tilesAround) {
            if (t instanceof NSilicon && ((NSilicon) t).getUnpoweredFor() == 0) {
                return false;
            }
        }
        return true;
    }
}
