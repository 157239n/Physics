package org.kelvinho.physics.bouncing;

import org.kelvinho.physics.ui.DrawingArea;
import org.kelvinho.physics.utilities.OnScreenLog;
import org.kelvinho.physics.utilities.RealTimeGraph;
import org.kelvinho.physics.utilities.Time;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Finished. To demonstrate evaporative cooling, press "w" to raise the wall height, "s" to lower the wall height and check out the energy that's left behind
 */
public class Entry extends PApplet {
    private System system;
    private OnScreenLog log;
    private Time time;
    private float borderHeight = 1.0f;
    private RealTimeGraph graph;

    public static void main(String[] arguments) {
        PApplet.main("org.kelvinho.physics.bouncing.Entry");
    }

    public void settings() {
        size(1200, 600);
    }

    public void setup() {
        system = System.Models.Realistic.lattice(this);
        log = new OnScreenLog(this, new ArrayList<>(Arrays.asList("Kinetic energy (J): ", "Potential energy (J): ", "Total energy (J): ", "KE per unit mass (J/kg): ", "PE per unit mass (J/kg): ", "Energy per unit mass(J/kg): ")), OnScreenLog.Location.TOP_RIGHT, 230);
        time = new Time(.1f, 3000);
        graph = new RealTimeGraph(new ArrayList<>(Arrays.asList(
                new RealTimeGraph.Series("Kinetic energy (J)", this.color(255, 0, 0)),
                new RealTimeGraph.Series("Potential energy (J)", this.color(0, 255, 0)),
                new RealTimeGraph.Series("Total energy (J)", this.color(0, 0, 255)),
                new RealTimeGraph.Series("Energy per unit mass (J/kg)", this.color(255))
        )), DrawingArea.standard(new PVector(1, 0)), this, time, new PVector(0, 100));
        background(0);
        stroke(255);
        fill(255);
    }

    public void draw() {
        background(0);
        system.update(time);
        system.draw();
        float ke = system.getProperties().getKineticEnergy();
        float pe = system.getProperties().getPotentialEnergy();
        float mass = system.getProperties().getMass();
        graph.graph(new ArrayList<>(Arrays.asList(ke, pe, (ke + pe), 20 * (ke + pe) / mass)));
        log.display(new ArrayList<>(Arrays.asList(ke + "", pe + "", (ke + pe) + "", ke / mass + "", pe / mass + "", (ke + pe) / mass + "")));
    }

    @Override
    public void keyPressed() {
        super.keyPressed();
        if (key == 'w') {
            borderHeight = 1 - (1 - borderHeight) * 0.9f;
            //borderHeight += 5;
            system.setBorderHeight(borderHeight);
        }
        if (key == 's') {
            borderHeight = borderHeight * 0.9f;
            //borderHeight -= 5;
            system.setBorderHeight(borderHeight);
        }
    }
}
