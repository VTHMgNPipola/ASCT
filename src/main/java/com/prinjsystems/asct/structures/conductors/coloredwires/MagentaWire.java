package com.prinjsystems.asct.structures.conductors.coloredwires;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile("wiring")
public class MagentaWire extends ColoredWire {
    private static final long serialVersionUID = -6348082130617605925L;

    public MagentaWire(int posX, int posY) {
        super(posX, posY, Color.decode("#e14be1"), "Magenta Wire", "MGTW");
    }
}
