package com.prinjsystems.asct.structures.conductors.light;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.conductors.light.Pixel;
import java.awt.Color;

@PlaceableTile
public class BluePixel extends Pixel {
    private static final long serialVersionUID = -7671115856993306691L;

    public BluePixel(int posX, int posY) {
        super(posX, posY, new Color(80, 100, 255), new Color(0, 0, 128), "Blue Pixel");
    }
}
