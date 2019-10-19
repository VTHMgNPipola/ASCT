package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.PSilicon;
import java.awt.Color;

@PlaceableTile("logic")
public class TLatch extends Latch {
    private static final long serialVersionUID = 1321033521887311188L;

    public TLatch(int posX, int posY) {
        super(posX, posY, new Color(100, 100, 255), "T Latch", "TLTC");
    }

    @Override
    public void trySetPowered(boolean powered, Tile source) {
        if (source != null) {
            if (source instanceof PSilicon) { // PSilicon is the T port
                conductive = !conductive;
            } else if (conductive) {
                super.trySetPowered(powered, null);
            }
        }
    }

    @Override
    public void update() {
        super.update();
        conductiveFor = 0;
    }
}
