package com.prinjsystems.asct.structures.conductors.light;

import java.awt.Color;

public class WhitePixel extends Pixel {
    private static final long serialVersionUID = -3716714066108854307L;

    public WhitePixel(int posX, int posY) {
        super(posX, posY, new Color(255, 255, 255), new Color(128, 128, 128), "White Pixel");
    }
}
