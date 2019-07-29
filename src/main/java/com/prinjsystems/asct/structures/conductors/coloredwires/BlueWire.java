package com.prinjsystems.asct.structures.conductors.coloredwires;

import java.awt.Color;

public class BlueWire extends ColoredWire {
    private static final long serialVersionUID = 4714253428846656035L;

    public BlueWire(int posX, int posY) {
        super(posX, posY, Color.decode("#4b4be1"), "Blue Wire");
    }
}
