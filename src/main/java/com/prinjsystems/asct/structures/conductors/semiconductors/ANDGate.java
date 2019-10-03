package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.LogicGate;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.NSilicon;
import java.awt.Color;

@PlaceableTile
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
