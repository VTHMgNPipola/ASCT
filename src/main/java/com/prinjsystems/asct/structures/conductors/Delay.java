package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.ConductorTile;
import java.awt.Color;

public abstract class Delay extends ConductorTile {
    private static final long serialVersionUID = -8428020553547510729L;

    protected int delay;
    private int delayTicks;
    private boolean powering;
    private Delay previousPower;

    protected Delay(int posX, int posY, Color color, String name, String shortenedName) {
        super(posX, posY, color, name, shortenedName);
        delayTicks = 0;
    }

    @Override
    public void trySetPowered(boolean powered, Tile source) {
        super.trySetPowered(powered, source);
        if (this.powered) {
            this.powered = false;
            powering = true;
        }
        if (source instanceof Delay) {
            previousPower = (Delay) source;
        }
    }

    @Override
    public void update() {
        super.update();
        if (powering && ++delayTicks == delay + 1) {
            canReceivePower = false;
            unpoweredFor = 0;
            powered = true;
            powering = false;
            delayTicks = 0;
        }
    }

    @Override
    protected boolean isValid(Tile tile) {
        return tile != previousPower;
    }
}
