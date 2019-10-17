package org.kelvinho.physics.mirror;

import processing.core.PApplet;
import processing.core.PVector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Segment {
    private static class Types {
        static final int LINE = 0, RAY = 1, SEGMENT = 2;
    }

    private PVector location;
    private PVector direction;
    private int segmentType;

    private Segment(@Nonnull PVector location, @Nonnull PVector direction, int segmentType) {
        this.location = location;
        this.direction = direction;
        this.segmentType = segmentType;
    }

    public static Segment segment(@Nonnull PVector location, @Nonnull PVector direction) {
        return new Segment(location, direction, Types.SEGMENT);
    }

    public static Segment segmentWithEndPoint(@Nonnull PVector location1, @Nonnull PVector location2) {
        return Segment.segment(location1, PVector.sub(location2, location1));
    }

    public static Segment ray(@Nonnull PVector location, PVector direction) {
        return new Segment(location, direction, Types.RAY);
    }

    public static Segment ray(@Nonnull PVector location, float angle) {
        return new Segment(location, new PVector(PApplet.cos(angle), PApplet.sin(angle)), Types.RAY);
    }

    public static Segment line(@Nonnull PVector location, @Nonnull PVector direction) {
        return new Segment(location, direction, Types.LINE);
    }

    public static Segment vector(@Nonnull PVector direction) {
        return new Segment(new PVector(0, 0), direction, Types.LINE);
    }

    public boolean insideSegment(float directionalScalingFactor) {
        if (segmentType == Types.LINE) {
            return true;
        }
        if (segmentType == Types.RAY) {
            return directionalScalingFactor >= 0;
        }
        if (segmentType == Types.SEGMENT) {
            return directionalScalingFactor >= 0 && directionalScalingFactor < 1;
        }
        throw new AssertionError("This segmentType of " + segmentType + " does not exist. Please review the code again and only use constants from Segment.Types");
    }

    private float getDirectionalScaling(@Nonnull PVector point) {
        return (point.x - location.x) / direction.x;
    }

    public boolean contains(@Nonnull PVector point) {
        PVector vector = PVector.sub(point, location);
        float alpha = vector.x / direction.x;
        float beta = vector.y / direction.y;
        return Config.equals(alpha, beta) && insideSegment(alpha);
    }

    public void setLocation(@Nonnull PVector location) {
        this.location = location;
    }

    public PVector getPoint(float directionalScaling) {
        if (Config.equals(directionalScaling, 0.0f)) {
            return location;
        }
        return PVector.add(location, PVector.mult(direction, directionalScaling));
    }

    public float getAngle() {
        return PApplet.atan2(direction.y, direction.x);
    }

    /**
     * Return the directional scaling factor so that the resulting point (using getPoint()) A forming a line with the
     * outside point will be perpendicular to this segment.
     */
    public float perpendicularPoint(@Nonnull PVector point) {
        return PVector.dot(PVector.sub(location, point), point) / PVector.dot(direction, point);
    }

    /**
     * This just gets the intersection's directionalScaling without caring about boundaries.
     */
    @Nullable
    public PVector intersection(@Nonnull Segment segment) {
        Matrix matrix = new Matrix(this.direction, segment.direction).inverse();
        if (matrix == null) {
            return null;
        }
        PVector answer = matrix.operate(PVector.sub(segment.location, this.location));
        answer.y = -answer.y;
        return answer;
    }

    /**
     * Gets the intersection, while caring about each Segment's limits.
     */
    @Nullable
    public PVector boundedIntersection(@Nonnull Segment segment) {
        PVector intersection = intersection(segment);
        if (intersection == null) {
            return null;
        }
        if (this.insideSegment(intersection.x) && segment.insideSegment(intersection.y)) {
            return intersection;
        } else {
            return null;
        }
    }

    @Nullable
    public PVector boundedIntersectionPoint(@Nonnull Segment segment) {
        PVector directionalScaling = boundedIntersection(segment);
        if (directionalScaling == null) {
            return null;
        } else {
            return PVector.add(location, PVector.mult(direction, directionalScaling.x));
        }
    }

    public float distance(@Nonnull PVector point) {
        return PVector.sub(getPoint(perpendicularPoint(point)), point).mag();
    }

    public ArrayList<PVector> sketchIntersections(@Nonnull PApplet sketch) {
        ArrayList<Segment> boundaries = new ArrayList<>();
        boundaries.add(Segment.segment(new PVector(0, 0), new PVector(sketch.width, 0)));
        boundaries.add(Segment.segment(new PVector(0, 0), new PVector(0, sketch.height)));
        boundaries.add(Segment.segment(new PVector(sketch.width, sketch.height), new PVector(-sketch.width, 0)));
        boundaries.add(Segment.segment(new PVector(sketch.width, sketch.height), new PVector(0, -sketch.height)));
        return boundaries.stream().map(this::intersection).collect(Collectors.toCollection(ArrayList::new));
    }

    private boolean insideSketch(@Nonnull PApplet sketch) {
        return sketchIntersections(sketch).stream().filter(this::contains).collect(Collectors.toCollection(ArrayList::new)).size() > 0;
    }

    public void draw(@Nonnull PApplet sketch) {
        if (segmentType == Types.LINE) {
            ArrayList<PVector> intersections = sketchIntersections(sketch);
            sketch.line(intersections.get(0).x, intersections.get(0).y, intersections.get(1).x, intersections.get(1).y);
        } else if (segmentType == Types.RAY) {
            throw new RuntimeException("This is not an error. I just put it here because I'm lazy to implement this. And I won't probably use it anyway");
        } else if (segmentType == Types.SEGMENT) {
            sketch.line(location.x, location.y, location.x + direction.x, location.y + direction.y);
        }
    }
}
