package com.prinjsystems.asct.structures.conductors.coloredwires;

import java.awt.Color;

public class RedWire extends ColoredWire {
    private static final long serialVersionUID = 5693218070051416024L;

    public RedWire(int posX, int posY) {
        super(posX, posY, Color.decode("#e11e00"), "Red Wire");
    }
}
