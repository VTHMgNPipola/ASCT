package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asct.structures.Tile;
import java.awt.Color;

public class ORGate extends LogicGate {
    public ORGate(int posX, int posY) {
        super(posX, posY, Color.GRAY, "OR Gate");
    }

    @Override
    public void trySetPowered(boolean powered, Tile source) {
        if (source != null && canReceivePower) {
            if (source instanceof NSilicon) {
                super.trySetPowered(true, null);
            }
        }
    }
}
