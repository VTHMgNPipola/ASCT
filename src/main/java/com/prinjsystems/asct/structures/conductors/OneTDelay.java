package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile("logic")
public class OneTDelay extends Delay {
    private static final long serialVersionUID = -8243600227846555976L;

    public OneTDelay(int posX, int posY) {
        super(posX, posY, new Color(255, 0, 255), "1T Delay", "D1T");
        delay = 1;
    }
}
