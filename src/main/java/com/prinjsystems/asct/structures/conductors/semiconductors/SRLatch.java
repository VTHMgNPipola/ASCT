package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.NSilicon;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.PSilicon;
import java.awt.Color;

@PlaceableTile("logic")
public class SRLatch extends Latch {
    private static final long serialVersionUID = -1156518099461170565L;

    public SRLatch(int posX, int posY) {
        super(posX, posY, Color.gray, "SR Latch", "SRLT");
    }

    @Override
    public void trySetPowered(boolean powered, Tile source) {
        if (source != null) {
            if (source instanceof NSilicon) { // NSilicon is the S port
                conductive = true;
            } else if (source instanceof PSilicon) { // PSilicon is the R port
                conductive = false;
            } else if (conductive) {
                super.trySetPowered(powered, null);
            }
        }
    }

    @Override
    protected boolean isValid(Tile tile) {
        return !(tile instanceof NSilicon) && !(tile instanceof PSilicon);
    }
}
