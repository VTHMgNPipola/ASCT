package com.prinjsystems.asct.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.structures.conductors.ConductorTile;
import com.prinjsystems.asctlib.structures.conductors.semiconductors.LogicGate;
import java.awt.Color;

public abstract class Latch extends LogicGate {
    private static final long serialVersionUID = -8501279453600521807L;

    protected Latch(int posX, int posY, Color color, String name, String shortenedName) {
        super(posX, posY, color, name, shortenedName);
        this.color = Color.white;
        conductiveDelay = -1;
    }

    @Override
    public Color getColor() {
        return conductive ? ConductorTile.POWERED_COLOR : color;
    }

    @Override
    public void update() {
        super.update();
        conductiveFor = 0;
    }
}
