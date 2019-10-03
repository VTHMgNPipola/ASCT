package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.ActionTile;
import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.NSilicon;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.PSilicon;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.Transistor;
import java.awt.Color;

@PlaceableTile
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
