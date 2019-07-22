package com.prinjsystems.asct.structures.conductors;

import java.awt.Color;

public class SlowClock extends Clock {
    private static final long serialVersionUID = -4795773074294504285L;

    public SlowClock(int posX, int posY) {
        super(posX, posY);
        color = new Color(105, 100, 0);
        delay = 8;
    }
}
