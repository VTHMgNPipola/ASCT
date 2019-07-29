package com.prinjsystems.asct;

import com.prinjsystems.asct.renderingengine.GameDisplay;
import com.prinjsystems.asct.structures.ActionTile;
import com.prinjsystems.asct.structures.GameMap;
import com.prinjsystems.asct.structures.Layer;
import com.prinjsystems.asct.structures.ThermalConductor;
import com.prinjsystems.asct.structures.Tile;
import com.prinjsystems.asct.structures.conductors.AluminiumConductor;
import com.prinjsystems.asct.structures.conductors.CopperConductor;
import com.prinjsystems.asct.structures.conductors.light.RedPixel;
import com.prinjsystems.asct.structures.conductors.semiconductors.NSilicon;
import com.prinjsystems.asct.structures.conductors.semiconductors.PSilicon;
import com.prinjsystems.asct.structures.conductors.semiconductors.ToggleSwitch;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainGameLoop {
    private static final float TARGET_FPS = 30f;
    private static final float TARGET_CLOCK_FREQUENCY = 30f; // Frequency in Hertz

    public static void main(String[] args) {
        Dimension resolution = new Dimension(1280, 768);
        for (String arg : args) {
            if (arg.matches("--res=\\d+x\\d+")) {
                String[] parts = arg.split("x");
                int width = Integer.parseInt(parts[0].substring(6));
                int height = Integer.parseInt(parts[1]);
                resolution = new Dimension(width, height);
            }
        }

        GameDisplay display = new GameDisplay(resolution);

        List<Tile> layer1 = new ArrayList<>();
        for (int y = 10; y < 18; y++) {
            for (int x = 2; x < 10; x++) {
                layer1.add(new RedPixel(x, y));
            }
        }

        List<Tile> layer2 = new ArrayList<>();
        CopperConductor starter = new CopperConductor(2, 1);
        starter.trySetPowered(true, null);
        layer2.add(starter);
        CopperConductor exitVia = new CopperConductor(2, 2);
        layer2.add(exitVia);
        CopperConductor entranceVia = new CopperConductor(2, 8);
        layer2.add(entranceVia);
        layer2.add(new CopperConductor(3, 8));
        layer2.add(new CopperConductor(4, 8));
        layer2.add(new CopperConductor(5, 8));
        layer2.add(new CopperConductor(6, 8));
        layer2.add(new CopperConductor(7, 8));
        layer2.add(new CopperConductor(8, 8));
        layer2.add(new CopperConductor(9, 8));
        layer2.add(new CopperConductor(9, 7));
        layer2.add(new CopperConductor(9, 6));
        layer2.add(new CopperConductor(9, 5));
        layer2.add(new CopperConductor(9, 4));
        layer2.add(new CopperConductor(9, 3));
        layer2.add(new CopperConductor(9, 2));
        layer2.add(new CopperConductor(9, 1));
        layer2.add(new CopperConductor(9, 1));
        layer2.add(new CopperConductor(8, 1));
        layer2.add(new CopperConductor(7, 1));
        CopperConductor moltenCopper = new CopperConductor(6, 1);
        moltenCopper.setTemp(2000f);
        layer2.add(moltenCopper);
        layer2.add(new CopperConductor(5, 1));
        layer2.add(new PSilicon(4, 1));
        layer2.add(new NSilicon(3, 1));

        layer2.add(new ThermalConductor(5, 2));
        layer2.add(new ThermalConductor(6, 2));
        layer2.add(new ThermalConductor(7, 2));
        layer2.add(new AluminiumConductor(4, 3));
        layer2.add(new AluminiumConductor(5, 3));
        layer2.add(new AluminiumConductor(6, 3));
        layer2.add(new AluminiumConductor(7, 3));
        layer2.add(new AluminiumConductor(8, 3));

        layer2.add(new CopperConductor(3, 9));
        PSilicon pSilicon = new PSilicon(2, 10);
        pSilicon.setConnectedTo(((ActionTile) layer1.get(0)));
        layer2.add(pSilicon);
        layer2.add(new ToggleSwitch(3, 10));
        NSilicon nSilicon = new NSilicon(4, 10);
        nSilicon.setConnectedTo(((ActionTile) layer1.get(2)));
        layer2.add(nSilicon);

        List<Tile> layer3 = new ArrayList<>();
        CopperConductor exit = new CopperConductor(2, 2);
        exitVia.setConnectedTo(exit);
        layer3.add(exit);
        layer3.add(new CopperConductor(2, 3));
        layer3.add(new CopperConductor(2, 4));
        layer3.add(new CopperConductor(2, 5));
        layer3.add(new CopperConductor(2, 6));
        layer3.add(new CopperConductor(2, 7));
        CopperConductor entrance = new CopperConductor(2, 8);
        entranceVia.setConnectedTo(entrance);
        layer3.add(entrance);

        List<Layer> layers = new ArrayList<>();
        layers.add(new Layer(layer1));
        layers.add(new Layer(layer2));
        layers.add(new Layer(layer3));
        GameMap map = new GameMap(layers);
        map.setCurrentLayer(1);
        display.setMap(map);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!display.isPaused()) {
                    display.tick();
                }
            }
        }, 2000, (int) (1000 / TARGET_CLOCK_FREQUENCY));

        display.setVisible(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                display.render();
                display.updateInputs();
            }
        }, 0, (int) (1000 / TARGET_FPS));
    }
}
