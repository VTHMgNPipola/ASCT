package com.prinjsystems.asct.structures.conductors.coloredwires;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile("wiring")
public class BrownWire extends ColoredWire {
    private static final long serialVersionUID = 8885978249756702549L;

    public BrownWire(int posX, int posY) {
        super(posX, posY, Color.decode("#a55a0f"), "Brown Wire", "BRWN");
    }
}
