package com.prinjsystems.asct.structures.conductors.light;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.conductors.light.Pixel;
import java.awt.Color;

@PlaceableTile
public class WhitePixel extends Pixel {
    private static final long serialVersionUID = -3716714066108854307L;

    public WhitePixel(int posX, int posY) {
        super(posX, posY, new Color(255, 255, 255), new Color(128, 128, 128), "White Pixel", "WHTE");
    }
}
