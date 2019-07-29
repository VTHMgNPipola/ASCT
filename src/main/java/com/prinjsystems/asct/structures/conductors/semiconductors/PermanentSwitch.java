package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asct.structures.ActionTile;
import com.prinjsystems.asct.structures.Tile;
import java.awt.Color;

public class PermanentSwitch extends Transistor {
    private static final long serialVersionUID = -7951692775675457336L;

    public PermanentSwitch(int posX, int posY) {
        super(posX, posY);
        name = "Permanent Switch";
        color = new Color(0, 88, 9);
        conductiveColor = new Color(0, 195, 0);
        conductiveDelay = -1;
    }

    @Override
    public void trySetPowered(boolean powered, Tile source) {
        if (source != null && canReceivePower) {
            if (source instanceof NSilicon) {
                conductive = false;
                conductiveFor = 0;
            } else if (source instanceof PSilicon) {
                conductive = true;
                conductiveFor = 0;
            } else if (source instanceof ActionTile && conductive) {
                this.powered = powered;
                canReceivePower = false;
                temp += 0.1f;
            }
        }
    }

    @Override
    public void update() {
        super.update();
        conductiveFor = 0;
    }
}
