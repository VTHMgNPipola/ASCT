package com.prinjsystems.asct.structures.conductors.coloredwires;

import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.ConductorTile;
import java.awt.Color;

public abstract class ColoredWire extends ConductorTile {
    private static final long serialVersionUID = -1605928447256168770L;

    protected ColoredWire(int posX, int posY, Color wireColor, String name, String shortenedName) {
        super(posX, posY, wireColor, name, shortenedName);
        meltingTemp = 1084.62f;
    }

    @Override
    protected boolean isValid(Tile tile) {
        return !(tile instanceof ColoredWire) || tile.getActualColor().equals(color);
    }
}
