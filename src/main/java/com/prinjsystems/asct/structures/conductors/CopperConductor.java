package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.conductors.ConductorTile;
import java.awt.Color;

@PlaceableTile("conductors")
public class CopperConductor extends ConductorTile {
    private static final long serialVersionUID = -8365509623662001747L;

    public CopperConductor(int posX, int posY) {
        super(posX, posY, new Color(217, 134, 53), "Copper Conductor", "COPR");
        meltingTemp = 1084.62f;
    }
}
