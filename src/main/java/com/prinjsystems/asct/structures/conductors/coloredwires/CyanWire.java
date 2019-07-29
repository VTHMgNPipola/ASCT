package com.prinjsystems.asct.structures.conductors.coloredwires;

import java.awt.Color;

public class CyanWire extends ColoredWire {
    private static final long serialVersionUID = 371051945540954066L;

    public CyanWire(int posX, int posY) {
        super(posX, posY, Color.decode("#4be1e1"), "Cyan Wire");
    }
}
