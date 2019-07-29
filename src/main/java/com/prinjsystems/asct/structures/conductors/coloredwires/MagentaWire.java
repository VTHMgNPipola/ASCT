package com.prinjsystems.asct.structures.conductors.coloredwires;

import java.awt.Color;

public class MagentaWire extends ColoredWire {
    private static final long serialVersionUID = -6348082130617605925L;

    public MagentaWire(int posX, int posY) {
        super(posX, posY, Color.decode("#e14be1"), "Magenta Wire");
    }
}
