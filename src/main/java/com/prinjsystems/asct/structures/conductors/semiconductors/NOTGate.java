package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asct.structures.Tile;
import java.awt.Color;

public class NOTGate extends LogicGate {
    private int unpoweredFor0 = 0;

    public NOTGate(int posX, int posY) {
        super(posX, posY, Color.RED, "NOT Gate");
    }

    @Override
    public void trySetPowered(boolean powered, Tile source) {
        if (source != null && canReceivePower) {
            if (source instanceof NSilicon) {
                super.trySetPowered(false, null);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (!powered && canReceivePower) {
            if (++unpoweredFor0 == unpoweredDelay) {
                powered = true;
                unpoweredFor0 = 0;
            }
        }
    }
}
