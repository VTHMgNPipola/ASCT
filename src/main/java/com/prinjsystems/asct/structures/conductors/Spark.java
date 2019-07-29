package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asct.structures.ActionTile;
import java.awt.Color;

public class Spark extends ActionTile {
    public Spark() {
        super(0, 0, new Color(255, 191, 0), "Spark", true);
    }

    @Override
    public void tick() {
        throw new IllegalStateException("");
    }

    @Override
    public Color getColor() {
        return color;
    }
}
