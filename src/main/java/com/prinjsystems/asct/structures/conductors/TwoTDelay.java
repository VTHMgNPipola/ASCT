package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile("logic")
public class TwoTDelay extends Delay {
    private static final long serialVersionUID = 2840522733501983180L;

    public TwoTDelay(int posX, int posY) {
        super(posX, posY, new Color(215, 0, 215), "2T Delay", "D2T");
        delay = 2;
    }
}
