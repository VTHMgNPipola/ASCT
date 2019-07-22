package com.prinjsystems.asct.structures.conductors;

import java.awt.Color;

public class CopperConductor extends ConductorTile {
    private static final long serialVersionUID = -8365509623662001747L;

    public CopperConductor(int posX, int posY) {
        super(posX, posY, new Color(217, 134, 53));
        meltingTemp = 1084.62f;
    }
}
