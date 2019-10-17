package org.kelvinho.physics.gravity;

import processing.core.PApplet;

import javax.annotation.Nonnull;
/**
 * Finished.
 * */
public class Entry extends PApplet {
    private System system;

    public static void main(@Nonnull String[] arguments) {
        PApplet.main("org.kelvinho.physics.gravity.Entry");
    }

    public void settings() {
        size(600, 600);
    }

    public void setup() {
        background(0);
        fill(255);
        //noStroke();
        system = System.random();
    }

    public void draw() {
        background(0);
        pushMatrix();
        translate(300, 300);
        //system.update(100000);
        float timeRatio = 4f; // how much 1 second in the real world correspond to in the simulated world? Assuming frameRate of 30fps
        int iterationsInTimeStep = 100; // how much iterations in 1 second in the real world?
        for (int i = 0; i < iterationsInTimeStep; i++) {
            system.update(timeRatio / (iterationsInTimeStep * frameRate * 1.0f));
        }
        system.draw(this);
        system.clearForces();
        popMatrix();
    }
}
