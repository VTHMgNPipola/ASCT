package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile
public class SlowClock extends Clock {
    private static final long serialVersionUID = -4795773074294504285L;

    public SlowClock(int posX, int posY) {
        super(posX, posY);
        shortenedName = "SCLK";
        name = "Slow Clock";
        color = new Color(105, 100, 0);
        delay = 8;
    }
}
