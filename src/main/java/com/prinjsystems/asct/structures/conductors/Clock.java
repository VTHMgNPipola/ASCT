package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.conductors.ConductorTile;
import java.awt.Color;

@PlaceableTile
public class Clock extends ConductorTile {
    private static final long serialVersionUID = -792645444317387412L;

    protected int delay = 4;
    private int cycles = 0;

    public Clock(int posX, int posY) {
        super(posX, posY, new Color(155, 149, 0), "Clock");
    }

    @Override
    public void update() {
        super.update();
        if (++cycles == delay) {
            powered = true;
            cycles = 0;
        }
    }
}
