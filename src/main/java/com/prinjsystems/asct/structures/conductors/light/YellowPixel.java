package com.prinjsystems.asct.structures.conductors.light;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.conductors.light.Pixel;
import java.awt.Color;

@PlaceableTile
public class YellowPixel extends Pixel {
    private static final long serialVersionUID = -5577521874091748559L;

    public YellowPixel(int posX, int posY) {
        super(posX, posY, new Color(255, 255, 0), new Color(120, 120, 50), "Yellow Pixel");
    }
}
