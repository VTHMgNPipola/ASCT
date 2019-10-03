package com.prinjsystems.asct.structures.conductors.light;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.conductors.light.Pixel;
import java.awt.Color;

@PlaceableTile
public class RedPixel extends Pixel {
    private static final long serialVersionUID = -4237471895261716386L;

    public RedPixel(int posX, int posY) {
        super(posX, posY, new Color(255, 0, 0), new Color(128, 25, 20), "Red Pixel");
    }
}
