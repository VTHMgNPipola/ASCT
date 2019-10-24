package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.NSilicon;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.PSilicon;
import java.awt.Color;

@PlaceableTile("logic")
public class DFlipFlop extends Latch {
    private static final long serialVersionUID = -1629389174589173061L;

    private int clockCycles;

    public DFlipFlop(int posX, int posY) {
        super(posX, posY, new Color(255, 100, 100), "D Flip-Flop", "DFLP");
        clockCycles = -1;
    }

    @Override
    public void trySetPowered(boolean powered, Tile source) {
        if (source != null) {
            if (source instanceof NSilicon) { // NSilicon is the clock port
                clockCycles = 0;
            } else if (source instanceof PSilicon && clockCycles != -1) { // PSilicon is the D port
                conductive = powered;
                clockCycles = -1;
            } else if (conductive) {
                super.trySetPowered(powered, null);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (clockCycles != -1 && ++clockCycles == unpoweredDelay) {
            conductive = false;
            clockCycles = -1;
        }
    }

    @Override
    protected boolean isValid(Tile tile) {
        return !(tile instanceof NSilicon) && !(tile instanceof PSilicon);
    }
}
