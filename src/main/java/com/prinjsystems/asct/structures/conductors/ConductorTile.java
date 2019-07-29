package com.prinjsystems.asct.structures.conductors;

import com.prinjsystems.asct.structures.ActionTile;
import com.prinjsystems.asct.structures.Tile;
import java.awt.Color;
import java.awt.Graphics2D;

public abstract class ConductorTile extends ActionTile {
    private static final long serialVersionUID = -1614333273624363555L;

    private ActionTile connectedTo; // If connectedTo is not null then the conductor/wire is a via

    protected ConductorTile(int posX, int posY, Color color, String name) {
        super(posX, posY, color, name, false);
    }

    @Override
    public final void tick() {
        if (powered) {
            Tile[] tilesAround = from.getTilesAround(posX, posY);
            for (Tile tile : tilesAround) {
                if (!(tile instanceof ActionTile)) {
                    continue;
                }
                if (isValid(tile)) {
                    run(tile);
                }
            }
            if (connectedTo != null) {
                run(connectedTo);
            }
            postRun();
        }
    }

    @Override
    public Color getColor() {
        return powered ? new Color(255, 191, 0) : color;
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        if (connectedTo != null) {
            g.setColor(new Color(255, 212, 0));
            g.drawRect(posX * TILE_SIZE, posY * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
        }
    }

    protected boolean isValid(Tile tile) {
        return true;
    }

    protected void run(Tile tile) {
        ((ActionTile) tile).trySetPowered(powered, this);
    }

    protected void postRun() {
        powered = false;
    }

    public void setConnectedTo(ActionTile connectedTo) {
        if (connectedTo != null && connectedTo.getPosX() == posX && connectedTo.getPosY() == posY
                && connectedTo != this) {
            this.connectedTo = connectedTo;
            if (connectedTo instanceof ConductorTile) {
                ((ConductorTile) connectedTo).setConnectedTo0(this);
            }
        } else if (connectedTo == null) {
            this.connectedTo = null;
        }
    }

    private void setConnectedTo0(ActionTile connectedTo) {
        this.connectedTo = connectedTo;
    }
}
