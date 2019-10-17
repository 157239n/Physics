package org.kelvinho.physics.rayTracing.simple;

import processing.core.PVector;

import javax.annotation.Nonnull;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ViewPoint {
    private PVector location;
    private PVector direction;

    /**
     * Creates a new ViewPoint, representing a person looking at a scenery
     */
    public ViewPoint(@Nonnull PVector location, @Nonnull PVector direction) {
        this.location = location;
        this.direction = direction;
        this.direction.normalize();
    }

    private ViewPoint() {
    }

    public ViewPoint clone() {
        ViewPoint clone;
        try {
            clone = (ViewPoint) super.clone();
        } catch (CloneNotSupportedException e) {
            clone = new ViewPoint();
        }
        clone.location = location.copy();
        clone.direction = direction.copy();
        return clone;
    }

    public void advance(float step) {
        location.add(PVector.mult(direction, step));
    }

    public PVector getLocation() {
        return location;
    }

    public PVector getDirection() {
        return direction;
    }
}
