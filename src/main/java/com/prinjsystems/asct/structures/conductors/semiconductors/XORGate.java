package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.LogicGate;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.NSilicon;
import java.awt.Color;

@PlaceableTile("logic")
public class XORGate extends LogicGate {
    private static final long serialVersionUID = -6241453379926218849L;

    private int activationTime;

    public XORGate(int posX, int posY) {
        super(posX, posY, Color.CYAN, "XOR Gate", "XOR");
        activationTime = -1;
    }

    @Override
    public void trySetPowered(boolean powered, Tile source) {
        if (source != null && canReceivePower) {
            if (source instanceof NSilicon && activationTime == -1) {
                activationTime = 0;
            } else if (source instanceof NSilicon) {
                activationTime = -1; // If the tile was already powered, then cancel activation
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (activationTime != -1 && ++activationTime == unpoweredDelay) {
            activationTime = -1;
            super.trySetPowered(true, null);
        }
    }
}
