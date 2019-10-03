package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.LogicGate;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.NSilicon;
import java.awt.Color;

@PlaceableTile
public class NORGate extends LogicGate {
    private static final long serialVersionUID = -6562875824123718840L;

    private int unpoweredFor0 = 0;

    public NORGate(int posX, int posY) {
        super(posX, posY, Color.LIGHT_GRAY, "NOR Gate");
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
