package com.prinjsystems.asct.structures.conductors.coloredwires;

import java.awt.Color;

public class WhiteWire extends ColoredWire {
    private static final long serialVersionUID = 442476847705213233L;

    public WhiteWire(int posX, int posY) {
        super(posX, posY, Color.decode("#e1e1e1"));
    }
}
