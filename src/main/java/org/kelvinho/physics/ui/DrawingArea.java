package org.kelvinho.physics.ui;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import javax.annotation.Nonnull;

/**
 * Basically a rectangle. This sort of defines a drawing area, and you can draw stuff in it. Basically duplicate code of PGraphics, but with extra properties.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DrawingArea {
    private PVector xRange;
    private PVector yRange;
    private float width, height;
    private PGraphics pGraphics;
    private PApplet sketch;

    @SuppressWarnings("SuspiciousNameCombination")
    public static DrawingArea fitSketch(@Nonnull PApplet sketch) {
        return new DrawingArea(new PVector(0, sketch.width), new PVector(0, sketch.height));
    }

    /**
     * This will generate a DrawingArea that is a square of 600 by 600. If disLocation is 1, the whole square jumps into the next square.
     *
     * @param locationBias The location bias. Meaning, if it's (0, 1), then the drawing area will be from (0, 600) to (600, 12000)
     * @return DrawingArea object
     */
    public static DrawingArea standard(@Nonnull PVector locationBias) {
        int width = 600;
        int height = 600;
        return new DrawingArea(new PVector(width * locationBias.x, width * (locationBias.x + 1)), new PVector(height * locationBias.y, height * (locationBias.y + 1)));
    }

    /**
     * This will generate a DrawingArea that is a square of 600 by 600.
     *
     * @return DrawingArea object
     */
    public static DrawingArea standard() {
        return standard(new PVector(0, 0));
    }

    /**
     * Create a DrawingArea from x range and y range.
     *
     * @param xRange x range
     * @param yRange y range
     */
    private DrawingArea(@Nonnull PVector xRange, @Nonnull PVector yRange) {
        this.xRange = xRange.copy();
        this.yRange = yRange.copy();
        this.width = PApplet.abs(xRange.x - xRange.y);
        this.height = PApplet.abs(yRange.x - yRange.y);
    }

    /**
     * Registers the sketch to be drawn on
     *
     * @param sketch the sketch
     */
    public void setSketch(@Nonnull PApplet sketch) {
        this.sketch = sketch;
        this.pGraphics = sketch.createGraphics((int) width, (int) height);
        this.pGraphics.beginDraw();
    }

    /**
     * Get the x range
     */
    public PVector xRange() {
        return xRange.copy();
    }

    /**
     * Get the y range
     */
    public PVector yRange() {
        return yRange.copy();
    }

    /**
     * Get the width
     */
    public float width() {
        return width;
    }

    /**
     * Get the height
     */
    public float height() {
        return height;
    }

    /**
     * Returns a new DrawingArea, scaled down a bit.
     *
     * @param scale Must be between 0 and 1
     */
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

    /**
     * Sets stroke color
     *
     * @param color color
     */
    public void stroke(int color) {
        pGraphics.stroke(color);
    }

    /**
     * Sets fill color
     *
     * @param color color
     */
    public void fill(int color) {
        pGraphics.fill(color);
    }

    /**
     * Draws a background
     */
    public void background(int color) {
        fill(color);
        stroke(color);
        pGraphics.rect(xRange.x, yRange.x, width, height);
    }

    /**
     * Draws a background
     */
    public void background() {
        background(sketch.color(0));
    }

    /**
     * Draws a line
     *
     * @param x1 x of 1st point
     * @param y1 y of 1st point
     * @param x2 x of 2nd point
     * @param y2 y of 2nd point
     */
    public void line(float x1, float y1, float x2, float y2) {
        pGraphics.line(x1, y1, x2, y2);
    }

    /**
     * Draws a rectangle
     *
     * @param x x
     * @param y y
     * @param w w
     * @param h h
     */
    public void rect(float x, float y, float w, float h) {
        pGraphics.rect(x, y, w, h);
    }

    /**
     * Draws an ellipse
     *
     * @param x x
     * @param y y
     * @param c w
     * @param d h
     */
    public void ellipse(float x, float y, float c, float d) {
        pGraphics.ellipse(x, y, c, d);
    }

    /**
     * Actually draws the loaded elements
     */
    public void draw() {
        pGraphics.endDraw();
        sketch.image(pGraphics, xRange.x, yRange.x);
        pGraphics.beginDraw();
    }

}

