package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile("logic")
public class TenTDelay extends Delay {
    private static final long serialVersionUID = 6486074407432958616L;

    public TenTDelay(int posX, int posY) {
        super(posX, posY, new Color(145, 0, 145), "10T Delay", "D10T");
        delay = 10;
    }
}
