package com.prinjsystems.asct.structures.conductors.coloredwires;

import com.prinjsystems.asctlib.PlaceableTile;
import java.awt.Color;

@PlaceableTile
public class DarkBlueWire extends ColoredWire {
    private static final long serialVersionUID = 6683826458959395498L;

    public DarkBlueWire(int posX, int posY) {
        super(posX, posY, Color.decode("#0000a5"), "Dark Blue Wire");
    }
}
