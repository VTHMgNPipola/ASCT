package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.ConductorTile;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.NSilicon;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.PSilicon;
import java.awt.Color;

@PlaceableTile
public class ToggleSwitch extends ConductorTile {
    private static final long serialVersionUID = -7099513109699412277L;

    private boolean powerP = true;

    public ToggleSwitch(int posX, int posY) {
        super(posX, posY, new Color(255, 0, 255), "Toggle Switch");
    }

    @Override
    protected boolean isValid(Tile tile) {
        return tile instanceof PSilicon || tile instanceof NSilicon;
    }

    @Override
    protected void spread(Tile tile) {
        if (powerP && tile instanceof PSilicon) {
            ((PSilicon) tile).trySetPowered(powered, this);
        } else if (!powerP && tile instanceof NSilicon) {
            ((NSilicon) tile).trySetPowered(powered, this);
        }
    }

    @Override
    protected void postSpread() {
        super.postSpread();
        powerP = !powerP;
    }
}
