package org.kelvinho.physics.gravity;

import processing.core.PApplet;
import processing.core.PVector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings({"unused", "WeakerAccess"})
public class System {
    private float gravitationalConstant;
    private float drawScale;
    private ArrayList<Planet> planets;

    private System(@Nonnull ArrayList<Planet> planets, float gravitationalConstant, float drawScale) {
        this.planets = planets;
        this.gravitationalConstant = gravitationalConstant;
        this.drawScale = drawScale;
    }

    public static System random() {
        ArrayList<Planet> planets = new ArrayList<>();
        Random random = new Random();
        int numberOfPlanets = Math.round(random.nextFloat() * 10 + 50);
        for (int i = 0; i < numberOfPlanets; i++) {
            planets.add(new Planet(new PVector(random.nextFloat() * 400f - 200f, random.nextFloat() * 400f - 200f, 0), random.nextFloat() * 20f + 10f));
        }
        return new System(planets, 1f, 1f);
    }

    private static float exp(double a, int n) {
        return (float) (a * Math.pow(10, n));
    }

    public static System solarSystem() {
        ArrayList<Planet> planets = new ArrayList<>();
        planets.add(new Planet(new PVector(0, 0, 0), exp(1.9, 30))); // sun
        planets.add(new Planet(new PVector(exp(57.91, 9), 0, 0), new PVector(0, exp(47, 3), 0), exp(3.3, 23))); // mercury
        planets.add(new Planet(new PVector(exp(107, 9), 0, 0), new PVector(0, exp(35, 3), 0), exp(4.86, 24))); // venus
        planets.add(new Planet(new PVector(exp(150, 9), 0, 0), new PVector(0, exp(30, 3), 0), exp(5.97, 24))); // earth
        planets.add(new Planet(new PVector(exp(230, 9), 0, 0), new PVector(0, exp(24, 3), 0), exp(6.41, 23))); // mars
        planets.add(new Planet(new PVector(exp(780, 9), 0, 0), new PVector(0, exp(13, 3), 0), exp(1.89, 27))); // jupiter
        planets.add(new Planet(new PVector(exp(1.4, 12), 0, 0), new PVector(0, exp(9.68, 3), 0), exp(5.68, 26))); // saturn
        planets.add(new Planet(new PVector(exp(2.8, 12), 0, 0), new PVector(0, exp(6.8, 3), 0), exp(8.68, 25))); // uranus
        planets.add(new Planet(new PVector(exp(4.5, 12), 0, 0), new PVector(0, exp(5.43, 3), 0), exp(1.02, 26))); // neptune
        return new System(planets, exp(6.67, -11), exp(6, -11));
    }

    private void exertForces() {
        for (int i = 0; i < this.planets.size(); i++) {
            for (int j = i + 1; j < this.planets.size(); j++) {
                planets.get(i).exertForceBy(planets.get(j), gravitationalConstant);
                planets.get(j).exertForceBy(planets.get(i), gravitationalConstant);
            }
        }
    }

    public void clearForces() {
        for (Planet planet : planets) {
            planet.clearForce();
        }
    }

    public void update(float time) {
        exertForces();
        for (Planet planet : this.planets) {
            planet.update(time);
        }
    }

    public void draw(@Nonnull PApplet sketch) {
        for (Planet planet : this.planets) {
            planet.draw(sketch, drawScale);
        }
    }
}