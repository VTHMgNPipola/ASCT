package com.prinjsystems.asct.structures.conductors.coloredwires;

import java.awt.Color;

public class YellowWire extends ColoredWire {
    private static final long serialVersionUID = -1300726299365978690L;

    public YellowWire(int posX, int posY) {
        super(posX, posY, Color.decode("#e1e14b"), "Yellow Wire");
    }
}
