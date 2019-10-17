package org.kelvinho.physics;

import org.kelvinho.physics.ui.ViewPoint;
import processing.core.PApplet;

public class Testing extends PApplet {

    public static void main(String[] arguments) {
        PApplet.main("org.kelvinho.physics.Testing");
    }

    public void settings() {
        size(600, 600);
    }

    public void setup() {
        background(0);
        fill(255);
        stroke(255);
        ViewPoint.Standards.floor(this).line(-300, 0, -300, 500);
        System.out.println(PApplet.map(0, 0, 600, 600, 0));
        ellipse(200, 500, 20, 20);
    }

    public void draw() {
    }
}
