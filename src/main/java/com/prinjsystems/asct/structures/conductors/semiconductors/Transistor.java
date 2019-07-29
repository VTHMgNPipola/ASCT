package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asct.structures.ActionTile;
import com.prinjsystems.asct.structures.Tile;
import com.prinjsystems.asct.structures.conductors.ConductorTile;
import java.awt.Color;

public class Transistor extends ConductorTile {
    private static final long serialVersionUID = 3056763776572443061L;

    protected int conductiveDelay = 4;
    protected Color conductiveColor;
    private int conductiveFor;
    private boolean conductive;
    private int poweredFromX = 0, poweredFromY = 0, conductingFromX, conductingFromY;

    public Transistor(int posX, int posY) {
        super(posX, posY, new Color(103, 75, 120), "Transistor");
        conductiveColor = new Color(220, 159, 255);
        meltingTemp = 1410f;
    }

    public boolean isConductive() {
        return conductive;
    }

    @Override
    public void trySetPowered(boolean powered, Tile source) {
        if (source != null && canReceivePower) {
            if (source instanceof NSilicon) {
                conductive = true;
                conductiveFor = 0;
                conductingFromX = source.getPosX();
                conductingFromY = source.getPosY();
            } else if (source instanceof PSilicon && conductive) {
                super.trySetPowered(true, null);
                poweredFromX = source.getPosX();
                poweredFromY = source.getPosY();
            }
        }
    }

    @Override
    public void update() {
        super.update();
        if (conductive) {
            if (++conductiveFor == conductiveDelay) {
                canReceivePower = true;
                conductive = false;
            }
        }
    }

    @Override
    public Color getColor() {
        // super#getColor() will always return the "turned on" color, since it will only be called when "powered"
        return powered ? super.getColor() : conductive ? conductiveColor : color;
    }

    @Override
    protected boolean isValid(Tile tile) {
        return (tile instanceof PSilicon && tile.getPosX() != poweredFromX && tile.getPosY() != poweredFromY) ||
                (tile instanceof NSilicon && tile.getPosX() != conductingFromX && tile.getPosY() != conductingFromY) ||
                tile instanceof ActionTile;
    }
}
