package org.kelvinho.physics.bouncing;

import org.kelvinho.physics.ui.ViewPoint;
import processing.core.PVector;

import javax.annotation.Nonnull;

@SuppressWarnings("WeakerAccess")
public class Border {
    private PVector startLocation;
    private PVector endLocation;

    public Border(@Nonnull PVector startLocation, @Nonnull PVector endLocation) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    public PVector getBounceForce(@Nonnull Particle particle, @Nonnull System system) {
        // these don't have proper names cause I just did and follow the math
        PVector a = startLocation;
        PVector b = endLocation;
        PVector e = PVector.sub(b, a);
        PVector c = particle.getLocation();
        float alpha = (PVector.dot(c, e) - PVector.dot(a, e)) / PVector.dot(e, e);
        PVector f = PVector.add(a, PVector.mult(e, alpha));
        PVector directional = PVector.sub(c, f);

        float distance = directional.mag();
        if (distance < system.getProperties().getMinDistance() && alpha >= 0 && alpha < 1) {
            float delta = system.getProperties().getMinDistance() - distance;
            directional.mult(delta * system.getProperties().getSpringConstant());
            //java.lang.System.out.println(directional.mag());
            return directional;
        } else {
            return new PVector(0, 0);
        }
    }

    public void draw(@Nonnull ViewPoint viewPoint) {
        viewPoint.line(startLocation.x, startLocation.y, endLocation.x, endLocation.y);
    }
}
