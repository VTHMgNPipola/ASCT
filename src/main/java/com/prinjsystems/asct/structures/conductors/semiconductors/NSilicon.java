package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asct.structures.Tile;
import com.prinjsystems.asct.structures.conductors.ConductorTile;
import java.awt.Color;

public class NSilicon extends ConductorTile {
    private static final long serialVersionUID = -649556609668628613L;

    public NSilicon(int posX, int posY) {
        super(posX, posY, new Color(50, 100, 230));
    }

    @Override
    protected boolean isValid(Tile tile) {
        return !(tile instanceof PSilicon);
    }
}
