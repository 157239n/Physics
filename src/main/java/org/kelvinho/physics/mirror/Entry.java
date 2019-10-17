package org.kelvinho.physics.mirror;

import processing.core.PApplet;
import processing.core.PVector;

/**
 * Finished
 */
public class Entry extends PApplet {
    private System system;

    private PVector lightSourceLocation;

    public static void main(String[] arguments) {
        PApplet.main("org.kelvinho.physics.mirror.Entry");
    }

    public void settings() {
        size(600, 600);
    }

    public void setup() {
        system = System.Models.Telescope.normal();
        lightSourceLocation = new PVector(300, 300);
        background(0);
        fill(0);
        stroke(255);
        strokeWeight(1);
    }

    public void draw() {
        system.draw(this, lightSourceLocation);
    }

    @Override
    public void mouseMoved() {
        super.mouseMoved();
        lightSourceLocation = new PVector(mouseX, mouseY);
    }

    public void keyTyped() {
        super.keyTyped();
        float step = 1;
        if (key == 'w') {
            lightSourceLocation.y -= step;
        }
        if (key == 's') {
            lightSourceLocation.y += step;
        }
        if (key == 'a') {
            lightSourceLocation.x -= step;
        }
        if (key == 'd') {
            lightSourceLocation.x += step;
        }
    }
}
