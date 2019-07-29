package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asct.structures.Tile;
import java.awt.Color;

public class ANDGate extends LogicGate {
    private static final long serialVersionUID = -326227518085991510L;

    protected ANDGate(int posX, int posY) {
        super(posX, posY, Color.white, "AND Gate");
    }

    @Override
    public void trySetPowered(boolean powered, Tile source) {
        if (source != null && canReceivePower) {
            if (source instanceof NSilicon) {
                conductive = true;
                conductiveFor = 0;
            }
        }
    }

    @Override
    protected boolean isValid(Tile tile) {
        return !(tile instanceof NSilicon);
    }
}
