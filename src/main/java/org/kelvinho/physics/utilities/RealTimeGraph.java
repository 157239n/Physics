package org.kelvinho.physics.utilities;

import org.kelvinho.physics.errors.MismatchArraySize;
import org.kelvinho.physics.ui.DrawingArea;
import processing.core.PApplet;
import processing.core.PVector;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

@SuppressWarnings("unused")
public class RealTimeGraph {
    public static class Series {
        private String label;
        private LinkedList<Float> data;
        private int color;
        private int density;
        private PApplet sketch;
        private DrawingArea graphArea;
        private PVector range;

        public Series(@Nonnull String label, int color) {
            this.label = label;
            this.color = color;
            data = new LinkedList<>();
        }

        private void initialize(@Nonnull PApplet sketch, @Nonnull DrawingArea graphArea, @Nonnull PVector range, int density) {
            this.density = density;
            this.sketch = sketch;
            this.graphArea = graphArea;
            this.range = range;
            for (int i = 0; i < density; i++) {
                data.add(0.0f);
            }
        }

        private void newData(float data) {
            this.data.removeLast();
            this.data.addFirst(data);
        }

        private float map(float yValue) {
            return PApplet.map(yValue, range.x, range.y, graphArea.yRange().y, graphArea.yRange().x);
        }

        private void draw() {
            sketch.stroke(color);
            ListIterator<Float> listIterator = data.listIterator(1);
            float prev = map(data.get(0)); // most recent data
            float deltaX = graphArea.width() / (density - 1);
            float x = graphArea.xRange().y - deltaX;
            while (listIterator.hasNext()) {
                float now = map(listIterator.next());
                sketch.line(x, now, x + deltaX, prev);
                prev = now;
                x -= deltaX;
            }
        }
    }

    private List<Series> series;
    private DrawingArea drawingArea;
    private DrawingArea graphArea;
    private PApplet sketch;
    private Time time;
    private PVector range;

    /**
     * Setting things up to graph.
     *
     * @param series      The series
     * @param drawingArea The area where this graph will draw
     * @param sketch      The Processing sketch object
     * @param time        The Time object. Each increments
     * @param range       The range of values to graph. Aka range of y axis.
     */
    public RealTimeGraph(@Nonnull List<Series> series, @Nonnull DrawingArea drawingArea, @Nonnull PApplet sketch, @Nonnull Time time, @Nonnull PVector range) {
        this.series = series;
        this.drawingArea = drawingArea;
        this.sketch = sketch;
        this.time = time;
        this.graphArea = this.drawingArea.scaleDown(0.8f);
        this.range = range;
        for (Series series1 : this.series) {
            series1.initialize(sketch, graphArea, range, 20);
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public void graph(@Nonnull List<Float> values) {
        drawingArea.background(sketch);
        if (values.size() != series.size()) {
            throw new MismatchArraySize(values, series);
        }
        for (int i = 0; i < series.size(); i++) {
            series.get(i).newData(values.get(i));
            series.get(i).draw();
        }
        { // eliminate drawings that are out of the allowed range
            int previousFillColor = sketch.g.fillColor;
            int previousStrokeColor = sketch.g.strokeColor;
            sketch.fill(0);
            sketch.stroke(0);
            sketch.rect(drawingArea.xRange().x, drawingArea.yRange().x, drawingArea.width(), graphArea.yRange().x);
            sketch.fill(previousFillColor);
            sketch.fill(previousStrokeColor);
        }
        // then draws graph elements like axis
        sketch.fill(255);
        sketch.text(range.y, graphArea.xRange().x, graphArea.yRange().x);
        sketch.text(range.x, graphArea.xRange().x, graphArea.yRange().y);
    }
}
