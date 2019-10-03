package com.prinjsystems.asct.structures;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.StaticTile;
import java.awt.Color;

@PlaceableTile
public class ThermalConductor extends StaticTile {
    private static final long serialVersionUID = 6385750159086695226L;

    public ThermalConductor(int posX, int posY) {
        super(posX, posY, new Color(244, 244, 245), "Thermal Conductor");
        meltingTemp = -40f;
        irradiationRatio = 0.085f;
        airIrradiationRatio = 0.005f;
        viscosity = 8;
    }
}
