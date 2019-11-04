package org.kelvinho.physics.ui;

import processing.core.PApplet;
import processing.core.PVector;

import javax.annotation.Nonnull;

/**
 * This basically converts from a mathematical screen to the actual screen.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ViewPoint {
    private PVector realCenter;
    private PVector realDimension;
    private DrawingArea drawingArea;
    private PApplet sketch;

    /**
     * These are for sketches that I usually use. All 600 by 600 pixels.
     */
    public static class Standards {
        /**
         * Centered, y direction flipped, 600 meters scale
         */
        public static ViewPoint centered(@Nonnull PApplet sketch) {
            return new ViewPoint(new PVector(0, 0), new PVector(600, 600), DrawingArea.standard(new PVector(0, 0)), sketch);
        }

        /**
         * Positive y, centered x, y direction flipped, 600 meters scale
         */
        public static ViewPoint floor(@Nonnull PApplet sketch) {
            return new ViewPoint(new PVector(0, 300), new PVector(600, 600), DrawingArea.standard(new PVector(0, 0)), sketch);
        }

        /**
         * Positive y, centered x, y direction flipped, 0.5 meters scale
         */
        public static ViewPoint smallFloor(@Nonnull PApplet sketch) {
            return new ViewPoint(new PVector(0, 0.25f), new PVector(0.5f, 0.5f), DrawingArea.standard(new PVector(0, 0)), sketch);
        }
    }

    public ViewPoint(@Nonnull PVector realCenter, @Nonnull PVector realDimension, @Nonnull DrawingArea drawingArea, @Nonnull PApplet sketch) {
        this.realCenter = realCenter.copy();
        this.realDimension = realDimension.copy();
        this.drawingArea = drawingArea;
        this.sketch = sketch;
        this.drawingArea.setSketch(sketch);
    }

    /**
     * This returns the screen's x coordinate of a real x coordinate.
     *
     * @param realX the real x coordinate
     */
    private float x(float realX) {
        return PApplet.map(realX, realCenter.x - realDimension.x / 2, realCenter.x + realDimension.x / 2, drawingArea.xRange().x, drawingArea.xRange().y);
    }

    /**
     * This returns the screen's y coordinate of a real y coordinate.
     *
     * @param realY the real y coordinate
     */
    private float y(float realY) {
        return PApplet.map(realY, realCenter.y - realDimension.y * 0.5f, realCenter.y + realDimension.y * 0.5f, drawingArea.yRange().y, drawingArea.yRange().x);
    }

    private float w(float realW) {
        return realW * drawingArea.width() / PApplet.abs(realDimension.x);
    }

    private float h(float realH) {
        return realH * drawingArea.height() / PApplet.abs(realDimension.y);
    }

    public void line(float x1, float y1, float x2, float y2) {
        drawingArea.line(x(x1), y(y1), x(x2), y(y2));
    }

    public void ellipse(float a, float b, float c, float d) {
        drawingArea.ellipse(x(a), y(b), c, d);
    }

    public void rect(float a, float b, float c, float d) {
        drawingArea.rect(x(a), y(b), w(c), h(d));
    }

    /**
     * Actually draws to the screen
     */
    public void draw() {
        drawingArea.draw();
    }

    /**
     * Gets the inner sketch
     *
     * @return the sketch
     */
    public PApplet getSketch() {
        return sketch;
    }

    /**
     * Gets the DrawingArea
     *
     * @return the DrawingArea
     */
    public DrawingArea getDrawingArea() {
        return drawingArea;
    }

    /**
     * Whether this real point is inside the sketch or not
     *
     * @param point real point
     * @return inside the sketch?
     */
    public boolean insideSketch(@Nonnull PVector point) {
        float screenX = x(point.x);
        float screenY = y(point.y);
        return screenX >= 0 && screenX < drawingArea.width() && screenY >= 0 && screenY < drawingArea.height();
    }

    /**
     * Return the real x range, but scaled with percentageLength.
     *
     * @param percentageLength percentage of real width
     * @return the new real x range
     */
    public PVector xRange(float percentageLength) {
        return new PVector(realCenter.x - percentageLength * PApplet.abs(realDimension.x * 0.5f), realCenter.x + percentageLength * PApplet.abs(realDimension.x * 0.5f));
    }

    /**
     * Return the real y range, but scaled with percentageLength.
     *
     * @param percentageLength percentage of real height
     * @return the new real y range
     */
    public PVector yRange(float percentageLength) {
        return new PVector(realCenter.y - percentageLength * PApplet.abs(realDimension.y * 0.5f), realCenter.y + percentageLength * PApplet.abs(realDimension.y * 0.5f));
    }
}
