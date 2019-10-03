package com.prinjsystems.asct.structures.conductors.coloredwires;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile
public class OrangeWire extends ColoredWire {
    private static final long serialVersionUID = -6535711758857557027L;

    public OrangeWire(int posX, int posY) {
        super(posX, posY, Color.decode("#e1a578"), "Orange Wire");
    }
}
