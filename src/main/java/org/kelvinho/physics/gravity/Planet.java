package org.kelvinho.physics.gravity;

import processing.core.PApplet;
import processing.core.PVector;

import javax.annotation.Nonnull;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Planet {
    private PVector location, velocity, acceleration;
    private float mass;

    public Planet(@Nonnull PVector location, @Nonnull PVector velocity, float mass) {
        this.location = location;
        this.velocity = velocity;
        this.acceleration = new PVector(0, 0, 0);
        this.mass = mass;
    }

    public Planet(@Nonnull PVector location, float mass) {
        this(location, new PVector(0, 0, 0), mass);
    }

    public void update(float time) {
        this.velocity.add(PVector.mult(this.acceleration, time));
        this.location.add(PVector.mult(this.velocity, time));
    }

    public void clearForce() {
        this.acceleration = new PVector(0, 0, 0);
    }

    public void addForce(@Nonnull PVector force) {
        this.acceleration.add(PVector.mult(force, 1 / this.mass));
    }

    public void exertForceBy(@Nonnull Planet planet, float gravitationalConstant) {
        PVector direction = PVector.sub(planet.location, this.location);
        float distance = Math.max(direction.mag(), 10);
        direction.normalize();
        float forceMagnitude = gravitationalConstant * this.mass / distance * planet.mass / distance;
        direction.mult(forceMagnitude);
        addForce(direction);
    }

    public void draw(@Nonnull PApplet sketch, float drawScale) {
        sketch.ellipse(this.location.x * drawScale, this.location.y * drawScale, 10, 10);
    }

    public PVector getLocation() {
        return location;
    }

    public PVector getVelocity() {
        return velocity;
    }

    public float getMass() {
        return mass;
    }
}

