package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile("logic")
public class EightTDelay extends Delay {
    private static final long serialVersionUID = -505596294769862295L;

    public EightTDelay(int posX, int posY) {
        super(posX, posY, new Color(135, 0, 135), "8T Delay", "D8T");
        delay = 8;
    }
}
