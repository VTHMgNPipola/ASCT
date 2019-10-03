package com.prinjsystems.asct.structures.conductors.coloredwires;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile
public class LightOrangeWire extends ColoredWire {
    private static final long serialVersionUID = 4938842388999474425L;

    public LightOrangeWire(int posX, int posY) {
        super(posX, posY, Color.decode("#e1c3b4"), "Light Orange");
    }
}
