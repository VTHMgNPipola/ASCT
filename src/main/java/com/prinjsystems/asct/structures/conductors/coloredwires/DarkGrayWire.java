package com.prinjsystems.asct.structures.conductors.coloredwires;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile
public class DarkGrayWire extends ColoredWire {
    private static final long serialVersionUID = 8722118955625756259L;

    public DarkGrayWire(int posX, int posY) {
        super(posX, posY, Color.decode("#4b4b4b"), "Dark Gray Wire");
    }
}
