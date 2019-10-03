package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.conductors.ConductorTile;
import java.awt.Color;

@PlaceableTile
public class AluminiumConductor extends ConductorTile {
    private static final long serialVersionUID = -264047126453588482L;

    public AluminiumConductor(int posX, int posY) {
        super(posX, posY, new Color(205, 205, 207), "Aluminum Conductor");
        meltingTemp = 660.32f;
        airIrradiationRatio = 0.075f;
    }
}
