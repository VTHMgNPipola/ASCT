package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile("logic")
public class FourTDelay extends Delay {
    private static final long serialVersionUID = 8485611983241113041L;

    public FourTDelay(int posX, int posY) {
        super(posX, posY, new Color(175, 0, 175), "4T Delay", "D4T");
        delay = 4;
    }
}
