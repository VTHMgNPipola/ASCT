package com.prinjsystems.asct.structures.conductors.coloredwires;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile("wiring")
public class GrayWire extends ColoredWire {
    private static final long serialVersionUID = 9171680455789107551L;

    public GrayWire(int posX, int posY) {
        super(posX, posY, Color.decode("#a5a5a5"), "Gray Wire", "GRAW");
    }
}
