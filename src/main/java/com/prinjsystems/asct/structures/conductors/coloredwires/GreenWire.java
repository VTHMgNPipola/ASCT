package com.prinjsystems.asct.structures.conductors.coloredwires;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile("wiring")
public class GreenWire extends ColoredWire {
    private static final long serialVersionUID = -605739540331673730L;

    public GreenWire(int posX, int posY) {
        super(posX, posY, Color.decode("#4be14b"), "Green Wire", "GRNW");
    }
}
