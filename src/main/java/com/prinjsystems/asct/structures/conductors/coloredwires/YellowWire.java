package com.prinjsystems.asct.structures.conductors.coloredwires;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile("wiring")
public class YellowWire extends ColoredWire {
    private static final long serialVersionUID = -1300726299365978690L;

    public YellowWire(int posX, int posY) {
        super(posX, posY, Color.decode("#e1e14b"), "Yellow Wire", "YLLW");
    }
}
