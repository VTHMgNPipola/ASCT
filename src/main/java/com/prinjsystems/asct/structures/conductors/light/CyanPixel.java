package com.prinjsystems.asct.structures.conductors.light;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.conductors.light.Pixel;
import java.awt.Color;

@PlaceableTile
public class CyanPixel extends Pixel {
    private static final long serialVersionUID = -1477336619750272330L;

    public CyanPixel(int posX, int posY) {
        super(posX, posY, new Color(0, 255, 255), new Color(50, 128, 128), "Cyan Pixel");
    }
}
