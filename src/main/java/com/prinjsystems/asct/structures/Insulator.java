package com.prinjsystems.asct.structures;

import java.awt.Color;

public class Insulator extends StaticTile {
    private static final long serialVersionUID = -4285899857608657923L;

    public Insulator(int posX, int posY) {
        super(posX, posY, new Color(127, 140, 141));
        meltingTemp = 200f; // Rubber actually melts at 180°C but that seems too low, so I pushed it to 200°C.
        airIrradiationRatio = 0.0015f;
        irradiationRatio = 0.0025f;
    }
}
