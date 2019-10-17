package org.kelvinho.physics.ui;

import processing.core.PApplet;
import processing.core.PVector;

import javax.annotation.Nonnull;

@SuppressWarnings({"WeakerAccess", "unused"})
public class DrawingArea {
    private PVector xRange;
    private PVector yRange;
    private float width, height;

    @SuppressWarnings("SuspiciousNameCombination")
    public static DrawingArea fitSketch(@Nonnull PApplet sketch) {
        return new DrawingArea(new PVector(0, sketch.width), new PVector(0, sketch.height));
    }

    /**
     * This will generate a DrawingArea that is a square of 600 by 600. If disLocation is 1, the whole square jumps into the next square.
     */
    public static DrawingArea standard(int disLocation) {
        return new DrawingArea(new PVector(600 * disLocation, 600 * disLocation + 600), new PVector(0, 600));
    }

    public DrawingArea(@Nonnull PVector xRange, @Nonnull PVector yRange) {
        this.xRange = xRange.copy();
        this.yRange = yRange.copy();
        this.width = PApplet.abs(xRange.x - xRange.y);
        this.height = PApplet.abs(yRange.x - yRange.y);
    }

    public PVector xRange() {
        return xRange.copy();
    }

    public PVector yRange() {
        return yRange.copy();
    }

    public float width() {
        return width;
    }

    public float height() {
        return height;
    }

    /**
     * Returns a new DrawingArea, scaled down a bit.
     *
     * @param scale Must be between 0 and 1
     * */
    public DrawingArea scaleDown(float scale) {
        if (scale > 1) {
            throw new IllegalArgumentException("Scale can't be larger than 1, you can't draw outside your boundary");
        }
        if (scale <= 0) {
            throw new IllegalArgumentException("Scale can't be less than or equal to 0, you don't have space to draw things in");
        }
        float xAvg = (xRange.x + xRange.y) / 2.0f;
        float yAvg = (yRange.x + yRange.y) / 2.0f;
        float xLength = scale * PApplet.abs(xRange.x - xRange.y) / 2.0f;
        float yLength = scale * PApplet.abs(yRange.x - yRange.y) / 2.0f;
        return new DrawingArea(new PVector(xAvg - xLength, xAvg + xLength), new PVector(yAvg - yLength, yAvg + yLength));
    }

    public void draw(@Nonnull PApplet sketch) {
        sketch.rect(xRange.x, yRange.x, width, height);
    }

    public void background(@Nonnull PApplet sketch) {
        int previousFillColor = sketch.g.fillColor;
        int previousStrokeColor = sketch.g.strokeColor;
        sketch.fill(0);
        sketch.stroke(0);
        sketch.rect(xRange.x, yRange.x, width, height);
        sketch.fill(previousFillColor);
        sketch.stroke(previousStrokeColor);
    }
}

