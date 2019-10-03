package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.LogicGate;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.NSilicon;
import java.awt.Color;

@PlaceableTile
public class ORGate extends LogicGate {
    private static final long serialVersionUID = -9170005924694439966L;

    public ORGate(int posX, int posY) {
        super(posX, posY, Color.GRAY, "OR Gate", "OR");
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
