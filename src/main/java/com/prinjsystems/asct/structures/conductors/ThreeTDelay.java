package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile("logic")
public class ThreeTDelay extends Delay {
    private static final long serialVersionUID = 3555083952081827664L;

    public ThreeTDelay(int posX, int posY) {
        super(posX, posY, new Color(195, 0, 195), "3T Delay", "D3T");
        delay = 3;
    }
}
