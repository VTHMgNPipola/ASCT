package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile
public class FastClock extends Clock {
    private static final long serialVersionUID = 8031799081661809603L;

    public FastClock(int posX, int posY) {
        super(posX, posY);
        shortenedName = "FCLK";
        name = "Fast Clock";
        color = new Color(225, 222, 82);
        delay = 2;
    }
}
