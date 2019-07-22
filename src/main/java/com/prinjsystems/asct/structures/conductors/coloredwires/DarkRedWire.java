package com.prinjsystems.asct.structures.conductors.coloredwires;

import java.awt.Color;

public class DarkRedWire extends ColoredWire {
    private static final long serialVersionUID = -5376973305980681278L;

    public DarkRedWire(int posX, int posY) {
        super(posX, posY, Color.decode("#a50000"));
    }
}
