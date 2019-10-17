package org.kelvinho.physics.bouncing;

import org.kelvinho.physics.ui.ViewPoint;
import processing.core.PVector;

import javax.annotation.Nonnull;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Particle {
    private PVector location;
    private PVector velocity;
    private PVector acceleration;
    private float mass;

    public Particle(@Nonnull PVector location, float mass) {
        this.location = location;
        this.velocity = new PVector(0, 0);
        this.acceleration = new PVector(0, 0);
        this.mass = mass;
    }

    public void update(float time) {
        //acceleration.mult(0.9f);
        velocity.add(PVector.mult(acceleration, time));
        //1velocity.mult(0.99999f); // this simulates drag. Kinda surprising that lowering the acceleration doesn't decrease the total energy. 0.9999999f is nice and slow
        location.add(PVector.mult(velocity, time));
        acceleration = new PVector(0, 0);
    }

    public PVector getLocation() {
        return location;
    }

    public PVector getVelocity() {
        return velocity;
    }

    public float getSpeed() {
        return velocity.mag();
    }

    public float getMass() {
        return mass;
    }

    public float getHeight() {
        return location.y;
    }

    public float getKineticEnergy() {
        float speed = getSpeed();
        return 0.5f * mass * speed * speed;
    }

    public float getPotentialEnergy(@Nonnull System.Properties properties) {
        return mass * getHeight() * Math.abs(properties.getGravitationalAcceleration());
    }

    public void applyAcceleration(@Nonnull PVector acceleration) {
        this.acceleration.add(acceleration);
    }

    public void applyForce(@Nonnull PVector force) {
        applyAcceleration(PVector.mult(force, 1.0f / mass));
    }

    /**
     * Let these 2 particles interact with one another. Both are updated.
     */
    public void interact(@Nonnull Particle particle, @Nonnull System system) {
        float distance = PVector.dist(particle.location, this.location);
        if (distance < system.getProperties().getMinDistance()) {
            float disLocation = system.getProperties().getMinDistance() - distance;
            PVector direction = PVector.sub(particle.location, this.location);
            direction.normalize();
            PVector force = PVector.mult(direction, system.getProperties().getSpringConstant() * disLocation);
            particle.applyForce(force);
            this.applyForce(PVector.mult(force, -1));
        }
    }

    public void interact(@Nonnull Border border, @Nonnull System system) {
        applyForce(border.getBounceForce(this, system));
    }

    public void draw(@Nonnull ViewPoint viewPoint) {
        viewPoint.ellipse(location.x, location.y, 20, 20);
    }
}
