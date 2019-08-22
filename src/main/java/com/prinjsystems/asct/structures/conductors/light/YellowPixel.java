package com.prinjsystems.asct.structures.conductors.light;

import java.awt.Color;

public class YellowPixel extends Pixel {
    private static final long serialVersionUID = -5577521874091748559L;

    public YellowPixel(int posX, int posY) {
        super(posX, posY, new Color(255, 255, 0), new Color(120, 120, 50), "Yellow Pixel");
    }
}
