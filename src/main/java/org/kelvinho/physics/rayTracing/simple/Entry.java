package org.kelvinho.physics.rayTracing.simple;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Not finished
 * */
public class Entry extends PApplet {
    private Scenery scenery;
    private ViewPoint viewPoint;
    private float angle;

    public static void main(String[] arguments) {
        PApplet.main("org.kelvinho.physics.rayTracing.simple.Entry");
    }

    public void settings() {
        size(600, 600);
    }

    public void setup() {
        scenery = Scenery.biggerBoxWithObstacle();
        background(0);
        fill(255);
        stroke(255);
    }

    public void draw() {
        background(0);
        viewPoint = new ViewPoint(new PVector(mouseX / 40f, mouseY / 40f), new PVector(cos(angle), sin(angle)));
        scenery.draw(this);
        scenery.rayTrace(this, viewPoint);
    }

    @Override
    public void keyPressed() {
        super.keyPressed();
        if (key == '+') {
            angle+=0.05;
        }
        if (key == '-') {
            angle-=0.05;
        }
    }
}
