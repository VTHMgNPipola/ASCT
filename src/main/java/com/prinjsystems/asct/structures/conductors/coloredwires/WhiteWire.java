package com.prinjsystems.asct.structures.conductors.coloredwires;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile
public class WhiteWire extends ColoredWire {
    private static final long serialVersionUID = 442476847705213233L;

    public WhiteWire(int posX, int posY) {
        super(posX, posY, Color.decode("#e1e1e1"), "White Wire");
    }
}
