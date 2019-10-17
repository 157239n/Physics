package org.kelvinho.physics.bouncing;

import org.kelvinho.physics.ui.DrawingArea;
import org.kelvinho.physics.ui.ViewPoint;
import org.kelvinho.physics.utilities.Time;
import processing.core.PApplet;
import processing.core.PVector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings({"WeakerAccess", "unused"})
public class System {
    private Properties properties;
    private ArrayList<Particle> particles;
    private ArrayList<Border> borders;
    private ViewPoint viewPoint;

    @SuppressWarnings("SuspiciousNameCombination")
    private static ArrayList<Border> createBorder(@Nonnull ViewPoint viewPoint, float percentageHeight) {
        PVector xRange = viewPoint.xRange(1);
        PVector yRange = viewPoint.yRange(1);
        ArrayList<Border> borders = new ArrayList<>();
        borders.add(new Border(new PVector(xRange.x, yRange.x), new PVector(xRange.x, yRange.y * percentageHeight))); // left side
        borders.add(new Border(new PVector(xRange.x, yRange.x), new PVector(xRange.y, yRange.x))); // floor
        borders.add(new Border(new PVector(xRange.y, yRange.x), new PVector(xRange.y, yRange.y * percentageHeight))); // right side
        borders.add(new Border(new PVector(xRange.x, yRange.y), new PVector(xRange.y, yRange.y))); // ceiling
        return borders;
    }

    private static float random(@Nonnull Random random, @Nonnull PVector bound) {
        return random.nextFloat() * (bound.y - bound.x) + bound.x;
    }

    public class Properties {
        private float springConstant;
        private float minDistance;
        private float gravitationalAcceleration;

        private Properties(float springConstant, float minDistance, float gravitationalAcceleration) {
            this.springConstant = springConstant;
            this.minDistance = minDistance;
            this.gravitationalAcceleration = gravitationalAcceleration;
        }

        public float getSpringConstant() {
            return springConstant;
        }

        public float getMinDistance() {
            return minDistance;
        }

        public float getGravitationalAcceleration() {
            return gravitationalAcceleration;
        }


        public float getKineticEnergy() {
            float energy = 0;
            for (Particle particle : particles) {
                energy += particle.getKineticEnergy();
            }
            return energy;
        }

        public float getPotentialEnergy() {
            float energy = 0;
            for (Particle particle : particles) {
                energy += particle.getPotentialEnergy(this);
            }
            return energy;
        }

        public float getEnergy() {
            return getKineticEnergy() + getPotentialEnergy();
        }

        public float getMass() {
            float mass = 0;
            for (Particle particle : particles) {
                mass += particle.getMass();
            }
            return mass;
        }
    }

    public static class Models {
        public static class Elements {
            @SuppressWarnings("SameParameterValue")
            private static ArrayList<Particle> randomParticles(@Nonnull ViewPoint viewPoint, @Nonnull PVector massRange, int numberOfParticles, @Nullable Random random) {
                if (random == null) {
                    random = new Random(157239L);
                }
                PVector xRange = viewPoint.xRange(0.8f);
                PVector yRange = viewPoint.yRange(0.8f);
                ArrayList<Particle> particles = new ArrayList<>();
                for (int i = 0; i < numberOfParticles; i++) {
                    particles.add(new Particle(new PVector(random(random, xRange), random(random, yRange)), random(random, massRange)));
                }
                return particles;
            }

            private static ArrayList<Particle> lattice(@Nonnull ViewPoint viewPoint, @Nonnull PVector massRange, int row, int column, @Nullable Random random) {
                if (random == null) {
                    random = new Random(157239L);
                }
                PVector xRange = viewPoint.xRange(0.8f);
                PVector yRange = viewPoint.yRange(0.8f);
                float width = xRange.y - xRange.x;
                float height = yRange.y - yRange.x;
                ArrayList<Particle> particles = new ArrayList<>();
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < column; j++) {
                        particles.add(new Particle(new PVector(xRange.x + j * width / column + i * 0.001f, yRange.x + i * height / row), random(random, massRange)));
                    }
                }
                return particles;
            }
        }

        public static class Huge {
            public static System simple(@Nonnull PApplet sketch) {
                ViewPoint viewPoint = ViewPoint.Standards.floor(sketch);
                return new System(viewPoint, Elements.randomParticles(viewPoint, new PVector(20, 60), 10, null), createBorder(viewPoint, 1.0f), 2000f, 25, -100f);
            }

            public static System many(@Nonnull PApplet sketch) {
                ViewPoint viewPoint = ViewPoint.Standards.floor(sketch);
                return new System(viewPoint, Elements.randomParticles(viewPoint, new PVector(20, 60), 60, null), createBorder(viewPoint, 1.0f), 2000f, 25, -100f);
            }
        }

        public static class Realistic {
            public static System simple(@Nonnull PApplet sketch) {
                ViewPoint viewPoint = ViewPoint.Standards.smallFloor(sketch);
                return new System(viewPoint, Elements.randomParticles(viewPoint, new PVector(0.2f, 0.5f), 40, new Random()), createBorder(viewPoint, 1.0f), 2000000f, 0.02f, -9.8f);
            }

            public static System lattice(@Nonnull PApplet sketch) {
                ViewPoint viewPoint = ViewPoint.Standards.smallFloor(sketch);
                return new System(viewPoint, Elements.lattice(viewPoint, new PVector(0.2f, 0.5f), 5, 5, new Random()), createBorder(viewPoint, 1.0f), 2000000000f, 0.02f, -9.8f);
            }
        }
    }

    private System(@Nonnull ViewPoint viewPoint, @Nonnull ArrayList<Particle> particles, @Nonnull ArrayList<Border> borders, float springConstant, float minDistance, float gravitationalAcceleration) {
        this.viewPoint = viewPoint;
        this.particles = particles;
        this.borders = borders;
        this.properties = new Properties(springConstant, minDistance, gravitationalAcceleration);
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * Updates the system.
     */
    public void update(Time time) {
        for (int w = 0; w < time.getIterations(); w++) {
            for (int i = 0; i < particles.size(); i++) {
                for (int j = i; j < particles.size(); j++) {
                    particles.get(i).interact(particles.get(j), this);
                }
            }
            for (Particle particle : particles) {
                for (Border border : borders) {
                    particle.interact(border, this);
                }
                particle.applyAcceleration(new PVector(0, properties.gravitationalAcceleration));
            }
            for (Particle particle : particles) {
                particle.update(time.getTimeStep());
            }
        }

        // removing particles when it's outside of the beaker, to simulate evaporative cooling.
        for (int i = particles.size() - 1; i >= 0; i--) {
            if (!viewPoint.insideSketch(particles.get(i).getLocation())) {
                particles.remove(i);
            }
        }
    }

    public void draw() {
        PApplet sketch = viewPoint.getSketch();
        sketch.stroke(255);
        sketch.fill(255);
        sketch.strokeWeight(10);
        for (Border border : borders) {
            border.draw(viewPoint);
        }
        sketch.strokeWeight(1);
        for (Particle particle : particles) {
            particle.draw(viewPoint);
        }
    }

    public void setBorderHeight(float percentageHeight) {
        borders = createBorder(viewPoint, percentageHeight);
    }
}
