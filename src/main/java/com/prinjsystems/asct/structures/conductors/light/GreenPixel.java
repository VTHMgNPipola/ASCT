package com.prinjsystems.asct.structures.conductors.light;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.conductors.light.Pixel;
import java.awt.Color;

@PlaceableTile
public class GreenPixel extends Pixel {
    private static final long serialVersionUID = 6661620588007136293L;

    public GreenPixel(int posX, int posY) {
        super(posX, posY, new Color(0, 255, 0), new Color(20, 120, 20), "Green Pixel", "GREN");
    }
}
