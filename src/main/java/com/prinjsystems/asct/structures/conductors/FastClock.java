package com.prinjsystems.asct.structures.conductors;

import java.awt.Color;

public class FastClock extends Clock {
    private static final long serialVersionUID = 8031799081661809603L;

    public FastClock(int posX, int posY) {
        super(posX, posY);
        name = "Fast Clock";
        color = new Color(225, 222, 82);
        delay = 2;
    }
}
