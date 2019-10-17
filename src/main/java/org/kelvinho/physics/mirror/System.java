package org.kelvinho.physics.mirror;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.function.Function;

import static processing.core.PApplet.cos;
import static processing.core.PApplet.sin;

@SuppressWarnings({"WeakerAccess", "unused"})
public class System {
    private ArrayList<Segment> mirrors;

    private System(@Nonnull ArrayList<Segment> segments) {
        this.mirrors = segments;
    }

    public static class Models {
        public static class Elements {
            @SuppressWarnings("SameParameterValue")
            private static ArrayList<Segment> boundingBox(float width, float height) {
                ArrayList<Segment> segments = new ArrayList<>();
                segments.add(Segment.segmentWithEndPoint(new PVector(4, 2), new PVector(width - 2, 4)));
                segments.add(Segment.segmentWithEndPoint(new PVector(width - 2, 4), new PVector(width - 4, height - 2)));
                segments.add(Segment.segmentWithEndPoint(new PVector(width - 4, height - 2), new PVector(2, height - 4)));
                segments.add(Segment.segmentWithEndPoint(new PVector(2, height - 4), new PVector(4, 2)));
                return segments;
            }

            private static ArrayList<Segment> contour(int iterations, Function<Float, PVector> contour) {
                ArrayList<Segment> segments = new ArrayList<>();
                float step = 1.0f / iterations;
                for (float i = 0.0f; i < 1.0f; i += step) {
                    segments.add(Segment.segmentWithEndPoint(contour.apply(i), contour.apply(i + step)));
                }
                return segments;
            }

            @SuppressWarnings("SameParameterValue")
            private static ArrayList<Segment> circle(@Nonnull PVector location, float radius, int numberOfSegments) {
                return contour(numberOfSegments, step -> {
                    float angle = PApplet.map(step, 0, 1, 0, PConstants.TWO_PI);
                    return new PVector(location.x + radius * cos(angle), location.y + radius * sin(angle));
                });
            }

            @SuppressWarnings("SameParameterValue")
            private static ArrayList<Segment> ellipse(@Nonnull PVector location, float semiHorizontalAxis, float semiVerticalAxis, int numberOfSegments) {
                return contour(numberOfSegments, step -> {
                    float angle = PApplet.map(step, 0, 1, 0, PConstants.TWO_PI);
                    return new PVector(location.x + semiHorizontalAxis * cos(angle), location.y + semiVerticalAxis * sin(angle));
                });
            }
        }

        public static class Simple {
            public static System simple() {
                ArrayList<Segment> segments = System.Models.Elements.boundingBox(600, 600);
                segments.add(Segment.segmentWithEndPoint(new PVector(100, 300), new PVector(500, 250)));
                segments.add(Segment.segmentWithEndPoint(new PVector(100, 100), new PVector(200, 400)));
                return new System(segments);
            }

            public static System box() {
                return new System(System.Models.Elements.boundingBox(600, 600));
            }
        }

        public static class Circle {
            public static System circle() {
                ArrayList<Segment> segments = System.Models.Elements.boundingBox(600, 600);
                segments.addAll(new ArrayList<>(System.Models.Elements.circle(new PVector(300, 300), 200, 32)));
                segments.add(Segment.segmentWithEndPoint(new PVector(100, 300), new PVector(500, 250)));
                segments.add(Segment.segmentWithEndPoint(new PVector(100, 100), new PVector(200, 400)));
                return new System(segments);
            }

            public static System loneCircle() {
                ArrayList<Segment> segments = System.Models.Elements.boundingBox(600, 600);
                segments.addAll(new ArrayList<>(System.Models.Elements.circle(new PVector(300, 300), 200, 300)));
                return new System(segments);
            }

            public static System linkedCircles() {
                ArrayList<Segment> segments = System.Models.Elements.boundingBox(600, 600);
                segments.addAll(new ArrayList<>(System.Models.Elements.circle(new PVector(250, 300), 100, 32)));
                segments.addAll(new ArrayList<>(System.Models.Elements.circle(new PVector(350, 300), 100, 32)));
                segments.add(Segment.segmentWithEndPoint(new PVector(100, 300), new PVector(500, 250)));
                segments.add(Segment.segmentWithEndPoint(new PVector(100, 100), new PVector(200, 400)));
                return new System(segments);
            }

            public static System dish() {
                ArrayList<Segment> segments = System.Models.Elements.boundingBox(600, 600);
                segments.addAll(System.Models.Elements.contour(500, step -> {
                    float angle = PApplet.map(step, 0, 1, PConstants.HALF_PI, PConstants.HALF_PI + PConstants.PI);
                    return new PVector(300 + 200 * cos(angle), 300 + 200 * sin(angle));
                }));
                return new System(segments);
            }
        }

        public static class Ellipse {
            public static System loneEllipse() {
                ArrayList<Segment> segments = System.Models.Elements.boundingBox(600, 600);
                segments.addAll(new ArrayList<>(System.Models.Elements.ellipse(new PVector(300, 300), 200, 100, 300)));
                return new System(segments);
            }

            public static System dish() {
                ArrayList<Segment> segments = System.Models.Elements.boundingBox(600, 600);
                segments.addAll(System.Models.Elements.contour(500, step -> {
                    float angle = PApplet.map(step, 0, 1, PConstants.HALF_PI, PConstants.HALF_PI + PConstants.PI);
                    return new PVector(300 + 200 * cos(angle), 300 + 100 * sin(angle));
                }));
                return new System(segments);
            }

            public static System smallDish() {
                ArrayList<Segment> segments = System.Models.Elements.boundingBox(600, 600);
                segments.addAll(System.Models.Elements.contour(500, step -> {
                    float angle = PApplet.map(step, 0, 1, PConstants.HALF_PI, PConstants.HALF_PI + PConstants.PI);
                    return new PVector(150 + 100 * cos(angle), 300 + 50 * sin(angle));
                }));
                return new System(segments);
            }
        }

        public static class Parabola {
            public static System large() {
                ArrayList<Segment> segments = System.Models.Elements.boundingBox(600, 600);
                segments.addAll(System.Models.Elements.contour(500, step -> {
                    float x = PApplet.map(step, 0, 1, -1, 1);
                    return new PVector(100 + x * x * 200, 300 + x * 250);
                }));
                return new System(segments);
            }
        }

        public static class FiberOptics {
            public static System straightVertical() {
                ArrayList<Segment> segments = System.Models.Elements.boundingBox(600, 600);
                segments.addAll(System.Models.Elements.contour(1, step -> new PVector(290, 100 + step * 400)));
                segments.addAll(System.Models.Elements.contour(1, step -> new PVector(310, 100 + step * 400)));
                return new System(segments);
            }

            public static System straightDiagonal() {
                ArrayList<Segment> segments = System.Models.Elements.boundingBox(600, 600);
                segments.addAll(System.Models.Elements.contour(1, step -> new PVector(100 + step * 400, 90 + step * 400)));
                segments.addAll(System.Models.Elements.contour(1, step -> new PVector(100 + step * 400, 110 + step * 400)));
                return new System(segments);
            }

            public static System bentCircularly() {
                ArrayList<Segment> segments = System.Models.Elements.boundingBox(600, 600);
                segments.addAll(System.Models.Elements.contour(100, step -> {
                    float angle = PApplet.map(step, 0, 1, 0, PConstants.HALF_PI);
                    return new PVector(490 - 400 * sin(angle), 490 - 400 * cos(angle));
                }));
                segments.addAll(System.Models.Elements.contour(100, step -> {
                    float angle = PApplet.map(step, 0, 1, 0, PConstants.HALF_PI);
                    return new PVector(510 - 400 * sin(angle), 510 - 400 * cos(angle));
                }));
                return new System(segments);
            }

            public static System bentElliptically() {
                ArrayList<Segment> segments = System.Models.Elements.boundingBox(600, 600);
                segments.addAll(System.Models.Elements.contour(100, step -> {
                    float angle = PApplet.map(step, 0, 1, 0, PConstants.HALF_PI);
                    return new PVector(490 - 400 * sin(angle), 390 - 200 * cos(angle));
                }));
                segments.addAll(System.Models.Elements.contour(100, step -> {
                    float angle = PApplet.map(step, 0, 1, 0, PConstants.HALF_PI);
                    return new PVector(510 - 400 * sin(angle), 410 - 200 * cos(angle));
                }));
                return new System(segments);
            }
        }

        public static class Telescope {
            public static System normal() {
                float innerLimit = 0.08f;
                float outerLimit = 0.8f;
                ArrayList<Segment> segments = System.Models.Elements.boundingBox(600, 600);
                segments.addAll(System.Models.Elements.contour(500, step -> {
                    float x = PApplet.map(step, 0, 1, -outerLimit, -innerLimit);
                    return new PVector(100 + x * x * 100, 300 + x * 250);
                }));
                segments.addAll(System.Models.Elements.contour(500, step -> {
                    float x = PApplet.map(step, 0, 1, innerLimit, outerLimit);
                    return new PVector(100 + x * x * 100, 300 + x * 250);
                }));
                float limit = 0.1f;
                segments.addAll(System.Models.Elements.contour(500, step -> {
                    float unit = PApplet.map(step, 0, 1, -1, 1);
                    float x = unit / 3.73f;
                    return new PVector(270 - x * x * 100, 300 + unit * 20);
                }));
                return new System(segments);
            }
        }
    }

    private Pair<PVector, Segment> intersect(@Nonnull Segment ray) {
        float minDistance = Float.POSITIVE_INFINITY;
        PVector minPoint = null;
        Segment minMirror = null;
        for (Segment mirror : mirrors) {
            PVector intersection = ray.boundedIntersectionPoint(mirror);
            if (intersection != null) {
                float distance = PVector.dist(intersection, ray.getPoint(0));
                if (distance < minDistance) {
                    minDistance = distance;
                    minPoint = intersection;
                    minMirror = mirror;
                }
            }
        }
        return new Pair<>(minPoint, minMirror);
    }

    private static class LightSource {
        static class Point {
            public static ArrayList<Segment> generic(@Nonnull PVector location, float heading, float fieldOfView) {
                ArrayList<Segment> rays = new ArrayList<>();
                for (float angle = heading - fieldOfView * 0.5f; angle < heading + fieldOfView * 0.5f; angle += fieldOfView * 0.0015) {
                    rays.add(Segment.ray(location.copy(), angle));
                }
                return rays;
            }

            public static ArrayList<Segment> uniform(@Nonnull PVector location) {
                return generic(location, 0, PConstants.TWO_PI);
            }

            public static ArrayList<Segment> beam(@Nonnull PVector location, float heading) {
                return generic(location, heading, PApplet.radians(30));
            }
        }

        static class Uniform {
            public static ArrayList<Segment> generic(@Nonnull PVector location, float heading, float width) {
                ArrayList<Segment> rays = new ArrayList<>();
                PVector ortho = new PVector(sin(heading), cos(heading));
                ortho.normalize();
                ortho.mult(width * 0.5f);
                for (float i = -1f; i < 1f; i += 0.004) {
                    rays.add(Segment.ray(PVector.add(location, PVector.mult(ortho, i)), heading));
                }
                return rays;
            }
        }
    }

    public void draw(@Nonnull PApplet sketch, @Nonnull PVector lightSourceLocation) {
        sketch.background(0);
        mirrors.forEach(segment -> segment.draw(sketch));

        //ArrayList<Segment> rays = LightSource.Point.uniform(lightSourceLocation);
        //ArrayList<Segment> rays = LightSource.Point.generic(lightSourceLocation, PConstants.PI, PConstants.PI * 2.0f / 3.0f);
        ArrayList<Segment> rays = LightSource.Uniform.generic(lightSourceLocation, PConstants.PI, 300);
        for (Segment ray : rays) {
            float lightIntensity = 1.0f;
            while (lightIntensity > 0.2) {
                Pair<PVector, Segment> a = intersect(ray);
                PVector intersection = a.getKey();
                Segment mirror = a.getValue();
                if (intersection != null) {
                    //sketch.loneEllipse(intersection.x, intersection.y, 10, 10);
                    float realLightIntensity = lightIntensity * 255;
                    sketch.stroke(255, realLightIntensity);
                    Segment.segment(ray.getPoint(0), PVector.sub(intersection, ray.getPoint(0))).draw(sketch);
                    float mirrorAngle = mirror.getAngle();
                    ray = Segment.ray(intersection, PConstants.PI + 2 * (mirrorAngle + PConstants.PI) - (ray.getAngle() + PConstants.PI));
                    ray.setLocation(ray.getPoint(0.1f));
                    lightIntensity *= 0.5;
                } else {
                    lightIntensity = 0.0f;
                }
            }
        }
        sketch.ellipse(lightSourceLocation.x, lightSourceLocation.y, 10, 10);
        sketch.text(sketch.frameRate, sketch.width - 40, 20);
    }
}
