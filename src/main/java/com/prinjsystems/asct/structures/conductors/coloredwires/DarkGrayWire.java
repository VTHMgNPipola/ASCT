package com.prinjsystems.asct.structures.conductors.coloredwires;

import java.awt.Color;

public class DarkGrayWire extends ColoredWire {
    private static final long serialVersionUID = 8722118955625756259L;

    public DarkGrayWire(int posX, int posY) {
        super(posX, posY, Color.decode("#4b4b4b"));
    }
}
