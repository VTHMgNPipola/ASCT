package com.prinjsystems.asct.structures.conductors.coloredwires;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile
public class CyanWire extends ColoredWire {
    private static final long serialVersionUID = 371051945540954066L;

    public CyanWire(int posX, int posY) {
        super(posX, posY, Color.decode("#4be1e1"), "Cyan Wire");
    }
}
