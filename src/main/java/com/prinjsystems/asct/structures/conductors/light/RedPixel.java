package com.prinjsystems.asct.structures.conductors.light;

import java.awt.Color;

public class RedPixel extends Pixel {
    private static final long serialVersionUID = -4237471895261716386L;

    public RedPixel(int posX, int posY) {
        super(posX, posY, new Color(255, 50, 40), new Color(128, 25, 20));
    }
}
