package com.prinjsystems.asct.structures.conductors.semiconductors;

import java.awt.Color;

public class FastTransistor extends Transistor {
    private static final long serialVersionUID = 364817866116384021L;

    public FastTransistor(int posX, int posY) {
        super(posX, posY);
        color = new Color(202, 94, 234);
        conductiveColor = new Color(148, 65, 255);
        conductiveDelay = 2;
        unpoweredDelay = 2;
    }
}
