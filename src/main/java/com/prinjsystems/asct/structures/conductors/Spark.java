package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.ActionTile;
import java.awt.Color;

@PlaceableTile
public class Spark extends ActionTile {
    private static final long serialVersionUID = -1176647464574253132L;

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
