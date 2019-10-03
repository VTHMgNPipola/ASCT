package com.prinjsystems.asct.structures.conductors.coloredwires;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile
public class DarkGreenWire extends ColoredWire {
    private static final long serialVersionUID = -671223136443026282L;

    public DarkGreenWire(int posX, int posY) {
        super(posX, posY, Color.decode("#00a500"), "Dark Green Wire");
    }
}
